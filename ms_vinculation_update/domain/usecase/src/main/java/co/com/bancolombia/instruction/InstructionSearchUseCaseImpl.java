package co.com.bancolombia.instruction;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.matrixacquisition.MatrixAcquisitionUseCase;
import co.com.bancolombia.matrixinstruction.MatrixInstructionUseCase;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONSULT_INSTRUCTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_RECHAZADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITHOUT_INSTRUCTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_MSG_MATRIX_ACQUISITION_NOT_FOUND_DB;

@RequiredArgsConstructor
public class InstructionSearchUseCaseImpl implements InstructionSearchUseCase {
    private final AcquisitionOperationUseCase acquisitionUseCase;
    private final MatrixAcquisitionUseCase matrixAcquisitionUseCase;
    private final MatrixInstructionUseCase matrixInstructionUseCase;
    private final CheckListUseCase checkListUseCase;
    private final InstructionUseCase instructionUseCase;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, CODE_CONSULT_INSTRUCTIONS);

    @Override
    public List<MatrixInstruction> searchInstructions(Acquisition acquisition) {
        instructionUseCase.validateAcquisitionInstructions(acquisition);
        List<MatrixAcquisition> mAcList = matrixAcquisitionUseCase
                .findByTypeAcquisition(acquisition.getTypeAcquisition());
        if (mAcList.isEmpty()) {
            adapter.error(ERROR + ERROR_MSG_MATRIX_ACQUISITION_NOT_FOUND_DB);
            throw new CustomException(ERROR_MSG_MATRIX_ACQUISITION_NOT_FOUND_DB);
        }
        List<MatrixInstruction> mInstructions = matrixInstructionUseCase.findAllByMatrixAcquisition(mAcList).stream()
                .filter(MatrixInstruction::getActive).collect(Collectors.toCollection(ArrayList::new));
        if (mInstructions.isEmpty()) {
            this.checkListUseCase.markOperation(acquisition.getId(), CODE_CONSULT_INSTRUCTIONS, CODE_ST_OPE_RECHAZADO);
            acquisitionUseCase.updateAcquisition(acquisition, Numbers.TWO.getNumber());
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_WITHOUT_INSTRUCTIONS, Collections.singletonList(ErrorField.builder().build()));
            adapter.error(ERROR + ERROR_CODE_WITHOUT_INSTRUCTIONS
                    + Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
        return mInstructions;
    }
}
