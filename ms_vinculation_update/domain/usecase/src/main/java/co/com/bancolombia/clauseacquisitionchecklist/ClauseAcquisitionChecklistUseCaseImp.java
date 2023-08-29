package co.com.bancolombia.clauseacquisitionchecklist;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.markrevoke.MarkRevokeUseCase;
import co.com.bancolombia.matrixtypeacquisitionclause.MatrixTypeAcquisitionClauseUseCase;
import co.com.bancolombia.model.acquisition.BasicAcquisitionRequest;
import co.com.bancolombia.model.clauseacquisitionchecklist.gateways.ClauseAcquisitionCheckListRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ACCEPT_CLAUSES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO_PARCIAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_RECHAZADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CLAUSES_NOT_VALID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CLAUSES_PREVIOUSLY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NOT_ACCEPT_CLAUSES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITHOUT_LINK;

@RequiredArgsConstructor
public class ClauseAcquisitionChecklistUseCaseImp implements ClauseAcquisitionChecklistUseCase {

    private final ClauseAcquisitionCheckListRepository repository;
    private final MatrixTypeAcquisitionClauseUseCase clauseUseCase;
    private final AcquisitionValidateUseCase acquisitionValidateUseCase;
    private final AcquisitionOperationUseCase acquisitionOperationUseCase;
    private final CoreFunctionDate coreFunctionDate;
    private final CheckListUseCase checkListUseCase;
    private final MarkRevokeUseCase markRevokeUseCase;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, CODE_ACCEPT_CLAUSES);

    @Override
    public void startCheckList(TypeAcquisition typeAcquisition, Acquisition acquisition) {
        List<MatrixTypeAcquisitionClause> list = clauseUseCase.findByTypeAcquisitionAndActive(
                MatrixTypeAcquisitionClause.builder().typeAcquisition(typeAcquisition).build());

        List<ClauseAcquisitionCheckList> checkLists = new ArrayList<>();

        list.forEach(item -> checkLists.add(ClauseAcquisitionCheckList.builder().createdBy(item.getCreatedBy())
                .createdDate(new CoreFunctionDate().getDatetime())
                .matrixTypeAcquisitionClause(item)
                .acquisition(acquisition).build()));
        this.saveAll(checkLists);
    }

    @Override
    public void saveAll(List<ClauseAcquisitionCheckList> list) {
        repository.saveAll(list);
    }

    @Override
    public List<ClauseAcquisitionCheckList> findByAcquisition(Acquisition acquisition) {
        return this.repository.findByAcquisition(acquisition);
    }

    @Override
    public Acquisition acceptClause(BasicAcquisitionRequest request, String acceptClauses, String clauseCode,
                                    String operation) {
        boolean acceptClause = acceptClauses.equals(Numbers.ONE.getNumber());
        Acquisition acq = this.validateAcquisition(request, operation);
        List<ClauseAcquisitionCheckList> list = findByAcquisition(acq);
        List<ClauseAcquisitionCheckList> checkLists = validatePreviousAcceptClause(list, clauseCode);

        checkLists.forEach(item -> {
            item.setAcceptClause(acceptClause);
            item.setDateAcceptClause(coreFunctionDate.getDatetime());
            item.setUpdatedBy(request.getUserTransaction());
            item.setUpdatedDate(coreFunctionDate.getDatetime());
        });
        saveAll(checkLists);
        if (!acceptClause) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            this.checkListUseCase.markOperation(acq.getId(), CODE_ACCEPT_CLAUSES, CODE_ST_OPE_RECHAZADO);
            acquisitionOperationUseCase.updateAcquisition(acq, Numbers.TWO.getNumber());
            adapter.error(ERROR + ERROR_CODE_NOT_ACCEPT_CLAUSES +
                    Collections.singletonList(ErrorField.builder().build()));
            error.put(ERROR_CODE_NOT_ACCEPT_CLAUSES, Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
        markRevokeUseCase.checkOperation(checkLists, acq, request.getUserTransaction());
        markOperationClause(acq);
        return acq;
    }

    @Override
    public Acquisition validateAcquisition(BasicAcquisitionRequest request, String operation) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        Optional<Acquisition> acqOpt = acquisitionValidateUseCase
                .validateInfoSearchAndGet(request.getIdAcq(), request.getDocumentType(),
                        request.getDocumentNumber(), operation);
        error.put(ERROR_CODE_WITHOUT_LINK, Collections.singletonList(ErrorField.builder().build()));
        return acqOpt.orElseThrow(() -> new ValidationException(error));
    }

    @Override
    public void markOperationClause(Acquisition acq) {
        List<ClauseAcquisitionCheckList> list = findByAcquisition(acq);
        Long res = list.stream().filter(item -> item.getDateAcceptClause() == null).count();
        if (res == 0) {
            this.checkListUseCase.markOperation(acq.getId(), CODE_ACCEPT_CLAUSES, CODE_ST_OPE_COMPLETADO);
        } else {
            this.checkListUseCase.markOperation(acq.getId(), CODE_ACCEPT_CLAUSES, CODE_ST_OPE_COMPLETADO_PARCIAL);
        }
    }

    @Override
    public List<ClauseAcquisitionCheckList> validatePreviousAcceptClause(
            List<ClauseAcquisitionCheckList> lists, String clauseCode) {
        List<ClauseAcquisitionCheckList> checkLists = new ArrayList<>();
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        boolean flagClause = false;
        for (ClauseAcquisitionCheckList item : lists) {
            if (item.getMatrixTypeAcquisitionClause().getClause().getCode().equals(clauseCode)) {
                flagClause = true;
                if (item.getDateAcceptClause() == null) {
                    checkLists.add(item);
                }
            }
        }
        if (!flagClause) {
            error.put(ERROR_CODE_CLAUSES_NOT_VALID, Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
        if (checkLists.isEmpty()) {
            error.put(ERROR_CODE_CLAUSES_PREVIOUSLY, Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
        return checkLists;
    }
}
