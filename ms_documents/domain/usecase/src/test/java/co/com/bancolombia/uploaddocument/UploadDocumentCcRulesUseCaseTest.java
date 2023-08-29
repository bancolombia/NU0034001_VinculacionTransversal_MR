package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.KofaxInformation;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.ProcessedDocument;
import co.com.bancolombia.model.uploaddocument.ProcessedFields;
import co.com.bancolombia.model.uploaddocument.UploadDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.parameters.ParametersUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_UPLOAD_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.BCR_BIRTH_DATE;
import static co.com.bancolombia.util.constants.Constants.BCR_DOC_NR;
import static co.com.bancolombia.util.constants.Constants.BCR_FIRST_NAME;
import static co.com.bancolombia.util.constants.Constants.BCR_GENDER;
import static co.com.bancolombia.util.constants.Constants.BCR_LAST_NAME;
import static co.com.bancolombia.util.constants.Constants.BIRTH_DATE;
import static co.com.bancolombia.util.constants.Constants.CODE_SUCCESS_PROCESS_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.COINCIDENCE_OCR_BCR_DOC_NR;
import static co.com.bancolombia.util.constants.Constants.DIFFERENCE_DOC_NR;
import static co.com.bancolombia.util.constants.Constants.DOCUMENT_NUMBER;
import static co.com.bancolombia.util.constants.Constants.EMISSION_DATE;
import static co.com.bancolombia.util.constants.Constants.FIRST_NAMES;
import static co.com.bancolombia.util.constants.Constants.GENDER;
import static co.com.bancolombia.util.constants.Constants.IDENTITY_RETRIES;
import static co.com.bancolombia.util.constants.Constants.LAST_NAMES;
import static co.com.bancolombia.util.constants.Constants.OCR_DOC_NR;
import static co.com.bancolombia.util.constants.Constants.OCR_EMISSION_DATE;
import static co.com.bancolombia.util.constants.Constants.OCR_FIRST_NAMES;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class UploadDocumentCcRulesUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentCcRulesUseCaseImpl uploadDocumentCcRulesUseCase;

    @Mock
    private ParametersUseCase parametersUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateCcDocumentBCRTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(DIFFERENCE_DOC_NR).fieldValue("0.95").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments)
                .codeResponseProcess(CODE_SUCCESS_PROCESS_DOCUMENT).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        Map<String, Double> configuration = new HashMap<>();
        configuration.put(COINCIDENCE_OCR_BCR_DOC_NR, 0.90);
        Map<String, Double> configurationRetries = new HashMap<>();
        ProcessDocumentKofaxTotal processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder().build();

        doReturn(configuration).when(uploadDocumentCcRulesUseCase).findConfiguration();
        doReturn(configurationRetries).when(uploadDocumentCcRulesUseCase).findConfigurationRetries();
        doReturn(processDocumentKofaxTotal).when(uploadDocumentCcRulesUseCase).rulesBCR(
                any(UploadDocumentResponse.class), any(AcquisitionProcessDocument.class), any(Map.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateCcDocument(uploadDocumentResponse, acquisition));
    }

    @Test
    public void validateCcDocumentOCRTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(DIFFERENCE_DOC_NR).fieldValue("0.50").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments)
                .codeResponseProcess(CODE_SUCCESS_PROCESS_DOCUMENT).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        Map<String, Double> configuration = new HashMap<>();
        configuration.put(COINCIDENCE_OCR_BCR_DOC_NR, 0.90);
        Map<String, Double> configurationRetries = new HashMap<>();
        ProcessDocumentKofaxTotal processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder().build();

        doReturn(configuration).when(uploadDocumentCcRulesUseCase).findConfiguration();
        doReturn(configurationRetries).when(uploadDocumentCcRulesUseCase).findConfigurationRetries();
        doReturn(processDocumentKofaxTotal).when(uploadDocumentCcRulesUseCase).rulesOCR(
                any(UploadDocumentResponse.class), any(AcquisitionProcessDocument.class), any(Map.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateCcDocument(uploadDocumentResponse, acquisition));
    }

    @Test
    public void validateCcDocumentNotPresentTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName("NO_VALUE").fieldValue("0.50").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments)
                .codeResponseProcess(CODE_SUCCESS_PROCESS_DOCUMENT).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        Map<String, Double> configuration = new HashMap<>();
        configuration.put(COINCIDENCE_OCR_BCR_DOC_NR, 0.90);
        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.00);
        ProcessDocumentKofaxTotal processDocumentKofaxTotalError = ProcessDocumentKofaxTotal.builder().build();

        doReturn(configuration).when(uploadDocumentCcRulesUseCase).findConfiguration();
        doReturn(configurationRetries).when(uploadDocumentCcRulesUseCase).findConfigurationRetries();
        doReturn(processDocumentKofaxTotalError).when(uploadDocumentCcRulesUseCase).validateExceptionRetries(
                any(AcquisitionProcessDocument.class), anyDouble());

        assertNotNull(uploadDocumentCcRulesUseCase.validateCcDocument(uploadDocumentResponse, acquisition));
    }

    @Test
    public void validateCcDocumentNoSuccessTest() {
        UploadDocument uploadDocument = UploadDocument.builder().codeResponseProcess("WRONG").build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        Map<String, Double> configuration = new HashMap<>();
        configuration.put(COINCIDENCE_OCR_BCR_DOC_NR, 0.90);
        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.00);
        ProcessDocumentKofaxTotal processDocumentKofaxTotalError = ProcessDocumentKofaxTotal.builder().build();

        doReturn(configuration).when(uploadDocumentCcRulesUseCase).findConfiguration();
        doReturn(configurationRetries).when(uploadDocumentCcRulesUseCase).findConfigurationRetries();
        doReturn(processDocumentKofaxTotalError).when(uploadDocumentCcRulesUseCase).validateExceptionRetries(
                any(AcquisitionProcessDocument.class), anyDouble());

        assertNotNull(uploadDocumentCcRulesUseCase.validateCcDocument(uploadDocumentResponse, acquisition));
    }

    @Test
    public void findConfigurationTest() {
        doReturn(getConfiguration()).when(parametersUseCase).findByParent(anyString());
        assertNotNull(uploadDocumentCcRulesUseCase.findConfiguration());
    }

    @Test
    public void findConfigurationNoCaseTest() {
        List<Parameters> parameters = Arrays.asList(
                Parameters.builder().name("BLOOD").parent(PARENT_UPLOAD_DOCUMENT).code("0.90").build());

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        assertNotNull(uploadDocumentCcRulesUseCase.findConfiguration());
    }

    @Test
    public void findConfigurationRetriesTest() {
        doReturn(getConfigurationRetries()).when(parametersUseCase).findByParent(anyString());
        assertNotNull(uploadDocumentCcRulesUseCase.findConfigurationRetries());
    }

    @Test
    public void findConfigurationNORetriesTest() {
        List<Parameters> parameters = Arrays.asList(
                Parameters.builder().name("BLOOD").parent(PARENT_UPLOAD_DOCUMENT).code("3").build());

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        assertNotNull(uploadDocumentCcRulesUseCase.findConfigurationRetries());
    }

    @Test
    public void rulesBCRPresentBCRInfoTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(BCR_DOC_NR).fieldValue("12345").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.0);

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        KofaxInformation kofaxInformation = KofaxInformation.builder().build();

        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxBCR(
                any(UploadDocumentResponse.class), anyString());
        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateExpeditionDateFromOCR(
                any(UploadDocumentResponse.class), any(Map.class), any(KofaxInformation.class));

        assertNotNull(uploadDocumentCcRulesUseCase.rulesBCR(
                uploadDocumentResponse, acquisition, configuration, configurationRetries));
    }

    @Test
    public void rulesBCRNoBCRInfoTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName("NO_INFO").fieldValue("67890").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.0);

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        ProcessDocumentKofaxTotal processDocumentKofaxTotalError = ProcessDocumentKofaxTotal.builder().build();

        doReturn(processDocumentKofaxTotalError).when(uploadDocumentCcRulesUseCase).validateExceptionRetries(
                any(AcquisitionProcessDocument.class), anyDouble());

        assertNotNull(uploadDocumentCcRulesUseCase.rulesBCR(
                uploadDocumentResponse, acquisition, configuration, configurationRetries));
    }

    @Test
    public void rulesBCRKofaxNullTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(BCR_DOC_NR).fieldValue("12345").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.0);

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        KofaxInformation kofaxInformation = KofaxInformation.builder().build();

        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxBCR(
                any(UploadDocumentResponse.class), anyString());
        doReturn(null).when(uploadDocumentCcRulesUseCase).validateExpeditionDateFromOCR(
                any(UploadDocumentResponse.class), any(Map.class), any(KofaxInformation.class));

        assertNotNull(uploadDocumentCcRulesUseCase.rulesBCR(
                uploadDocumentResponse, acquisition, configuration, configurationRetries));
    }

    @Test
    public void rulesBCRDiffDocIdTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(BCR_DOC_NR).fieldValue("67890").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.0);

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        ProcessDocumentKofaxTotal processDocumentKofaxTotalError = ProcessDocumentKofaxTotal.builder().build();

        doReturn(processDocumentKofaxTotalError).when(uploadDocumentCcRulesUseCase).validateExceptionRetries(
                any(AcquisitionProcessDocument.class), anyDouble());

        assertNotNull(uploadDocumentCcRulesUseCase.rulesBCR(
                uploadDocumentResponse, acquisition, configuration, configurationRetries));
    }

    @Test
    public void validateKofaxBCR1Test() {
        String bcrDocIdValue = "12345";

        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(BCR_FIRST_NAME).fieldValue("TEST NAME").build(),
                ProcessedFields.builder().fieldName(BCR_LAST_NAME).fieldValue("DE LAST NAME").build(),
                ProcessedFields.builder().fieldName(BCR_GENDER).fieldValue("TEST GENDER").build(),
                ProcessedFields.builder().fieldName(BCR_BIRTH_DATE).fieldValue("01/01/2000").build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());

        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxBCR(uploadDocumentResponse, bcrDocIdValue));
    }

    @Test
    public void validateKofaxBCR2Test() {
        String bcrDocIdValue = "12345";

        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(BCR_FIRST_NAME).fieldValue("TEST DE OTHER").build(),
                ProcessedFields.builder().fieldName(BCR_LAST_NAME).fieldValue("DE LAS TEST NAME").build(),
                ProcessedFields.builder().fieldName(BCR_GENDER).fieldValue("TEST GENDER").build(),
                ProcessedFields.builder().fieldName(BCR_BIRTH_DATE).fieldValue("01/01/2000").build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());

        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxBCR(uploadDocumentResponse, bcrDocIdValue));
    }

    @Test
    public void validateExpeditionDateFromOCRValidTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(OCR_EMISSION_DATE).fieldValue("01/01/2000").confidencePercentage("0.91").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(EMISSION_DATE, 0.90);

        KofaxInformation kofaxInformation = KofaxInformation.builder().build();
        doReturn(configuration).when(uploadDocumentCcRulesUseCase).findConfiguration();

        assertNotNull(uploadDocumentCcRulesUseCase.validateExpeditionDateFromOCR(
                uploadDocumentResponse, configuration, kofaxInformation));
    }

    @Test
    public void validateExpeditionDateFromOCRInvalidTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(OCR_EMISSION_DATE).fieldValue("01/01/2000").confidencePercentage("0.89").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(EMISSION_DATE, 0.90);

        KofaxInformation kofaxInformation = KofaxInformation.builder().build();
        doReturn(configuration).when(uploadDocumentCcRulesUseCase).findConfiguration();

        assertNull(uploadDocumentCcRulesUseCase.validateExpeditionDateFromOCR(
                uploadDocumentResponse, configuration, kofaxInformation));
    }

    @Test
    public void validateExpeditionDateFromOCRNoEmissionInfoTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName("NO_INFO").fieldValue("01/01/2000").confidencePercentage("0.89").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(EMISSION_DATE, 0.90);

        KofaxInformation kofaxInformation = KofaxInformation.builder().build();
        doReturn(configuration).when(uploadDocumentCcRulesUseCase).findConfiguration();

        assertNull(uploadDocumentCcRulesUseCase.validateExpeditionDateFromOCR(
                uploadDocumentResponse, configuration, kofaxInformation));
    }

    @Test
    public void rulesOCRDocInfoValidTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(OCR_DOC_NR).fieldValue("12345").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.0);

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        ProcessDocumentKofaxTotal processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder().build();
        doReturn(processDocumentKofaxTotal).when(uploadDocumentCcRulesUseCase).validateKofaxOCR(
                any(UploadDocumentResponse.class), anyString(), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.rulesOCR(
                uploadDocumentResponse, acquisition, configuration, configurationRetries));
    }

    @Test
    public void rulesOCRDocInfoInvalidTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(OCR_DOC_NR).fieldValue("12345").confidencePercentage("0.89").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.0);

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("12345").build();
        ProcessDocumentKofaxTotal processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder().build();
        doReturn(processDocumentKofaxTotal).when(uploadDocumentCcRulesUseCase).validateExceptionRetries(
                any(AcquisitionProcessDocument.class), anyDouble());

        assertNotNull(uploadDocumentCcRulesUseCase.rulesOCR(uploadDocumentResponse, acquisition, configuration, configurationRetries));
    }

    @Test
    public void rulesOCRNoDocInfoTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName("NO_INFO").fieldValue("12345").confidencePercentage("0.89").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.0);

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().build();
        ProcessDocumentKofaxTotal processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder().build();
        doReturn(processDocumentKofaxTotal).when(uploadDocumentCcRulesUseCase).validateExceptionRetries(
                any(AcquisitionProcessDocument.class), anyDouble());

        assertNotNull(uploadDocumentCcRulesUseCase.rulesOCR(
                uploadDocumentResponse, acquisition, configuration, configurationRetries));
    }

    @Test
    public void rulesOCRDocDifferentDocumentTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(OCR_DOC_NR).fieldValue("12345").confidencePercentage("0.95").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        Map<String, Double> configurationRetries = new HashMap<>();
        configurationRetries.put(IDENTITY_RETRIES, 3.0);

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().documentNumber("67890").build();
        ProcessDocumentKofaxTotal processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder().build();
        doReturn(processDocumentKofaxTotal).when(uploadDocumentCcRulesUseCase).validateExceptionRetries(
                any(AcquisitionProcessDocument.class), anyDouble());

        assertNotNull(uploadDocumentCcRulesUseCase.rulesOCR(
                uploadDocumentResponse, acquisition, configuration, configurationRetries));
    }

    @Test
    public void validateKofaxOCRNotExceptionTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        String docId = "12345";
        Date date = new Date();

        KofaxInformation kofaxInformation = KofaxInformation.builder().firstName("FN").firstSurname("FS")
                .secondSurname("SS").gender("M").birthDate(date).expeditionDate(date).build();
        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxOCRRules(
                any(UploadDocumentResponse.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCR(uploadDocumentResponse, docId, configuration));
    }

    @Test
    public void validateKofaxOCRExceptionTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        String docId = "12345";

        KofaxInformation kofaxInformation = KofaxInformation.builder().build();
        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxOCRRules(
                any(UploadDocumentResponse.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCR(uploadDocumentResponse, docId, configuration));
    }

    @Test
    public void validateKofaxOCRCaseNullFirstSurnameNoTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        String docId = "12345";
        Date date = new Date();

        KofaxInformation kofaxInformation = KofaxInformation.builder().firstName("FN").firstSurname(null)
                .secondSurname("SS").gender("M").birthDate(date).expeditionDate(date).build();
        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxOCRRules(
                any(UploadDocumentResponse.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCR(uploadDocumentResponse, docId, configuration));
    }

    @Test
    public void validateKofaxOCRNullSecondSurnameTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        String docId = "12345";
        Date date = new Date();

        KofaxInformation kofaxInformation = KofaxInformation.builder().firstName("FN").firstSurname("FS")
                .secondSurname(null).gender("M").birthDate(date).expeditionDate(date).build();
        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxOCRRules(
                any(UploadDocumentResponse.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCR(uploadDocumentResponse, docId, configuration));
    }

    @Test
    public void validateKofaxOCRNullGenderTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        String docId = "12345";
        Date date = new Date();

        KofaxInformation kofaxInformation = KofaxInformation.builder().firstName("FN").firstSurname("FS")
                .secondSurname("SS").gender(null).birthDate(date).expeditionDate(date).build();
        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxOCRRules(
                any(UploadDocumentResponse.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCR(uploadDocumentResponse, docId, configuration));
    }

    @Test
    public void validateKofaxOCRNullBirthDateTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        String docId = "12345";
        Date date = new Date();

        KofaxInformation kofaxInformation = KofaxInformation.builder().firstName("FN").firstSurname("FS")
                .secondSurname("SS").gender("M").birthDate(null).expeditionDate(date).build();
        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxOCRRules(
                any(UploadDocumentResponse.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCR(uploadDocumentResponse, docId, configuration));
    }

    @Test
    public void validateKofaxOCRNullExpeditionDateTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        String docId = "12345";
        Date date = new Date();

        KofaxInformation kofaxInformation = KofaxInformation.builder().firstName("FN").firstSurname("FS")
                .secondSurname("SS").gender("M").birthDate(date).expeditionDate(null).build();
        doReturn(kofaxInformation).when(uploadDocumentCcRulesUseCase).validateKofaxOCRRules(
                any(UploadDocumentResponse.class), any(Map.class));

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCR(uploadDocumentResponse, docId, configuration));
    }

    @Test
    public void validateKofaxOCRRulesNotNullTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        doReturn("").when(uploadDocumentCcRulesUseCase).validateFieldOCR(
                any(UploadDocumentResponse.class), any(Map.class), anyString(), anyString());
        doReturn(new HashMap<>()).when(uploadDocumentCcRulesUseCase).separateNames(anyString());

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCRRules(uploadDocumentResponse, configuration));
    }

    @Test
    public void validateKofaxOCRRulesNullTest() {
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(DOCUMENT_NUMBER, 0.90);

        doReturn(null).when(uploadDocumentCcRulesUseCase).validateFieldOCR(
                any(UploadDocumentResponse.class), any(Map.class), anyString(), anyString());

        assertNotNull(uploadDocumentCcRulesUseCase.validateKofaxOCRRules(uploadDocumentResponse, configuration));
    }

    @Test
    public void validateFieldOCRValidTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(OCR_FIRST_NAMES).fieldValue("JUAN ANDRES").confidencePercentage("0.91").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(FIRST_NAMES, 0.90);

        assertNotNull(uploadDocumentCcRulesUseCase.validateFieldOCR(
                uploadDocumentResponse, configuration, OCR_FIRST_NAMES, FIRST_NAMES));
    }

    @Test
    public void validateFieldOCRInvalidTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName(OCR_FIRST_NAMES).fieldValue("JUAN ANDRES").confidencePercentage("0.89").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(FIRST_NAMES, 0.90);

        assertNull(uploadDocumentCcRulesUseCase.validateFieldOCR(
                uploadDocumentResponse, configuration, OCR_FIRST_NAMES, FIRST_NAMES));
    }

    @Test
    public void validateFieldOCRNullTest() {
        List<ProcessedFields> processedFieldsList = Arrays.asList(
                ProcessedFields.builder().fieldName("NOT_FIELD").fieldValue("JUAN ANDRES").confidencePercentage("0.89").build());
        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFieldsList).build());
        UploadDocument uploadDocument = UploadDocument.builder().processedDocument(processedDocuments).build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        Map<String, Double> configuration = new HashMap<>();
        configuration.put(FIRST_NAMES, 0.90);

        assertNull(uploadDocumentCcRulesUseCase.validateFieldOCR(
                uploadDocumentResponse, configuration, OCR_FIRST_NAMES, FIRST_NAMES));
    }

    @Test
    public void validateExceptionRetriesSuccessTest() {
        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder()
                .id(UUID.randomUUID()).uploadDocumentRetries(1).build();

        Double parameterRetries = 3.00;
        assertNotNull(uploadDocumentCcRulesUseCase.validateExceptionRetries(acquisition, parameterRetries));
    }

    @Test
    public void validateExceptionRetriesErrorTest() {
        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder()
                .id(UUID.randomUUID()).uploadDocumentRetries(4).build();

        Double parameterRetries = 3.00;
        assertNotNull(uploadDocumentCcRulesUseCase.validateExceptionRetries(acquisition, parameterRetries));
    }

    @Test
    public void separateNamesOneTest() {
        assertNotNull(uploadDocumentCcRulesUseCase.separateNames("FIRSTNAME"));
    }

    @Test
    public void separateNamesTwoTest() {
        assertNotNull(uploadDocumentCcRulesUseCase.separateNames("DE LAS "));
    }

    @Test
    public void separateNamesOtherTest() {
        assertNotNull(uploadDocumentCcRulesUseCase.separateNames("DE"));
    }

    @Test
    public void separateNamesNullTest() {
        assertNotNull(uploadDocumentCcRulesUseCase.separateNames(null));
    }

    @Test
    public void separateNamesEmptyTest() {
        assertNotNull(uploadDocumentCcRulesUseCase.separateNames(EMPTY));
    }

    private List<Parameters> getConfiguration() {
        List<Parameters> parameters = Arrays.asList(
                Parameters.builder().name(DOCUMENT_NUMBER).parent(PARENT_UPLOAD_DOCUMENT).code("1234").build(),
                Parameters.builder().name(FIRST_NAMES).parent(PARENT_UPLOAD_DOCUMENT).code("0.90").build(),
                Parameters.builder().name(LAST_NAMES).parent(PARENT_UPLOAD_DOCUMENT).code("0.90").build(),
                Parameters.builder().name(BIRTH_DATE).parent(PARENT_UPLOAD_DOCUMENT).code("0.90").build(),
                Parameters.builder().name(EMISSION_DATE).parent(PARENT_UPLOAD_DOCUMENT).code("0.90").build(),
                Parameters.builder().name(COINCIDENCE_OCR_BCR_DOC_NR).parent(PARENT_UPLOAD_DOCUMENT).code("0.90").build(),
                Parameters.builder().name(GENDER).parent(PARENT_UPLOAD_DOCUMENT).code("0.90").build());

        return parameters;
    }

    private List<Parameters> getConfigurationRetries() {
        List<Parameters> parameters = Arrays.asList(
                Parameters.builder().name(IDENTITY_RETRIES).parent(PARENT_UPLOAD_DOCUMENT).code("3").build());

        return parameters;
    }
}
