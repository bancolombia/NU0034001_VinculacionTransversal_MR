package co.com.bancolombia.acquisition;

import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.clauseacquisitionchecklist.ClauseAcquisitionChecklistUseCase;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.matrixtypeacquisition.MatrixTypeAcquisitionUseCase;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRepository;
import co.com.bancolombia.model.matrixtypeacquisition.MatrixTypeAcquisition;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;
import co.com.bancolombia.stateacquisition.StateAcquisitionUseCase;
import co.com.bancolombia.util.catalog.CatalogUtilUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_USER_ACQUISITION_INITIAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_START_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STATE_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_SIN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PROFILING;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITH_ACTIVE_LINK;

@RequiredArgsConstructor
public class AcquisitionOperationUseCaseImpl implements AcquisitionOperationUseCase {

    private final AcquisitionUseCase acquisitionUseCase;
    private final StateAcquisitionUseCase stateAcquisitionUseCase;
    private final CheckListUseCase checkListUseCase;
    private final ClauseAcquisitionChecklistUseCase checklistClauseUseCase;
    private final MatrixTypeAcquisitionUseCase matrixTypeAcquisitionUseCase;
    private final CatalogUtilUseCase catalogUtilUseCase;
    private final AcquisitionOpeValidateUseCase acqOpeValidateUseCase;
    private final AcquisitionRepository acquisitionRepository;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, OPER_START_ACQUISITION);

    @Override
    public Acquisition startAcquisition(
            AcquisitionRequestModel acquisitionRequestModel, String usrMod, String operation) {
        acqOpeValidateUseCase.validateCustomerControl(acquisitionRequestModel.getDocumentType(),
                acquisitionRequestModel.getDocumentNumber());
        catalogUtilUseCase.validateCatalogs(acquisitionRequestModel);
        AcquisitionStartObjectModel acqSOM = catalogUtilUseCase.transformObjectCatalog(acquisitionRequestModel);
        Acquisition acquisitionInstance = acquisitionUseCase.findAcquisition(acqSOM);

        if (acquisitionInstance != null) {
            adapter.error(ERROR + ERROR_CODE_WITH_ACTIVE_LINK);
            throw new ValidationException(new MapErrorAcquisition(ERROR_CODE_WITH_ACTIVE_LINK,
                    ErrorField.builder().build()).map);
        }
        Optional<MatrixTypeAcquisition> matrixTypeAcquisition = matrixTypeAcquisitionUseCase.search(acqSOM);
        if (!matrixTypeAcquisition.isPresent()) {
            adapter.error(ERROR + ERROR_CODE_PROFILING);
            throw new ValidationException(new MapErrorAcquisition(ERROR_CODE_PROFILING,
                    ErrorField.builder().build()).map);
        }
        acqSOM.setTypeAcquisition(matrixTypeAcquisition.get().getTypeAcquisition());
        ValidateSessionResponse sessionResponse = acqOpeValidateUseCase.getValidateSessionResponse(
                acquisitionRequestModel, operation);
        acquisitionInstance = saveAcquisition(acqSOM, usrMod);
        if (acquisitionInstance != null) {
            acquisitionUseCase.setAcquisitionMatrices(acquisitionInstance);
            checkListUseCase.createCheckList(acquisitionInstance.getId(), usrMod);
            checklistClauseUseCase.startCheckList(acquisitionInstance.getTypeAcquisition(), acquisitionInstance);
            checkListUseCase.markOperation(acquisitionInstance.getId(), operation, Numbers.TWO.getNumber());
            acqOpeValidateUseCase.saveValidateSession(sessionResponse, acquisitionInstance, operation);
        }
        return acquisitionInstance;
    }

    @Override
    public Acquisition saveAcquisition(AcquisitionStartObjectModel acquisitionStartObjectModel, String usrMod) {
        StateAcquisition stateAcquisition = stateAcquisitionUseCase
                .findByCode(Numbers.ONE.getNumber()).orElseThrow(() -> {
                    adapter.error(ERROR + ERROR_CODE_CATALOG_SIN);
                    return new ValidationException(new MapErrorAcquisition(ERROR_CODE_CATALOG_SIN,
                            ErrorField.builder().name(Numbers.ONE.getNumber()).
                                    complement(STATE_ACQUISITION).build()).map);
                });
        Acquisition acquisition = Acquisition.builder().documentType(acquisitionStartObjectModel.getDocumentType())
                .documentNumber(acquisitionStartObjectModel.getDocumentNumber())
                .typePerson(acquisitionStartObjectModel.getTypePerson())
                .typeProduct(acquisitionStartObjectModel.getTypeProduct())
                .typeChannel(acquisitionStartObjectModel.getTypeChannel())
                .businessLine(acquisitionStartObjectModel.getBusinessLine()).stateAcquisition(stateAcquisition)
                .typeAcquisition(acquisitionStartObjectModel.getTypeAcquisition()).suitable(true)
                .createdBy(usrMod).createdDate(new CoreFunctionDate().getDatetime()).build();
        return acquisitionRepository.save(acquisition);
    }

    @Override
    public Acquisition save(Acquisition acquisition) {
        acquisition.setUpdatedBy(CODE_USER_ACQUISITION_INITIAL);
        acquisition.setUpdatedDate(new CoreFunctionDate().getDatetime());
        return acquisitionRepository.save(acquisition);
    }

    @Override
    public void updateAcquisition(Acquisition ocp, String code) {
        StateAcquisition stateAcquisition = stateAcquisitionUseCase.findByCode(code).orElseThrow(() -> {
            adapter.error(ERROR + ERROR_CODE_CATALOG_SIN);
            return new ValidationException(new MapErrorAcquisition(ERROR_CODE_CATALOG_SIN,
                    ErrorField.builder().name(code).complement(STATE_ACQUISITION).build()).map);
        });
        ocp.setStateAcquisition(stateAcquisition);
        ocp.setUpdatedBy(CODE_USER_ACQUISITION_INITIAL);
        ocp.setUpdatedDate(new CoreFunctionDate().getDatetime());
        acquisitionRepository.save(ocp);
    }

    @Override
    public long countAcquisitionByState(String state, List<UUID> acquisitionIdList){
        return acquisitionRepository.countAcquisitionByState(state, acquisitionIdList);
    }
}