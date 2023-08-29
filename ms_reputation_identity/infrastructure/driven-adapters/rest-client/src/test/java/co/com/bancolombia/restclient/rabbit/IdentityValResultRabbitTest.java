package co.com.bancolombia.restclient.rabbit;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.IdentityValResultReply;
import co.com.bancolombia.model.expoquestion.ExpoQuestionSave;
import co.com.bancolombia.model.expoquestion.QuestionnaireSave;
import co.com.bancolombia.model.expoquestion.gateways.ExpoQuestionSaveRepository;
import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityScoreRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class IdentityValResultRabbitTest {
    @InjectMocks
    @Spy
    private IdentityValResultRabbit identityValResultRabbit;

    @Mock
    private ValidateIdentityScoreRepository validateIdentityScoreRepository;

    @Mock
    private ExpoQuestionSaveRepository expoQuestionSaveRepository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void getIndentityValResultReplySuccessTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().accumulated(new BigDecimal(2))
                .build();
        doReturn(validateIdentityScore).when(validateIdentityScoreRepository).findByAcquisitionId(any(String.class));
        ExpoQuestionSave expoQuestionSave = ExpoQuestionSave.builder().questionnaire(QuestionnaireSave.builder()
                .createdDate(new Date()).build()).build();
        doReturn(expoQuestionSave).when(expoQuestionSaveRepository).findByAcquisitionId(any(String.class));
        IdentityValResultReply reply = identityValResultRabbit.getIndentityValResultReply(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getIndentityValResultReplyNullQuestionnaireTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().accumulated(new BigDecimal(2))
                .build();
        doReturn(validateIdentityScore).when(validateIdentityScoreRepository).findByAcquisitionId(any(String.class));
        doReturn(null).when(expoQuestionSaveRepository).findByAcquisitionId(any(String.class));
        IdentityValResultReply reply = identityValResultRabbit.getIndentityValResultReply(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getIndentityValResultReplyFalseTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(null).build();
        IdentityValResultReply reply = identityValResultRabbit.getIndentityValResultReply(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getIndentityValResultReplyNullTest() {
        IdentityValResultReply reply = identityValResultRabbit.getIndentityValResultReply(null);
        assertFalse(reply.isValid());
    }
}
