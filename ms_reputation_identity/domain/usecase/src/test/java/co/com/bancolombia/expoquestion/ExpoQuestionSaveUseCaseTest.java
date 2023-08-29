package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.ExpoQuestionSave;
import co.com.bancolombia.model.expoquestion.Meta;
import co.com.bancolombia.model.expoquestion.Questionnaire;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.QuestionnaireSave;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationSave;
import co.com.bancolombia.model.expoquestion.gateways.ExpoQuestionSaveRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class ExpoQuestionSaveUseCaseTest {

    @InjectMocks
    @Spy
    private ExpoQuestionSaveUseCaseImpl expoQuestionSUC;

    @Mock
    private ExpoQuestionSaveRepository saveRepository;

    @Mock
    private ExpoQuestionTransUseCase expoQuestionTUC;

    @Mock
    private ExpoQuestionQuestionnaireTransUseCase questionnaireTUC;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveIdentification() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().build();
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder().build();
        ValidateIdentificationResponse validateIdentificationResponse =
                ValidateIdentificationResponse.builder().build();
        ValidateIdentificationSave validateIdentificationSave = ValidateIdentificationSave.builder().build();
        ExpoQuestionSave expoQuestionSave =
                ExpoQuestionSave.builder().id("").validateIdentification(validateIdentificationSave).build();
        Mockito.doReturn(expoQuestionSave).when(expoQuestionTUC).transExpoQuestionSave(any(AcquisitionReply.class),
                any(BasicAcquisitionRequest.class), any(ValidateIdentificationResponse.class));
        Mockito.doReturn(expoQuestionSave).when(saveRepository).saveIdentification(any(ExpoQuestionSave.class));
        assertNotNull(expoQuestionSUC.saveIdentification(acquisitionReply, basicAcquisitionRequest,
                validateIdentificationResponse));
    }

    @Test
    public void saveQuestionnaire() {
        String idExpoQuestion = "";
        Meta meta = Meta.builder().messageId("").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        List<Questionnaire> data = Collections.singletonList(Questionnaire.builder().questionnaireRecordId("").build());

        QuestionnaireResponse questionnaireResponse =
                QuestionnaireResponse.builder().meta(meta).data(data).infoReuseCommon(infoReuseCommon).build();
        ExpoQuestionSave expoQuestionSave =
                ExpoQuestionSave.builder().id("").build();
        Mockito.doReturn(expoQuestionSave).when(expoQuestionTUC).transExpoQuestionSave(any(AcquisitionReply.class),
                any(BasicAcquisitionRequest.class), any(ValidateIdentificationResponse.class));
        Mockito.doReturn(ExpoQuestionSave.builder().build()).when(saveRepository)
                .saveQuestionnaire(any(QuestionnaireSave.class), anyString());
        expoQuestionSUC.saveQuestionnaire(questionnaireResponse,idExpoQuestion);
        Mockito.verify(expoQuestionSUC, Mockito.times(1))
                .saveQuestionnaire(questionnaireResponse, idExpoQuestion);
    }
}
