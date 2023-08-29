package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.KofaxInformation;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.ProcessedFields;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.util.constants.Constants;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TWO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ZERO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PROCESS_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY;

@RequiredArgsConstructor
public class UploadDocumentCcRulesUseCaseImpl implements UploadDocumentCcRulesUseCase {

    private final ParametersUseCase parametersUseCase;
    private final CoreFunctionDate coreFunctionDate;

    @Override
    public ProcessDocumentKofaxTotal validateCcDocument(
            UploadDocumentResponse uploadDocumentResponse, AcquisitionProcessDocument acquisition) {

        Map<String, Double> configuration = findConfiguration();
        Map<String, Double> configurationRetries = findConfigurationRetries();

        if(uploadDocumentResponse.getData().getCodeResponseProcess().equals(Constants.CODE_SUCCESS_PROCESS_DOCUMENT)) {
            Optional<ProcessedFields> processedFieldDiffDocNr =  uploadDocumentResponse.getData()
                    .getProcessedDocument().get(0).getProcessedFields().stream()
                    .filter(pf -> pf.getFieldName().equals(Constants.DIFFERENCE_DOC_NR)).findFirst();

            if(processedFieldDiffDocNr.isPresent()) {
                BigDecimal valueCoincidenceOCRDocNrBCR = new BigDecimal(processedFieldDiffDocNr.get().getFieldValue());
                ProcessDocumentKofaxTotal processDocumentKofaxTotal;
                if((valueCoincidenceOCRDocNrBCR).compareTo(
                        BigDecimal.valueOf(configuration.get(Constants.COINCIDENCE_OCR_BCR_DOC_NR))) >= 0) {
                    processDocumentKofaxTotal = rulesBCR(
                            uploadDocumentResponse, acquisition, configuration, configurationRetries);
                } else {
                    processDocumentKofaxTotal = rulesOCR(
                            uploadDocumentResponse, acquisition, configuration, configurationRetries);
                }
                return processDocumentKofaxTotal;

            } else {
                validateExceptionRetries(acquisition, configurationRetries.get(Constants.IDENTITY_RETRIES));
            }
        } else {
            return validateExceptionRetries(acquisition, configurationRetries.get(Constants.IDENTITY_RETRIES));
        }
        return ProcessDocumentKofaxTotal.builder().build();
    }

    public Map<String, Double> findConfiguration() {
        Map<String, Double> parameters = new HashMap<>();
        List<Parameters> listParameterUploadDocument = parametersUseCase.findByParent(PARENT_UPLOAD_DOCUMENT);

        listParameterUploadDocument.forEach(a -> {
            if(a.getName().equals(Constants.DOCUMENT_NUMBER)) {
                parameters.put(Constants.DOCUMENT_NUMBER, Double.valueOf(a.getCode()));
            }
            if(a.getName().equals(Constants.FIRST_NAMES)) {
                parameters.put(Constants.FIRST_NAMES, Double.valueOf(a.getCode()));
            }
            if(a.getName().equals(Constants.LAST_NAMES)) {
                parameters.put(Constants.LAST_NAMES, Double.valueOf(a.getCode()));
            }
            if(a.getName().equals(Constants.BIRTH_DATE)) {
                parameters.put(Constants.BIRTH_DATE, Double.valueOf(a.getCode()));
            }
            if(a.getName().equals(Constants.EMISSION_DATE)) {
                parameters.put(Constants.EMISSION_DATE, Double.valueOf(a.getCode()));
            }
            if(a.getName().equals(Constants.COINCIDENCE_OCR_BCR_DOC_NR)) {
                parameters.put(Constants.COINCIDENCE_OCR_BCR_DOC_NR, Double.valueOf(a.getCode()));
            }
            if(a.getName().equals(Constants.GENDER)) {
                parameters.put(Constants.GENDER, Double.valueOf(a.getCode()));
            }

        });
        return parameters;
    }

    public Map<String, Double> findConfigurationRetries() {
        Map<String, Double> parameters = new HashMap<>();
        List<Parameters> listParameterUploadDocument = parametersUseCase.findByParent(PARENT_UPLOAD_DOCUMENT);
        listParameterUploadDocument.forEach(a -> {
            if (a.getName().equals(Constants.IDENTITY_RETRIES)) {
                parameters.put(Constants.IDENTITY_RETRIES, Double.valueOf(a.getCode()));
            }
        });
        return parameters;
    }

    public ProcessDocumentKofaxTotal rulesBCR(
            UploadDocumentResponse uploadDocumentResponse, AcquisitionProcessDocument acquisition,
            Map<String, Double> configuration, Map<String, Double> configurationRetries) {

        Optional<ProcessedFields> processedFieldDocNrBCR = uploadDocumentResponse.getData()
                .getProcessedDocument().get(0).getProcessedFields().stream()
                .filter(pf -> pf.getFieldName().equals(Constants.BCR_DOC_NR)).findFirst();

        if (processedFieldDocNrBCR.isPresent()) {
            String bcrDocIdValue = processedFieldDocNrBCR.get().getFieldValue();
            if (bcrDocIdValue.equals(acquisition.getDocumentNumber())) {
                ProcessDocumentKofaxTotal processDocumentKofaxTotal;
                KofaxInformation kofaxInformationBCR = validateKofaxBCR(uploadDocumentResponse, bcrDocIdValue);
                KofaxInformation kofaxInformationBCRTotal = validateExpeditionDateFromOCR(
                        uploadDocumentResponse, configuration, kofaxInformationBCR);
                if (kofaxInformationBCRTotal == null) {
                    ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                            .code(ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL)
                            .detail(Constants.EXPEDITION_DATE_FIELD).build();
                    processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder()
                            .kofaxInformation(kofaxInformationBCR)
                            .processDocumentKofaxError(processDocumentKofaxError).build();
                } else {
                    processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder().
                            kofaxInformation(kofaxInformationBCRTotal).build();
                }
                return processDocumentKofaxTotal;
            } else {
                return validateExceptionRetries(acquisition, configurationRetries.get(Constants.IDENTITY_RETRIES));
            }
        } else {
            return validateExceptionRetries(acquisition, configurationRetries.get(Constants.IDENTITY_RETRIES));
        }
    }

    public KofaxInformation validateKofaxBCR(UploadDocumentResponse uploadDocumentResponse, String bcrDocIdValue) {
        KofaxInformation kofaxInformationBCR = KofaxInformation.builder().build();
        uploadDocumentResponse.getData().getProcessedDocument().get(0).getProcessedFields()
                .forEach(a -> {
                    if (a.getFieldName().equals(Constants.BCR_FIRST_NAME)) {
                        String bcrName = a.getFieldValue();
                        Map<String, String> resultNames = separateNames(bcrName);
                        kofaxInformationBCR.setFirstName(resultNames.get(Constants.FIRST));
                        kofaxInformationBCR.setSecondName(resultNames.get(Constants.SECOND));
                    }
                    if (a.getFieldName().equals(Constants.BCR_LAST_NAME)) {
                        String bcrLastName = a.getFieldValue();
                        Map<String, String> resultNames = separateNames(bcrLastName);
                        kofaxInformationBCR.setFirstSurname(resultNames.get(Constants.FIRST));
                        kofaxInformationBCR.setSecondSurname(resultNames.get(Constants.SECOND));
                    }
                    if (a.getFieldName().equals(Constants.BCR_GENDER)) {
                        String bcrGender = a.getFieldValue();
                        kofaxInformationBCR.setGender(bcrGender);
                    }
                    if (a.getFieldName().equals(Constants.BCR_BIRTH_DATE)) {
                        String bcrBirthDate = a.getFieldValue();
                        kofaxInformationBCR.setBirthDate(
                                coreFunctionDate.getDateFromString(bcrBirthDate, Constants.DATE_FORMAT));
                    }
                });
        kofaxInformationBCR.setDocumentNumber(bcrDocIdValue);
        return kofaxInformationBCR;
    }

    public KofaxInformation validateExpeditionDateFromOCR(UploadDocumentResponse uploadDocumentResponse,
            Map<String, Double> configuration, KofaxInformation kofaxInformationBCR) {

        Optional<ProcessedFields> fieldDateExp = uploadDocumentResponse.getData().getProcessedDocument().get(0)
                .getProcessedFields().stream().filter(
                        pf -> pf.getFieldName().equals(Constants.OCR_EMISSION_DATE)).findFirst();

        if (fieldDateExp.isPresent()) {
            if (validateThreshold(
                    fieldDateExp.get().getConfidencePercentage(), configuration.get(Constants.EMISSION_DATE))) {
                kofaxInformationBCR.setExpeditionDate(coreFunctionDate.getDateFromString(
                        fieldDateExp.get().getFieldValue(), Constants.DATE_FORMAT));
                return kofaxInformationBCR;
            }
        }
        return null;
    }

    public ProcessDocumentKofaxTotal rulesOCR(
            UploadDocumentResponse uploadDocumentResponse, AcquisitionProcessDocument acquisition,
            Map<String, Double> configuration, Map<String, Double> configurationRetries) {

        Optional<ProcessedFields> processedFieldOcrDocNr = uploadDocumentResponse.getData()
                .getProcessedDocument().get(0).getProcessedFields().stream()
                .filter(processedFields1 -> processedFields1.getFieldName().equals(Constants.OCR_DOC_NR)).findFirst();

        if (processedFieldOcrDocNr.isPresent()) {
            BigDecimal confidenceOCRDocNr = new BigDecimal(
                    processedFieldOcrDocNr.get().getConfidencePercentage());
            if ((confidenceOCRDocNr).compareTo(BigDecimal.valueOf(configuration.get(Constants.DOCUMENT_NUMBER))) >= 0) {
                String ocrDocIdValue = processedFieldOcrDocNr.get().getFieldValue();
                if (ocrDocIdValue.equals(acquisition.getDocumentNumber())) {
                    return validateKofaxOCR(uploadDocumentResponse, ocrDocIdValue, configuration);
                }
            }
        }
        return validateExceptionRetries(acquisition, configurationRetries.get(Constants.IDENTITY_RETRIES));
    }

    public ProcessDocumentKofaxTotal validateKofaxOCR(
            UploadDocumentResponse uploadDocumentResponse, String ocrDocIdValue, Map<String, Double> configuration) {

        KofaxInformation kofaxInformationProcessed = validateKofaxOCRRules(uploadDocumentResponse, configuration);
        boolean flagException;

        if (kofaxInformationProcessed.getFirstName() == null) {
            flagException = true;
        } else if (kofaxInformationProcessed.getFirstSurname() == null) {
            flagException = true;
        } else if (kofaxInformationProcessed.getSecondSurname() == null) {
            flagException = true;
        } else if (kofaxInformationProcessed.getGender() == null) {
            flagException = true;
        } else if (kofaxInformationProcessed.getBirthDate() == null) {
            flagException = true;
        } else if (kofaxInformationProcessed.getExpeditionDate() == null) {
            flagException = true;
        } else {
            flagException = false;
        }

        if (!flagException) {
            kofaxInformationProcessed.setDocumentNumber(ocrDocIdValue);
            return ProcessDocumentKofaxTotal.builder().kofaxInformation(kofaxInformationProcessed).build();
        } else {
            ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                    .code(ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL).detail(Constants.TYPE_ID_CC).build();
            return ProcessDocumentKofaxTotal.builder().processDocumentKofaxError(processDocumentKofaxError).build();
        }
    }

    public KofaxInformation validateKofaxOCRRules(
            UploadDocumentResponse uploadDocumentResponse, Map<String, Double> configuration) {

        KofaxInformation kofaxInformationOCR = KofaxInformation.builder().build();
        String fieldValue;

        fieldValue = validateFieldOCR(
                uploadDocumentResponse, configuration, Constants.OCR_FIRST_NAMES, Constants.FIRST_NAMES);
        if (fieldValue != null) {
            Map<String, String> resultNames = separateNames(fieldValue);
            kofaxInformationOCR.setFirstName(resultNames.get(Constants.FIRST));
            kofaxInformationOCR.setSecondName(resultNames.get(Constants.SECOND));
        }
        fieldValue = validateFieldOCR(
                uploadDocumentResponse, configuration, Constants.OCR_LAST_NAMES, Constants.LAST_NAMES);
        if (fieldValue != null) {
            Map<String, String> resultNames = separateNames(fieldValue);
            kofaxInformationOCR.setFirstSurname(resultNames.get(Constants.FIRST));
            kofaxInformationOCR.setSecondSurname(resultNames.get(Constants.SECOND));
        }
        fieldValue = validateFieldOCR(uploadDocumentResponse, configuration, Constants.OCR_GENDER, Constants.GENDER);
        if (fieldValue != null) {
            kofaxInformationOCR.setGender(fieldValue);
        }
        validateKofaxOCRRulesDates(kofaxInformationOCR, uploadDocumentResponse, configuration);

        return kofaxInformationOCR;
    }

    private void validateKofaxOCRRulesDates(KofaxInformation kofaxInformationOCR,
            UploadDocumentResponse uploadDocumentResponse, Map<String, Double> configuration) {

        String fieldValue;

        fieldValue = validateFieldOCR(
                uploadDocumentResponse, configuration, Constants.OCR_BIRTH_DATE, Constants.BIRTH_DATE);
        if (fieldValue != null) {
            kofaxInformationOCR.setBirthDate(coreFunctionDate.getDateFromString(fieldValue, Constants.DATE_FORMAT));
        }
        fieldValue = validateFieldOCR(
                uploadDocumentResponse, configuration, Constants.OCR_EMISSION_DATE, Constants.EMISSION_DATE);
        if (fieldValue != null) {
            kofaxInformationOCR.setExpeditionDate(
                    coreFunctionDate.getDateFromString(fieldValue, Constants.DATE_FORMAT));
        }
    }

    public String validateFieldOCR(UploadDocumentResponse uploadDocumentResponse,
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

    public ProcessDocumentKofaxTotal validateExceptionRetries(
            AcquisitionProcessDocument acquisition, Double parameterRetries) {

        ProcessDocumentKofaxError processDocumentKofaxError;
        if (acquisition.getUploadDocumentRetries() < parameterRetries) {
            processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                    .code(ERROR_CODE_UPLOAD_DOCUMENT_RETRY).detail(Constants.TYPE_ID_CC).build();
        } else {
            processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                    .code(ERROR_CODE_PROCESS_DOCUMENT).detail(Constants.TYPE_ID_CC).build();
        }
        return ProcessDocumentKofaxTotal.builder().processDocumentKofaxError(processDocumentKofaxError).build();
    }

    public boolean validateThreshold(String percentage, Double threshold) {
        return Double.parseDouble(percentage) >= (threshold);
    }

    public Map<String, String> separateNames(String names) {
        if (names == null || names.isEmpty()) {
            return saveNames(EMPTY, EMPTY);
        }
        names = names.toUpperCase().trim();
        List<String> unions = new ArrayList(Arrays.asList(Constants.UNION));
        Map<String, String> resultNames;
        String wordFounded = null;
        String[] arrProgress = null;
        boolean flagGeneral = false;
        for (String union : unions) {
            if (names.contains(union)) {
                flagGeneral = true;
                arrProgress = names.split(SPACE);
                wordFounded = union;
                break;
            }
        }
        if (!flagGeneral) {
            arrProgress = names.split(SPACE, TWO);
            if (arrProgress.length > 1) {
                resultNames = saveNames(arrProgress[ZERO], arrProgress[ONE]);
            } else {
                resultNames = saveNames(arrProgress[ZERO], EMPTY);
            }
        } else {
            return valNamesComposes(arrProgress, names, wordFounded);
        }
        return resultNames;
    }

    private Map<String, String> valNamesComposes(String[] arrProgress, String names, String wordFounded) {
        boolean flag;
        String[] arrayMiddle;
        String initialWord;
        if (arrProgress.length > 1) {
            initialWord = arrProgress[ZERO] + SPACE + arrProgress[ONE];
        } else {
            initialWord = arrProgress[ZERO];
        }
        List<String> complement2 = new ArrayList(Arrays.asList(Constants.COMPLEMENT_TWO));
        flag = complement2.stream().anyMatch(initialWord::equals);
        if (flag) {
            String value;
            if (arrProgress.length > Constants.VAL_NAME_COMPOSE_TWO) {
                value = arrProgress[ZERO] + SPACE + arrProgress[ONE] + SPACE + arrProgress[TWO];
            } else {
                value = arrProgress[ZERO] + SPACE + arrProgress[ONE];
            }
            arrayMiddle = names.split(value + SPACE);
            return saveNames(value, valArray(arrayMiddle));
        }
        return valNamesComposesComplementOne(arrProgress, names, wordFounded);
    }

    private Map<String, String> valNamesComposesComplementOne(String[] arrProgress, String names, String wordFounded) {
        boolean flag;
        String[] arrayMiddle;

        List<String> complement1 = new ArrayList(Arrays.asList(Constants.COMPLEMENT_ONE));
        flag = complement1.stream().anyMatch(arrProgress[ZERO]::equals);
        if (flag) {
            String valueOne;
            if (arrProgress.length > 1) {
                valueOne = arrProgress[ZERO] + SPACE + arrProgress[ONE];
            } else {
                valueOne = arrProgress[ZERO];
            }
            arrayMiddle = names.split(valueOne + SPACE);
            return saveNames(valueOne, valArray(arrayMiddle));
        } else {
            arrProgress = names.split(wordFounded);
            return saveNames(arrProgress[ZERO], wordFounded + arrProgress[ONE]);
        }
    }

    private Map<String, String> saveNames(String firstName, String secondName) {
        Map<String, String> result = new HashMap<>();
        result.put(Constants.FIRST, firstName);
        result.put(Constants.SECOND, secondName);
        return result;
    }

    private String valArray(String[] arrayMiddle) {
        String middle;
        if (arrayMiddle.length > 1) {
            middle = arrayMiddle[ONE];
        } else {
            middle = EMPTY;
        }
        return middle;
    }
}
