package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
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
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PROCESS_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY;
import static co.com.bancolombia.util.constants.Constants.EMISSION_RUT_DATE;
import static co.com.bancolombia.util.constants.Constants.FIRST_NAME;
import static co.com.bancolombia.util.constants.Constants.FIRST_SURNAME;
import static co.com.bancolombia.util.constants.Constants.IDENTIFICATION_TYPE;
import static co.com.bancolombia.util.constants.Constants.MAIN_ACTIVITY;
import static co.com.bancolombia.util.constants.Constants.PARAM_ASSESSEE_TYPE;
import static co.com.bancolombia.util.constants.Constants.PARAM_CORPORATE_NAME;
import static co.com.bancolombia.util.constants.Constants.PARAM_EMISSION_RUT_DATE;
import static co.com.bancolombia.util.constants.Constants.PARAM_FIRST_NAME;
import static co.com.bancolombia.util.constants.Constants.PARAM_FIRST_SURNAME;
import static co.com.bancolombia.util.constants.Constants.PARAM_IDENTIFICATION_TYPE;
import static co.com.bancolombia.util.constants.Constants.PARAM_MAIN_ACTIVITY;
import static co.com.bancolombia.util.constants.Constants.PARAM_SECONDARY_ACTIVITY;
import static co.com.bancolombia.util.constants.Constants.PARAM_SECOND_NAME;
import static co.com.bancolombia.util.constants.Constants.PARAM_SECOND_SURNAME;
import static co.com.bancolombia.util.constants.Constants.PARAM_TAX_IDENTIFICATION_NUMBER;
import static co.com.bancolombia.util.constants.Constants.RUT_RETRIES;
import static co.com.bancolombia.util.constants.Constants.TAX_IDENTIFICATION_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class UploadDocumentRutRulesUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentRutRulesUseCaseImpl uploadDocumentRutRulesUseCase;

    @Mock
    private ParametersUseCase parametersUseCase;

    private List<Parameters> parameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        parameters = Arrays.asList(
                Parameters.builder().name(RUT_RETRIES).code("3").build(),
                Parameters.builder().name(PARAM_TAX_IDENTIFICATION_NUMBER).code("0.90").build(),
                Parameters.builder().name(PARAM_CORPORATE_NAME).code("0.90").build(),
                Parameters.builder().name(PARAM_MAIN_ACTIVITY).code("0.90").build(),
                Parameters.builder().name(PARAM_SECONDARY_ACTIVITY).code("0.90").build(),
                Parameters.builder().name(PARAM_EMISSION_RUT_DATE).code("0.90").build(),
                Parameters.builder().name(PARAM_ASSESSEE_TYPE).code("0.90").build(),
                Parameters.builder().name(PARAM_FIRST_NAME).code("0.90").build(),
                Parameters.builder().name(PARAM_SECOND_NAME).code("0.90").build(),
                Parameters.builder().name(PARAM_FIRST_SURNAME).code("0.90").build(),
                Parameters.builder().name(PARAM_SECOND_SURNAME).code("0.90").build(),
                Parameters.builder().name(PARAM_IDENTIFICATION_TYPE).code("0.90").build());
    }

    @Test
    public void validateRutDocumentErrorCodeTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().uploadRutRetries(3).build();
        UploadDocument uploadDocument = UploadDocument.builder().codeResponseProcess("01").build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertEquals(ERROR_CODE_PROCESS_DOCUMENT, response.getProcessDocumentKofaxError().getCode());
    }

    @Test
    public void validateRutDocumentDataErrorTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().uploadRutRetries(3).build();

        List<ProcessedFields> processedFields = Arrays.asList(
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(MAIN_ACTIVITY).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(IDENTIFICATION_TYPE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(TAX_IDENTIFICATION_NUMBER).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_NAME).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(EMISSION_RUT_DATE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.88").fieldName(FIRST_SURNAME).build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFields).build());

        UploadDocument uploadDocument = UploadDocument.builder()
                .codeResponseProcess("00").processedDocument(processedDocuments).build();

        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertEquals(ERROR_CODE_PROCESS_DOCUMENT, response.getProcessDocumentKofaxError().getCode());
    }

    @Test
    public void validateRutDocumentNoMainActivityTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().uploadRutRetries(2).build();

        List<ProcessedFields> processedFields = Arrays.asList(
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(IDENTIFICATION_TYPE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(TAX_IDENTIFICATION_NUMBER).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_NAME).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(EMISSION_RUT_DATE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_SURNAME).build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFields).build());

        UploadDocument uploadDocument = UploadDocument.builder()
                .codeResponseProcess("00").processedDocument(processedDocuments).build();

        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertEquals(ERROR_CODE_UPLOAD_DOCUMENT_RETRY, response.getProcessDocumentKofaxError().getCode());
    }

    @Test
    public void validateRutDocumentNoIdentificationTypeTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().uploadRutRetries(2).build();

        List<ProcessedFields> processedFields = Arrays.asList(
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(MAIN_ACTIVITY).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(TAX_IDENTIFICATION_NUMBER).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_NAME).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(EMISSION_RUT_DATE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_SURNAME).build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFields).build());

        UploadDocument uploadDocument = UploadDocument.builder()
                .codeResponseProcess("00").processedDocument(processedDocuments).build();

        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertEquals(ERROR_CODE_UPLOAD_DOCUMENT_RETRY, response.getProcessDocumentKofaxError().getCode());
    }

    @Test
    public void validateRutDocumentNoTaxIdentificationNumberTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().uploadRutRetries(2).build();

        List<ProcessedFields> processedFields = Arrays.asList(
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(MAIN_ACTIVITY).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(IDENTIFICATION_TYPE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_NAME).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(EMISSION_RUT_DATE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_SURNAME).build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFields).build());

        UploadDocument uploadDocument = UploadDocument.builder()
                .codeResponseProcess("00").processedDocument(processedDocuments).build();

        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertEquals(ERROR_CODE_UPLOAD_DOCUMENT_RETRY, response.getProcessDocumentKofaxError().getCode());
    }

    @Test
    public void validateRutDocumentNoFirstNameTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().uploadRutRetries(2).build();

        List<ProcessedFields> processedFields = Arrays.asList(
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(MAIN_ACTIVITY).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(IDENTIFICATION_TYPE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(TAX_IDENTIFICATION_NUMBER).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(EMISSION_RUT_DATE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_SURNAME).build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFields).build());

        UploadDocument uploadDocument = UploadDocument.builder()
                .codeResponseProcess("00").processedDocument(processedDocuments).build();

        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertEquals(ERROR_CODE_UPLOAD_DOCUMENT_RETRY, response.getProcessDocumentKofaxError().getCode());
    }

    @Test
    public void validateRutDocumentNoFirstSurnameTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().uploadRutRetries(2).build();

        List<ProcessedFields> processedFields = Arrays.asList(
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(MAIN_ACTIVITY).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(IDENTIFICATION_TYPE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(TAX_IDENTIFICATION_NUMBER).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_NAME).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(EMISSION_RUT_DATE).build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFields).build());

        UploadDocument uploadDocument = UploadDocument.builder()
                .codeResponseProcess("00").processedDocument(processedDocuments).build();

        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertEquals(ERROR_CODE_UPLOAD_DOCUMENT_RETRY, response.getProcessDocumentKofaxError().getCode());
    }

    @Test
    public void validateRutDocumentNoEmissionRutDateTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder().uploadRutRetries(2).build();

        List<ProcessedFields> processedFields = Arrays.asList(
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(MAIN_ACTIVITY).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(IDENTIFICATION_TYPE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(TAX_IDENTIFICATION_NUMBER).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_NAME).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_SURNAME).build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFields).build());

        UploadDocument uploadDocument = UploadDocument.builder()
                .codeResponseProcess("00").processedDocument(processedDocuments).build();

        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertEquals(ERROR_CODE_UPLOAD_DOCUMENT_RETRY, response.getProcessDocumentKofaxError().getCode());
    }

    @Test
    public void validateRutDocumentSuccessTest() {
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());

        AcquisitionProcessDocument acquisition = AcquisitionProcessDocument.builder()
                .uploadRutRetries(3)
                .build();

        List<ProcessedFields> processedFields = Arrays.asList(
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(MAIN_ACTIVITY).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(IDENTIFICATION_TYPE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(TAX_IDENTIFICATION_NUMBER).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_NAME).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(EMISSION_RUT_DATE).build(),
                ProcessedFields.builder().fieldValue("").confidencePercentage("0.91").fieldName(FIRST_SURNAME).build());

        List<ProcessedDocument> processedDocuments = Arrays.asList(
                ProcessedDocument.builder().processedFields(processedFields).build());

        UploadDocument uploadDocument = UploadDocument.builder()
                .codeResponseProcess("00").processedDocument(processedDocuments).build();

        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder().data(uploadDocument).build();

        ProcessDocumentKofaxTotal response = uploadDocumentRutRulesUseCase.validateRutDocument(
                uploadDocumentResponse, acquisition);

        assertNotNull(response.getKofaxRutInformation());
    }
}
