package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.documenttype.DocumentTypeUseCase;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRepository;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.prerequisitesstep.PrerequisitesStepUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_START_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPE_MARKCUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_ACQUISITION_TIME_VALIDITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MARK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NUMBER_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_TYPE_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITHOUT_LINK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers.FOUR;

@RequiredArgsConstructor
public class AcquisitionValidateUseCaseImpl implements AcquisitionValidateUseCase {
    private final AcquisitionUseCase acquisitionUseCase;
    private final AcquisitionRepository acquisitionRepository;
    private final AcquisitionOperationUseCase acquisitionOperationUseCase;
    private final ParametersUseCase parametersUseCase;
    private final DocumentTypeUseCase documentTypeUseCase;
    private final PrerequisitesStepUseCase prerequisitesStepUseCase;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, OPER_START_ACQUISITION);

    @Override
    public Optional<Acquisition> validateInfoSearchAndGet(
            String idAcq, String documentType, String documentNumber, String operation) {
        Acquisition acOp = null;
        if (idAcq != null && idAcq.length() > 0) {
            acOp = acquisitionUseCase.findById(UUID.fromString(idAcq));
        }
        acquisitionValidations(acOp, operation);
        if (acOp != null) {
            if (documentType.compareTo(acOp.getDocumentType().getCode()) != 0) {
                adapter.error(ERROR + ERROR_CODE_TYPE_DOCUMENT + MIDDLE_SCREEN + ErrorField.builder().build());
                throw new ValidationException(
                        new MapErrorAcquisition(ERROR_CODE_TYPE_DOCUMENT, ErrorField.builder().build()).map);
            }
            if (documentNumber.compareTo(acOp.getDocumentNumber()) != 0) {
                adapter.error(ERROR + ERROR_CODE_NUMBER_DOCUMENT + MIDDLE_SCREEN + ErrorField.builder().build());
                throw new ValidationException(
                        new MapErrorAcquisition(ERROR_CODE_NUMBER_DOCUMENT, ErrorField.builder().build()).map);
            }
            validatePrerrequisites(acOp, operation);
            return Optional.of(acOp);
        }

        return Optional.empty();
    }

    @Override
    public String validityAcquisition(Acquisition acquisition) {
        List<Parameters> minutesBlock = parametersUseCase
                .findByParent(PARAMETER_ACQUISITION_TIME_VALIDITY);
        return new CoreFunctionDate().
                compareDifferenceTime(acquisition.getCreatedDate(), minutesBlock.get(0).getCode(), true, false);
    }

    @Override
    public List<Acquisition> findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber) {
        List<Acquisition> rawResult = acquisitionRepository.findByDocumentTypeAndDocumentNumber(documentType,
                documentNumber);
        return rawResult.stream()
                .filter(acq -> (acq.getStateAcquisition() != null
                        && (acq.getStateAcquisition().getCode().equals(Numbers.ONE.getNumber()))))
                .collect(Collectors.toList());
    }

    @Override
    public List<Acquisition> getAllByOpAcqIdDocTypeAndDocNum(UUID acqId, String docType, String docNumber,
                                                             String operation) {
        List<Acquisition> acquisitions = new ArrayList<>();
        if (acqId != null) {
            Acquisition acquisition = validateInfoSearchAndGet(acqId.toString(), docType, docNumber, operation)
                    .orElseThrow(() -> new ValidationException(
                            new MapErrorAcquisition(ERROR_CODE_WITHOUT_LINK, ErrorField.builder().build()).map));
            acquisitions.add(acquisition);
        } else {
            Optional<DocumentType> opDocumentType = documentTypeUseCase.findByCode(docType);
            DocumentType documentType = opDocumentType.orElseThrow(() -> new ValidationException(
                    new MapErrorAcquisition(ERROR_CODE_TYPE_DOCUMENT, ErrorField.builder().build()).map));
            acquisitions = findByDocumentTypeAndDocumentNumber(documentType, docNumber);
        }
        if (acquisitions == null || acquisitions.isEmpty()) {
            adapter.error(ERROR + ERROR_CODE_WITHOUT_LINK + MIDDLE_SCREEN + ErrorField.builder().build());
            throw new ValidationException(
                    new MapErrorAcquisition(ERROR_CODE_WITHOUT_LINK, ErrorField.builder().build()).map);
        }
        return acquisitions;
    }

    public void acquisitionValidations(Acquisition acOp, String operation) {
        if (acOp == null) {
            throw new ValidationException(
                    new MapErrorAcquisition(ERROR_CODE_WITHOUT_LINK, ErrorField.builder().build()).map);
        }
        if (acOp.getStateAcquisition().getCode().equals(Numbers.FIVE.getNumber()) &&
                !operation.equals(OPE_MARKCUSTOMER_VALUE)) {
            throw new ValidationException(new MapErrorAcquisition(ERROR_CODE_MARK, ErrorField.builder().build()).map);
        }
        String stateAcquisition = acOp.getStateAcquisition().getCode();
        if (isValidStateAcquisition(stateAcquisition) && validityAcquisition(acOp) != null) {
            acquisitionOperationUseCase.updateAcquisition(acOp, FOUR.getNumber());
            throw new ValidationException(
                    new MapErrorAcquisition(ERROR_CODE_WITHOUT_LINK, ErrorField.builder().build()).map);
        }
    }

    private boolean isValidStateAcquisition(String stateAcquisition) {
        return !stateAcquisition.equals(Numbers.THREE.getNumber())
                && !stateAcquisition.equals(Numbers.FIVE.getNumber())
                && !stateAcquisition.equals(Numbers.SIX.getNumber())
                && !stateAcquisition.equals(Numbers.SEVEN.getNumber());
    }

    private void validatePrerrequisites(Acquisition acOp, String operation) {
        if (!prerequisitesStepUseCase.findByTypeAcquisitionAndCurrentStep(acOp.getTypeAcquisition(), operation)
                .isEmpty()) {
            prerequisitesStepUseCase.validatePrerrequisites(acOp, operation);
        }
    }
}
