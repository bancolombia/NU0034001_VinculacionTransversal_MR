package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.ApiRiskCredential;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentification;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationRequest;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.gateways.ValidateIdentificationRestRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ATTEMPTS_ALLOWED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CREDIT_LIST_HISTORY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.INVALID_IDENTIFICATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_DATA_MATCH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_EXIST_IDENTIFICATION;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ExpoQuestionIdentificationUseCaseTest {

    @InjectMocks
    @Spy
    private ExpoQuestionIdentificationUseCaseImpl vIdentificationUC;

    @Mock
    private ApiRiskCredential apiRiskCredential;

    @Mock
    private ValidateIdentificationRestRepository vIdentificationRestRepository;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ExpoQuestionQuestionnaireUseCase questionnaireUC;

    @Mock
    private ExpoQuestionSaveUseCase expoQuestionSUC;

    @Mock
    private ExpoQuestionValidationUseCase expoQuestionVUC;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startProcessVIdentificationTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentNumber("1234")
                .documentTypeCodeGenericType("FS001").build();
        BasicAcquisitionRequest bARequest = BasicAcquisitionRequest.builder().messageId("").build();
        ValidateIdentityReply persoInfo = ValidateIdentityReply.builder().firstName("").firstSurname("")
                .expeditionDate(new Date()).build();
        doNothing().when(expoQuestionVUC).validationStageOne(any(AcquisitionReply.class));
        doReturn(persoInfo).when(this.expoQuestionVUC).validateMissingData(any(AcquisitionReply.class));
        QuestionnaireResponse qResponse = QuestionnaireResponse.builder().build();
        ValidateIdentificationRequest vIdentifiRequest = ValidateIdentificationRequest.builder().build();
        doReturn(vIdentifiRequest).when(vIdentificationUC).createVIdentifiRequest(any(AcquisitionReply.class),
                any(ValidateIdentityReply.class));
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        ValidateIdentificationResponse vIdentifiResponse = ValidateIdentificationResponse.builder()
                .data(Collections.singletonList(ValidateIdentification.builder()
                        .resultCodeValidation(CREDIT_LIST_HISTORY).build())).infoReuseCommon(infoReuseCommon).build();
        doReturn(vIdentifiResponse).when(vIdentificationRestRepository)
                .getUserInfoIdentification(any(ValidateIdentificationRequest.class), anyString());
        doReturn("").when(expoQuestionSUC).saveIdentification(any(AcquisitionReply.class),
                any(BasicAcquisitionRequest.class),any(ValidateIdentificationResponse.class));
        doReturn(qResponse).when(vIdentificationUC).actionValidation(any(AcquisitionReply.class),
                any(BasicAcquisitionRequest.class),any(ValidateIdentificationResponse.class),anyString());
        QuestionnaireResponse result = vIdentificationUC.starProcessVIdentification(acquisitionReply,
                bARequest);
        assertNotNull(result);
    }

    @Test
    public void createVIdentifiRecuestTest(){
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentNumber("1234")
                .documentTypeCodeGenericType("FS001").build();
        ValidateIdentityReply persoInfo = ValidateIdentityReply.builder().firstName("").firstSurname("")
                .expeditionDate(new Date()).build();
        assertNotNull(this.vIdentificationUC.createVIdentifiRequest(acquisitionReply,persoInfo));
    }

    @Test
    public void actionValidationTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        BasicAcquisitionRequest bARequest = BasicAcquisitionRequest.builder().messageId("").build();
        ValidateIdentificationResponse vIdentificationResponse = ValidateIdentificationResponse.builder()
                .data(Collections.singletonList(ValidateIdentification.builder()
                        .resultCodeValidation(CREDIT_LIST_HISTORY).build())).build();
        vIdentificationUC.actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
        Mockito.verify(this.vIdentificationUC, Mockito.times(1))
                .actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
    }

    @Test
    public void actionValidationNoDataMatchTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        BasicAcquisitionRequest bARequest = BasicAcquisitionRequest.builder().messageId("").build();
        ValidateIdentificationResponse vIdentificationResponse = ValidateIdentificationResponse.builder()
                .data(Collections.singletonList(ValidateIdentification.builder()
                        .resultCodeValidation(NO_DATA_MATCH).build())).build();
        Mockito.doNothing().when(vIdentificationUC).validateStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        vIdentificationUC.actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
        Mockito.verify(this.vIdentificationUC, Mockito.times(1))
                .actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
    }

    @Test
    public void actionValidationAttemptsAllowedTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        BasicAcquisitionRequest bARequest = BasicAcquisitionRequest.builder().messageId("").build();
        ValidateIdentificationResponse vIdentificationResponse = ValidateIdentificationResponse.builder()
                .data(Collections.singletonList(ValidateIdentification.builder()
                        .resultCodeValidation(ATTEMPTS_ALLOWED).build())).build();
        Mockito.doNothing().when(vIdentificationUC).validateStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        doReturn("").when(expoQuestionVUC).blockCustomer(any(AcquisitionReply.class), anyString());
        vIdentificationUC.actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
        Mockito.verify(this.vIdentificationUC, Mockito.times(1))
                .actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
    }

    @Test
    public void actionValidationNoExistIdentificationTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        BasicAcquisitionRequest bARequest = BasicAcquisitionRequest.builder().messageId("").build();
        ValidateIdentificationResponse vIdentificationResponse = ValidateIdentificationResponse.builder()
                .data(Collections.singletonList(ValidateIdentification.builder()
                        .resultCodeValidation(NO_EXIST_IDENTIFICATION).build())).build();
        Mockito.doNothing().when(vIdentificationUC).validateStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        vIdentificationUC.actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
        Mockito.verify(this.vIdentificationUC, Mockito.times(1))
                .actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
    }

    @Test
    public void actionValidationInvalidIdentificationTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        BasicAcquisitionRequest bARequest = BasicAcquisitionRequest.builder().messageId("").build();
        ValidateIdentificationResponse vIdentificationResponse = ValidateIdentificationResponse.builder()
                .data(Collections.singletonList(ValidateIdentification.builder()
                        .resultCodeValidation(INVALID_IDENTIFICATION).build())).build();
        Mockito.doNothing().when(vIdentificationUC).validateStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        vIdentificationUC.actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
        Mockito.verify(this.vIdentificationUC, Mockito.times(1))
                .actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
    }

    @Test
    public void actionValidationNullTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        BasicAcquisitionRequest bARequest = BasicAcquisitionRequest.builder().messageId("").build();
        ValidateIdentificationResponse vIdentificationResponse = ValidateIdentificationResponse.builder()
                .data(Collections.singletonList(ValidateIdentification.builder()
                        .resultCodeValidation("").build())).build();
        Mockito.doNothing().when(vIdentificationUC).validateStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        vIdentificationUC.actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
        Mockito.verify(this.vIdentificationUC, Mockito.times(1))
                .actionValidation(acquisitionReply, bARequest, vIdentificationResponse, "");
    }

    @Test
    public void validateStateAndErrorsTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        vIdentificationUC.validateStateAndErrors(acquisitionReply, "", "", "");
        Mockito.verify(this.vIdentificationUC, Mockito.times(1))
                .validateStateAndErrors(acquisitionReply, "", "", "");
    }

    @Test
    public void validateStateAndErrorsAttemsAllowedTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        vIdentificationUC.validateStateAndErrors(acquisitionReply, "", ATTEMPTS_ALLOWED, "");
        Mockito.verify(this.vIdentificationUC, Mockito.times(1))
                .validateStateAndErrors(acquisitionReply, "", ATTEMPTS_ALLOWED, "");
    }
}
