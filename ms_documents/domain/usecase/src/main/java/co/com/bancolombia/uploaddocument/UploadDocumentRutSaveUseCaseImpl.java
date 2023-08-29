package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.KofaxRutInformation;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityRuleUtilUseCase;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EXPIRED_RUT_YEAR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.KOFAX_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.KOFAX_CE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.KOFAX_NIT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.KOFAX_PA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_FIRST_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_FIRST_SURNAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_RUT_RETRIES_FLAG_EMISSION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_RUT_RETRIES_FLAG_PERSONAL_INFO;

@RequiredArgsConstructor
public class UploadDocumentRutSaveUseCaseImpl implements UploadDocumentRutSaveUseCase {

    private final NaturalPersonUseCase naturalPersonUseCase;
    private final ValidateIdentityRuleUtilUseCase validateIdentityRuleUtilUseCase;
    private final ParametersUseCase parametersUseCase;
    private final UploadDocumentRutModifyCiiuUseCase uploadDocumentRutModifyCiiuUseCase;
    private final CoreFunctionDate coreFunctionDate;
    private final UploadDocumentValidateErrors uploadDocumentValidateErrors;

    @Override
    public boolean validateAndSaveInformation(KofaxRutInformation kofaxRutInformation, Acquisition acquisition,
            String userTransaction, SqsMessageParamAllObject sqsMessageParamAllObject) {

        boolean flagException = validatePersonalInfo(kofaxRutInformation, acquisition, sqsMessageParamAllObject);
        if (flagException) {
            uploadDocumentValidateErrors.validateExceptionRutRetries(
                    acquisition, TYPE_RUT, VALIDATE_RUT_RETRIES_FLAG_PERSONAL_INFO, sqsMessageParamAllObject);
        }

        flagException = validateEmissionRutRate(kofaxRutInformation);
        if (flagException) {
            uploadDocumentValidateErrors.validateExceptionRutRetries(
                    acquisition, TYPE_RUT, VALIDATE_RUT_RETRIES_FLAG_EMISSION, sqsMessageParamAllObject);
        }

        return uploadDocumentRutModifyCiiuUseCase.modifyCiiu(
                kofaxRutInformation.getMainActivity(), acquisition, userTransaction, sqsMessageParamAllObject);
    }

    private boolean validatePersonalInfo(KofaxRutInformation kofaxRutInformation,
            Acquisition acquisition, SqsMessageParamAllObject sqsMessageParamAllObject) {

        String identificationType = transformKofaxIdentTypeField(kofaxRutInformation.getIdentificationType());
        if (identificationType.isEmpty()) {
            uploadDocumentValidateErrors.validateExceptionRetries(acquisition, TYPE_RUT, sqsMessageParamAllObject);
        }

        boolean flagException = false;
        if (!identificationType.equals(acquisition.getDocumentType().getCode())) {
            flagException = true;
        }
        if (!kofaxRutInformation.getDocumentNumber().equals(acquisition.getDocumentNumber())) {
            flagException = true;
        }

        ValidateIdentityReply reply = naturalPersonUseCase.validateIdentity(acquisition.getId());
        if (reply.getFirstName() != null && reply.getFirstSurname() != null) {
            if (!validateIdentityRuleUtilUseCase.compareString(kofaxRutInformation.getFirstName(),
                    reply.getFirstName(), findConfiguration().get(THRESHOLD_FIRST_NAME))) {
                flagException = true;
            }
            if (!validateIdentityRuleUtilUseCase.compareString(kofaxRutInformation.getFirstSurname(),
                    reply.getFirstSurname(), findConfiguration().get(THRESHOLD_FIRST_SURNAME))) {
                flagException = true;
            }
        } else {
            flagException = false;
        }

        return flagException;
    }

    private boolean validateEmissionRutRate(KofaxRutInformation kofaxRutInformation) {
        boolean flagException = false;
        LocalDateTime emissionYear = coreFunctionDate.fromDateToLocalDateTime(
                coreFunctionDate.getDateFromString(kofaxRutInformation.getEmissionDate(), "dd/MM/yyyy"));
        if(emissionYear.getYear() < EXPIRED_RUT_YEAR) {
            flagException = true;
        } else {
            flagException = false;
        }
        return flagException;
    }

    private String transformKofaxIdentTypeField(String identType) {
        String documentType = EMPTY;
        if (identType.equals(KOFAX_CC)) {
            documentType = Constants.PDF_TYPE_DOCUMENT_CC;
        } else if (identType.equals(KOFAX_PA)) {
            documentType = Constants.PDF_TYPE_DOCUMENT_PA;
        } else if (identType.equals(KOFAX_CE)) {
            documentType = Constants.PDF_TYPE_DOCUMENT_CE;
        } else if (identType.equals(KOFAX_NIT)) {
            documentType = Constants.DOCUMENT_TYPE_NIT;
        }
        return documentType;
    }

    private Map<String, Double> findConfiguration() {
        Map<String, Double> rules = new HashMap<>();
        List<Parameters> listParameterValidateIdentity = parametersUseCase.findByParent(PARENT_UPLOAD_DOCUMENT);

        listParameterValidateIdentity.stream().forEach(a -> {
            if (a.getName().equals(THRESHOLD_FIRST_NAME)) {
                rules.put(THRESHOLD_FIRST_NAME, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(THRESHOLD_FIRST_SURNAME)) {
                rules.put(THRESHOLD_FIRST_SURNAME, Double.valueOf(a.getCode()));
            }
        });

        return rules;
    }
}
