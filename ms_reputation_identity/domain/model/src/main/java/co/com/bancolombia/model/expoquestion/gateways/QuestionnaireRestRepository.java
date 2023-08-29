package co.com.bancolombia.model.expoquestion.gateways;

import co.com.bancolombia.model.expoquestion.QuestionnaireRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;

import java.util.Date;

public interface QuestionnaireRestRepository {
    QuestionnaireResponse getUserInfoQuestionnaire(QuestionnaireRequest request, String messageId,
                                                   Date dateRequestApi, String requestIdentification,
                                                   String responseIdentification);
}
