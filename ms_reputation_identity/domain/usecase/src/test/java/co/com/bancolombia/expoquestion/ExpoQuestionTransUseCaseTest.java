package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.AlertIdentification;
import co.com.bancolombia.model.expoquestion.ExpoQuestionSave;
import co.com.bancolombia.model.expoquestion.Meta;
import co.com.bancolombia.model.expoquestion.ValidateIdentification;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationAlert;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationSave;
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

@RequiredArgsConstructor
public class ExpoQuestionTransUseCaseTest {
    @InjectMocks
    @Spy
    private ExpoQuestionTransUseCaseImpl expoQuestionTUC;

    @Mock
    private CoreFunctionDate coreFD;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void transExpoQuestionSaveTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        BasicAcquisitionRequest ba = BasicAcquisitionRequest.builder().userTransaction("").build();
        ValidateIdentificationResponse vIdentificationResponse =
                ValidateIdentificationResponse.builder().build();
        ValidateIdentificationSave validateIdentificationSave = ValidateIdentificationSave.builder().build();
        Mockito.doReturn(validateIdentificationSave).when(expoQuestionTUC)
                .transVIdentificationSave(vIdentificationResponse);
        ExpoQuestionSave expoQuestionSave = expoQuestionTUC.transExpoQuestionSave(acquisitionReply,ba,
                vIdentificationResponse);
        assertNotNull(expoQuestionSave);
    }

    @Test
    public void transVIdentificationAlertsTest() {
        List<AlertIdentification> alerts = Collections.singletonList(AlertIdentification.builder().build());
        List<ValidateIdentificationAlert> alertsList = expoQuestionTUC.transVIdentificationAlerts(alerts);
        assertNotNull(alertsList);
    }

    @Test
    public void transVIdentificationAlertsTestNull() {
        List<ValidateIdentificationAlert> alertsList = expoQuestionTUC.transVIdentificationAlerts(null);
        assertNotNull(alertsList);
    }

    @Test
    public void transValidateIdentificationSaveTest() {
        Meta meta = Meta.builder().messageId("").build();
        List<ValidateIdentification> list = Collections.singletonList(ValidateIdentification.builder().build());
        ValidateIdentificationResponse vIdentifiResponse = ValidateIdentificationResponse.builder().data(list)
                .meta(meta).build();
        ValidateIdentificationSave validateIdentificationSave = expoQuestionTUC
                .transVIdentificationSave(vIdentifiResponse);
        assertNotNull(validateIdentificationSave);
    }
}
