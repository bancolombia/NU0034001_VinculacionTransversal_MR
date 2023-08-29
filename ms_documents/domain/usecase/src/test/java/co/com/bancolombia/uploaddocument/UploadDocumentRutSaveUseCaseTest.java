package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.KofaxRutInformation;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityRuleUtilUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_TYPE_NIT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.KOFAX_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.KOFAX_CE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.KOFAX_NIT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.KOFAX_PA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PDF_TYPE_DOCUMENT_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PDF_TYPE_DOCUMENT_CE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PDF_TYPE_DOCUMENT_PA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_FIRST_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_FIRST_SURNAME;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class UploadDocumentRutSaveUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentRutSaveUseCaseImpl uploadDocumentRutSaveUseCase;

    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;

    @Mock
    private ValidateIdentityRuleUtilUseCase validateIdentityRuleUtilUseCase;

    @Mock
    private ParametersUseCase parametersUseCase;

    @Mock
    private UploadDocumentRutModifyCiiuUseCase uploadDocumentRutModifyCiiuUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private UploadDocumentValidateErrors uploadDocumentValidateErrors;

    private List<Parameters> parameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        parameters = Arrays.asList(
                Parameters.builder().name(THRESHOLD_FIRST_NAME).code("90").build(),
                Parameters.builder().name(THRESHOLD_FIRST_SURNAME).code("90").build());
    }

    @Test(expected = ValidationException.class)
    public void validateAndSaveInformationNoValidIdentificationTypeTest() {
        doThrow(ValidationException.class).when(uploadDocumentValidateErrors).validateExceptionRetries(
                any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation("NO_INFO", "123456");
        Acquisition acquisition = Acquisition.builder().build();
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);
    }

    @Test
    public void validateAndSaveInformationCcTest() {
        ValidateIdentityReply identityReply = getIdentityInfo("FIRST NAME", "FIRST SURNAME");

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(true).when(validateIdentityRuleUtilUseCase).compareString(anyString(), anyString(), anyDouble());
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(LocalDateTime.now()).when(coreFunctionDate).fromDateToLocalDateTime(any(Date.class));
        doReturn(true).when(uploadDocumentRutModifyCiiuUseCase).modifyCiiu(
                anyString(), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_CC, "123456");
        Acquisition acquisition = getAcquisition(PDF_TYPE_DOCUMENT_CC, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        boolean save = uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);

        assertTrue(save);
    }

    @Test
    public void validateAndSaveInformationPassportTest() {
        ValidateIdentityReply identityReply = getIdentityInfo("FIRST NAME", "FIRST SURNAME");

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(true).when(validateIdentityRuleUtilUseCase).compareString(anyString(), anyString(), anyDouble());
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(LocalDateTime.now()).when(coreFunctionDate).fromDateToLocalDateTime(any(Date.class));
        doReturn(true).when(uploadDocumentRutModifyCiiuUseCase).modifyCiiu(
                anyString(), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_PA, "123456");
        Acquisition acquisition = getAcquisition(PDF_TYPE_DOCUMENT_PA, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        boolean save = uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);

        assertTrue(save);
    }

    @Test
    public void validateAndSaveInformationCeTest() {
        ValidateIdentityReply identityReply = getIdentityInfo("FIRST NAME", "FIRST SURNAME");

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(true).when(validateIdentityRuleUtilUseCase).compareString(anyString(), anyString(), anyDouble());
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(LocalDateTime.now()).when(coreFunctionDate).fromDateToLocalDateTime(any(Date.class));
        doReturn(true).when(uploadDocumentRutModifyCiiuUseCase).modifyCiiu(
                anyString(), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_CE, "123456");
        Acquisition acquisition = getAcquisition(PDF_TYPE_DOCUMENT_CE, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        boolean save = uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);

        assertTrue(save);
    }

    @Test
    public void validateAndSaveInformationNitTest() {
        ValidateIdentityReply identityReply = getIdentityInfo("FIRST NAME", "FIRST SURNAME");

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(true).when(validateIdentityRuleUtilUseCase).compareString(anyString(), anyString(), anyDouble());
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(LocalDateTime.now()).when(coreFunctionDate).fromDateToLocalDateTime(any(Date.class));
        doReturn(true).when(uploadDocumentRutModifyCiiuUseCase).modifyCiiu(
                anyString(), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_NIT, "123456");
        Acquisition acquisition = getAcquisition(DOCUMENT_TYPE_NIT, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        boolean save = uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);

        assertTrue(save);
    }

    @Test(expected = ValidationException.class)
    public void validateAndSaveDifferentDocumentTest() {
        ValidateIdentityReply identityReply = getIdentityInfo("FIRST NAME", "FIRST SURNAME");

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(true).when(validateIdentityRuleUtilUseCase).compareString(anyString(), anyString(), anyDouble());

        doThrow(ValidationException.class).when(uploadDocumentValidateErrors).validateExceptionRutRetries(
                any(Acquisition.class), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_CC, "123456");
        Acquisition acquisition = getAcquisition(DOCUMENT_TYPE_NIT, "789012");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);
    }

    @Test(expected = ValidationException.class)
    public void validateAndSaveNotValidEmissionRutRateTest() {
        ValidateIdentityReply identityReply = getIdentityInfo("FIRST NAME", "FIRST SURNAME");

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(true).when(validateIdentityRuleUtilUseCase).compareString(anyString(), anyString(), anyDouble());
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(LocalDateTime.of(2000, 1, 1, 0, 0)).when(coreFunctionDate).fromDateToLocalDateTime(any(Date.class));

        doThrow(ValidationException.class).when(uploadDocumentValidateErrors).validateExceptionRutRetries(
                any(Acquisition.class), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_CC, "123456");
        Acquisition acquisition = getAcquisition(PDF_TYPE_DOCUMENT_CC, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);
    }

    @Test
    public void validateAndSaveInformationNullIdentityInfoTest() {
        ValidateIdentityReply identityReply = getIdentityInfo(null, null);

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(LocalDateTime.now()).when(coreFunctionDate).fromDateToLocalDateTime(any(Date.class));
        doReturn(true).when(uploadDocumentRutModifyCiiuUseCase).modifyCiiu(
                anyString(), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_CC, "123456");
        Acquisition acquisition = getAcquisition(PDF_TYPE_DOCUMENT_CC, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        boolean save = uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);

        assertTrue(save);
    }

    @Test
    public void validateAndSaveInformationNullFirstNameInfoTest() {
        ValidateIdentityReply identityReply = getIdentityInfo(null, "FIRST SURNAME");

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(LocalDateTime.now()).when(coreFunctionDate).fromDateToLocalDateTime(any(Date.class));
        doReturn(true).when(uploadDocumentRutModifyCiiuUseCase).modifyCiiu(
                anyString(), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_CC, "123456");
        Acquisition acquisition = getAcquisition(PDF_TYPE_DOCUMENT_CC, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        boolean save = uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);

        assertTrue(save);
    }

    @Test
    public void validateAndSaveInformationNullFirstSurnameInfoTest() {
        ValidateIdentityReply identityReply = getIdentityInfo("FIRST NAME", null);

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(LocalDateTime.now()).when(coreFunctionDate).fromDateToLocalDateTime(any(Date.class));
        doReturn(true).when(uploadDocumentRutModifyCiiuUseCase).modifyCiiu(
                anyString(), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_CC, "123456");
        Acquisition acquisition = getAcquisition(PDF_TYPE_DOCUMENT_CC, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        boolean save = uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);

        assertTrue(save);
    }

    @Test(expected = ValidationException.class)
    public void validateAndSaveInformationNotValidNamesTest() {
        ValidateIdentityReply identityReply = getIdentityInfo("FIRST NAME", "FIRST SURNAME");

        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doReturn(identityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        doReturn(false).when(validateIdentityRuleUtilUseCase).compareString(anyString(), anyString(), anyDouble());

        doThrow(ValidationException.class).when(uploadDocumentValidateErrors).validateExceptionRutRetries(
                any(Acquisition.class), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        KofaxRutInformation kofaxRutInformation = getKofaxRutInformation(KOFAX_CC, "123456");
        Acquisition acquisition = getAcquisition(PDF_TYPE_DOCUMENT_CC, "123456");
        SqsMessageParamAllObject sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();

        uploadDocumentRutSaveUseCase.validateAndSaveInformation(
                kofaxRutInformation, acquisition, "USRTRANS", sqsMessageParamAllObject);
    }

    private KofaxRutInformation getKofaxRutInformation(String identificationType, String documentNumber) {
        return KofaxRutInformation.builder()
                .identificationType(identificationType).documentNumber(documentNumber)
                .firstName("FIRST NAME").firstSurname("FIRST SURNAME")
                .emissionDate("01/01/1900").mainActivity("0001").build();
    }

    private Acquisition getAcquisition(String documentType, String documentNumber) {
        return Acquisition.builder()
                .id(UUID.randomUUID()).documentNumber(documentNumber)
                .documentType(DocumentType.builder().code(documentType).build()).build();
    }

    private ValidateIdentityReply getIdentityInfo(String firstName, String firstSurname) {
        return ValidateIdentityReply.builder().firstName(firstName).firstSurname(firstSurname).build();
    }
}
