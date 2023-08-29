package co.com.bancolombia.instruction;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.containeraction.ContainerAction;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixInfoClause;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.acquisition.ClauseInstructions;
import co.com.bancolombia.model.acquisition.ClauseInstructionsWithAcquisition;
import co.com.bancolombia.model.clauseacquisitionchecklist.gateways.ClauseAcquisitionCheckListRepository;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONSULT_INSTRUCTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_RECHAZADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITHOUT_CLAUSES;

@RequiredArgsConstructor
public class InstructionClausesUseCaseImpl implements InstructionClausesUseCase {
    private final AcquisitionValidateUseCase acquisitionValidateUseCase;
    private final AcquisitionOperationUseCase acquisitionUseCase;
    private final CheckListUseCase checkListUseCase;
    private final ClauseAcquisitionCheckListRepository cAcCheckListRepository;
    private final InstructionUseCase instructionUseCase;
    private final InstructionSearchUseCase instructionSearchUseCase;

    @Override
    public List<MatrixInfoClause> searchClauses(Acquisition acquisition) {
        instructionUseCase.validateAcquisitionInstructions(acquisition);
        List<ClauseAcquisitionCheckList> clausLists = cAcCheckListRepository.findByAcquisition(acquisition);
        if (clausLists.isEmpty()) {
            this.checkListUseCase.markOperation(acquisition.getId(), CODE_CONSULT_INSTRUCTIONS, CODE_ST_OPE_RECHAZADO);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            acquisitionUseCase.updateAcquisition(acquisition, Numbers.TWO.getNumber());
            error.put(ERROR_CODE_WITHOUT_CLAUSES, Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
        List<MatrixInfoClause> mTyAcCla = new ArrayList<>();
        clausLists.forEach(clause -> {
            MatrixTypeAcquisitionClause matrixClause = clause.getMatrixTypeAcquisitionClause();
            Optional<MatrixInfoClause> optMIC = mTyAcCla.stream()
                    .filter(actions -> actions.getClause().getCode().equals(matrixClause.getClause().getCode())
                            && actions.getStep().getCode().equals(matrixClause.getStep().getCode())
                            && actions.getSequence() == matrixClause.getSequence())
                    .findFirst();
            if (!optMIC.isPresent()) {
                mTyAcCla.add(MatrixInfoClause.builder().clause(matrixClause.getClause()).step(matrixClause.getStep())
                        .sequence(matrixClause.getSequence())
                        .containerActions(Collections.singletonList(matrixClause.getAction())).build());
            } else {
                List<ContainerAction> containerActions = new ArrayList<>(optMIC.get().getContainerActions());
                containerActions.add(matrixClause.getAction());
                optMIC.get().setContainerActions(containerActions);
            }
        });
        return mTyAcCla;
    }

    @Override
    public ClauseInstructionsWithAcquisition searchClausesAndInstructions(
            String acqId, String documentType, String documentNumber, String operation) {
        Acquisition acquisition = acquisitionValidateUseCase.
                validateInfoSearchAndGet(acqId, documentType, documentNumber, operation).orElse(null);
        List<MatrixInfoClause> clauses = new ArrayList<>();
        List<MatrixInstruction> mInstructions = new ArrayList<>();
        if (acquisition != null) {
            clauses = searchClauses(acquisition);
            mInstructions = instructionSearchUseCase.searchInstructions(acquisition);
            this.checkListUseCase.markOperation(acquisition.getId(), CODE_CONSULT_INSTRUCTIONS, CODE_ST_OPE_COMPLETADO);
        }
        return ClauseInstructionsWithAcquisition.builder()
                .clauseInstructions(ClauseInstructions.builder().clauses(clauses).instructions(mInstructions).build())
                .acquisition(acquisition).build();
    }
}
