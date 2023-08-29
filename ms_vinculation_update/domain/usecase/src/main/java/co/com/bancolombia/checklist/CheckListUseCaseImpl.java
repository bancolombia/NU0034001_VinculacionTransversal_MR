package co.com.bancolombia.checklist;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.activity.Activity;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.startlist.StartList;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.execfield.ExecFieldUseCase;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
import co.com.bancolombia.model.clauseacquisitionchecklist.gateways.ClauseAcquisitionCheckListRepository;
import co.com.bancolombia.statestep.StateStepUseCase;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONSULT_INSTRUCTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_USER_ACQUISITION_INITIAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_STATUS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATE_FORMAT_TIME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_MSG_MATRIX_ACQUISITION_NOT_FOUND_DB;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_MSG_STATE_NOT_FOUND_DB;

@RequiredArgsConstructor
public class CheckListUseCaseImpl implements CheckListUseCase {

    private final AcquisitionValidateUseCase acquisitionValidateUseCase;
    private final AcquisitionUseCase acquisitionUseCase;
    private final StateStepUseCase stateStepUseCase;
    private final CheckListRepository checkListRepository;
    private final ExecFieldUseCase execFieldUseCase;
    private final ClauseAcquisitionCheckListRepository cAcCheckListRepository;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, CODE_CONSULT_INSTRUCTIONS);

    @Override
    public List<CheckList> createCheckList(UUID acqId, String usrMod) {
        Optional<StateStep> opState = this.stateStepUseCase.findByCode("1");
        if (!opState.isPresent()) {
            throw new CustomException(ERROR_MSG_STATE_NOT_FOUND_DB);
        }
        Acquisition acquisition = this.acquisitionUseCase.findAndValidateById(acqId);
        List<MatrixAcquisition> matrices = acquisition.getTypeAcquisition().getAcquisitionMatrices();
        matrices.stream().forEach(m -> {
            CheckList currCheckList = CheckList.builder()
                    .acquisition(acquisition)
                    .step(m.getStep())
                    .state(opState.get())
                    .createdBy(usrMod)
                    .build();
            currCheckList = this.checkListRepository.save(currCheckList);
            this.execFieldUseCase.insertExecFieldByMAcqAndChecklist(m, currCheckList);
        });
        List<CheckList> checkList = new ArrayList<>();
        return this.checkListRepository.saveAll(checkList);
    }

    @Override
    public void markOperation(UUID acqId, String stepCode, String stateCode) {
        Acquisition acquisition = this.acquisitionUseCase.findAndValidateById(acqId);
        Optional<MatrixAcquisition> opMatrixAcquisition = acquisition.getTypeAcquisition()
                .getAcquisitionMatrices()
                .stream()
                .filter(m -> m.getStep() != null &&
                        m.getStep().getCode().equals(stepCode))
                .findFirst();
        if (opMatrixAcquisition.isPresent()) {
            Optional<StateStep> opState = this.stateStepUseCase.findByCode(stateCode);
            if (!opState.isPresent()) {
                adapter.error(ERROR + ERROR_MSG_STATE_NOT_FOUND_DB);
                throw new CustomException(ERROR_MSG_STATE_NOT_FOUND_DB);
            }
            CheckList checkList = this.checkListRepository.findByAcquisitionAndStep(acquisition,
                    opMatrixAcquisition.get().getStep());
            checkList = checkList.toBuilder()
                    .state(opState.get()).updatedDate(new CoreFunctionDate().getDatetime())
                    .updatedBy(CODE_USER_ACQUISITION_INITIAL.toUpperCase()).build();
            checkListRepository.save(checkList);
        } else {
            adapter.error(ERROR + ERROR_MSG_MATRIX_ACQUISITION_NOT_FOUND_DB);
            throw new CustomException(ERROR_MSG_MATRIX_ACQUISITION_NOT_FOUND_DB);
        }
    }

    @Override
    public List<StartList> getProcessesCheckList(
            UUID acqId, String docType, String docNumber, String operation
    ) {
        List<Acquisition> acquisitions = this.acquisitionValidateUseCase
                .getAllByOpAcqIdDocTypeAndDocNum(acqId, docType, docNumber, operation);
        return acquisitions.stream().map(acquisition -> {
            List<ClauseAcquisitionCheckList> cAcq = this.cAcCheckListRepository.findByAcquisition(acquisition);
            List<ClauseAcquisitionCheckList> transformCAcq = new ArrayList<>();
            cAcq.forEach(c -> {
                Optional<ClauseAcquisitionCheckList> optMIC = transformCAcq.stream().filter(t ->
                        t.getMatrixTypeAcquisitionClause().getClause().getCode().equals(
                                c.getMatrixTypeAcquisitionClause().getClause().getCode()
                        )).findFirst();
                if (!optMIC.isPresent()) {
                    transformCAcq.add(c);
                }
            });
            return StartList.builder()
                    .activitiesList(this.getAcquisitionActivities(acquisition.getId()))
                    .acquisitionId(acquisition.getId().toString()).documentType(docType).documentNumber(docNumber)
                    .registrationDate(new SimpleDateFormat(DATE_FORMAT_TIME).format(acquisition.getCreatedDate()))
                    .codeStateAcquisition(acquisition.getStateAcquisition().getCode())
                    .nameStateAcquisition(acquisition.getStateAcquisition().getName())
                    .clauseAcquisitionCheckLists(transformCAcq).build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<CheckList> getCheckListByAcquisition(Acquisition acquisition) {
        return checkListRepository.findByAcquisition(acquisition);
    }

    public List<Activity> getAcquisitionActivities(UUID acqId) {
        Acquisition acquisition = this.acquisitionUseCase.findAndValidateById(acqId);
        List<CheckList> checklist = this.checkListRepository.findByAcquisition(acquisition);
        List<Activity> activities = new ArrayList<>();
        checklist.stream().forEach(item -> {
            String nameStateActivity = item.getState() == null ? null : item.getState().getName();
            String codeStateActivity = item.getState() == null ? null : item.getState().getCode();
            Activity tmp = Activity.builder()
                    .idActivity(item.getStep().getId().toString())
                    .nameActivity(item.getStep().getName())
                    .codeStateActivity(codeStateActivity)
                    .nameStateActivity(nameStateActivity)
                    .build();
            activities.add(tmp);

        });
        this.markOperation(acqId, CODE_VALIDATE_STATUS, CODE_ST_OPE_COMPLETADO);
        return activities;
    }
}
