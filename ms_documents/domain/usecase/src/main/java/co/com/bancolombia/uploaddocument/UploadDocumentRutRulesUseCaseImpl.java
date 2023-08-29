package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.KofaxRutInformation;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.ProcessedFields;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.util.constants.Constants;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PROCESS_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY;
import static co.com.bancolombia.util.constants.Constants.CODE_SUCCESS_PROCESS_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.EMISSION_RUT_DATE;
import static co.com.bancolombia.util.constants.Constants.FIRST_NAME;
import static co.com.bancolombia.util.constants.Constants.FIRST_SURNAME;
import static co.com.bancolombia.util.constants.Constants.IDENTIFICATION_TYPE;
import static co.com.bancolombia.util.constants.Constants.MAIN_ACTIVITY;
import static co.com.bancolombia.util.constants.Constants.RUT_RETRIES;
import static co.com.bancolombia.util.constants.Constants.TAX_IDENTIFICATION_NUMBER;
import static co.com.bancolombia.util.constants.Constants.TYPE_RUT;

@RequiredArgsConstructor
public class UploadDocumentRutRulesUseCaseImpl implements UploadDocumentRutRulesUseCase {

    private final ParametersUseCase parametersUseCase;

    @Override
    public ProcessDocumentKofaxTotal validateRutDocument(
            UploadDocumentResponse uploadDocumentResponse, AcquisitionProcessDocument acquisition) {

        Map<String, Double> configuration = findConfiguration();
        Map<String, Double> configurationRetries = findConfigurationRetries();

        if (uploadDocumentResponse.getData().getCodeResponseProcess().equals(CODE_SUCCESS_PROCESS_DOCUMENT)) {
            ProcessDocumentKofaxTotal processDocumentKofaxTotal;
            processDocumentKofaxTotal = validateKofaxRut(
                    uploadDocumentResponse, configuration, acquisition, configurationRetries);
            return processDocumentKofaxTotal;
        } else {
            return validateExceptionRetries(acquisition, configurationRetries.get(RUT_RETRIES));
        }
    }

    public ProcessDocumentKofaxTotal validateKofaxRut(
            UploadDocumentResponse uploadDocumentResponse, Map<String, Double> configuration,
            AcquisitionProcessDocument acquisition, Map<String, Double> configurationRetries) {

        KofaxRutInformation kofaxRutInformation = validateKofaxRutRules(uploadDocumentResponse, configuration);
        boolean flagException;
        if (kofaxRutInformation.getMainActivity() == null) {
            flagException = true;
        } else if (kofaxRutInformation.getIdentificationType() == null) {
            flagException = true;
        } else if (kofaxRutInformation.getDocumentNumber() == null) {
            flagException = true;
        } else if (kofaxRutInformation.getFirstName() == null) {
            flagException = true;
        } else if (kofaxRutInformation.getFirstSurname() == null) {
            flagException = true;
        } else if (kofaxRutInformation.getEmissionDate() == null) {
            flagException = true;
        } else {
            flagException = false;
        }

        if (!flagException) {
            return ProcessDocumentKofaxTotal.builder().kofaxRutInformation(kofaxRutInformation).build();
        } else {
            return validateExceptionRetries(acquisition, configurationRetries.get(RUT_RETRIES));
        }
    }

    private KofaxRutInformation validateKofaxRutRules(
            UploadDocumentResponse uploadDocumentResponse, Map<String, Double> configuration) {

        KofaxRutInformation kofaxRutInformation = KofaxRutInformation.builder().build();
        String fieldValue;
        fieldValue = validateField(uploadDocumentResponse, configuration, MAIN_ACTIVITY, Constants.PARAM_MAIN_ACTIVITY);
        if (fieldValue != null) {
            kofaxRutInformation.setMainActivity(fieldValue);
        }
        fieldValue = validateField(
                uploadDocumentResponse, configuration, IDENTIFICATION_TYPE, Constants.PARAM_IDENTIFICATION_TYPE);
        if (fieldValue != null) {
            kofaxRutInformation.setIdentificationType(fieldValue);
        }
        fieldValue = validateField(
                uploadDocumentResponse, configuration,
                TAX_IDENTIFICATION_NUMBER, Constants.PARAM_TAX_IDENTIFICATION_NUMBER);
        if (fieldValue != null) {
            kofaxRutInformation.setDocumentNumber(fieldValue);
        }
        fieldValue = validateField(uploadDocumentResponse, configuration, FIRST_NAME, Constants.PARAM_FIRST_NAME);
        if (fieldValue != null) {
            kofaxRutInformation.setFirstName(fieldValue);
        }
        fieldValue = validateField(uploadDocumentResponse, configuration, FIRST_SURNAME, Constants.PARAM_FIRST_SURNAME);
        if (fieldValue != null) {
            kofaxRutInformation.setFirstSurname(fieldValue);
        }

        return validateKofaxRutRulesEmissionDate(uploadDocumentResponse, configuration, kofaxRutInformation);
    }

    private KofaxRutInformation validateKofaxRutRulesEmissionDate(UploadDocumentResponse uploadDocumentResponse,
            Map<String, Double> configuration, KofaxRutInformation kofaxRutInformation) {

        String fieldValue;
        fieldValue = validateField(
                uploadDocumentResponse, configuration, EMISSION_RUT_DATE, Constants.PARAM_EMISSION_RUT_DATE);
        if (fieldValue != null) {
            kofaxRutInformation.setEmissionDate(fieldValue);
        }
        return kofaxRutInformation;
    }

    private String validateField(UploadDocumentResponse uploadDocumentResponse,
            Map<String, Double> configuration, String field, String parameter) {

        ProcessedFields fields = uploadDocumentResponse.getData().getProcessedDocument().get(0).getProcessedFields()
                .stream().filter(pf -> pf.getFieldName().equals(field)).findFirst().orElse(null);

        if (fields != null) {
            if (validateThreshold(fields.getConfidencePercentage(), configuration.get(parameter))) {
                return fields.getFieldValue();
            }
        }
        return null;
    }

    private boolean validateThreshold(String percentage, Double threshold) {
        return Double.parseDouble(percentage) >= (threshold);
    }

    private ProcessDocumentKofaxTotal validateExceptionRetries(
            AcquisitionProcessDocument acquisition, Double parameterRetries) {

        ProcessDocumentKofaxError processDocumentKofaxError;
        if (acquisition.getUploadRutRetries() < parameterRetries) {
            processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                    .code(ERROR_CODE_UPLOAD_DOCUMENT_RETRY).detail(TYPE_RUT)
                    .build();
        } else {
            processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                    .code(ERROR_CODE_PROCESS_DOCUMENT).detail(TYPE_RUT)
                    .build();
        }

        return ProcessDocumentKofaxTotal.builder().processDocumentKofaxError(processDocumentKofaxError).build();
    }

    private Map<String, Double> findConfiguration() {
        Map<String, Double> parameters = new HashMap<>();
        List<Parameters> listParameterUploadDocument = parametersUseCase.findByParent(PARENT_UPLOAD_DOCUMENT);

        listParameterUploadDocument.forEach(a -> {
            if (a.getName().equals(Constants.PARAM_TAX_IDENTIFICATION_NUMBER)) {
                parameters.put(Constants.PARAM_TAX_IDENTIFICATION_NUMBER, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_CORPORATE_NAME)) {
                parameters.put(Constants.PARAM_CORPORATE_NAME, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_MAIN_ACTIVITY)) {
                parameters.put(Constants.PARAM_MAIN_ACTIVITY, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_SECONDARY_ACTIVITY)) {
                parameters.put(Constants.PARAM_SECONDARY_ACTIVITY, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_EMISSION_RUT_DATE)) {
                parameters.put(Constants.PARAM_EMISSION_RUT_DATE, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_ASSESSEE_TYPE)) {
                parameters.put(Constants.PARAM_ASSESSEE_TYPE, Double.valueOf(a.getCode()));
            }

        });
        transConfigurationPersonalInfo(parameters, listParameterUploadDocument);
        return parameters;
    }

    private void transConfigurationPersonalInfo(
            Map<String, Double> parameters, List<Parameters> listParameterUploadDocument) {

        listParameterUploadDocument.forEach(a -> {
            if (a.getName().equals(Constants.PARAM_FIRST_NAME)) {
                parameters.put(Constants.PARAM_FIRST_NAME, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_SECOND_NAME)) {
                parameters.put(Constants.PARAM_SECOND_NAME, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_FIRST_SURNAME)) {
                parameters.put(Constants.PARAM_FIRST_SURNAME, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_SECOND_SURNAME)) {
                parameters.put(Constants.PARAM_SECOND_SURNAME, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(Constants.PARAM_IDENTIFICATION_TYPE)) {
                parameters.put(Constants.PARAM_IDENTIFICATION_TYPE, Double.valueOf(a.getCode()));
            }
        });
    }

    private Map<String, Double> findConfigurationRetries() {
        Map<String, Double> parameters = new HashMap<>();
        List<Parameters> listParameterUploadDocument = parametersUseCase.findByParent(PARENT_UPLOAD_DOCUMENT);
        listParameterUploadDocument.forEach(a -> {
            if (a.getName().equals(RUT_RETRIES)) {
                parameters.put(RUT_RETRIES, Double.valueOf(a.getCode()));
            }
        });
        return parameters;
    }
}
