package co.com.bancolombia.restclient.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.IdentityValResultReply;
import co.com.bancolombia.model.expoquestion.ExpoQuestionSave;
import co.com.bancolombia.model.expoquestion.gateways.ExpoQuestionSaveRepository;
import co.com.bancolombia.model.exposerabbit.IdentityValResultRabbitRepository;
import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_IDENTITY_SCORE_NOT_FOUND;

@Component
@AllArgsConstructor
public class IdentityValResultRabbit extends ErrorHandleRabbit implements IdentityValResultRabbitRepository {

    private ValidateIdentityScoreRepository validateIdentityScoreRepository;
    private ExpoQuestionSaveRepository expoQuestionSaveRepository;

    @Override
    public IdentityValResultReply getIndentityValResultReply(AcquisitionIdQuery query) {

        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());
            ValidateIdentityScore validateIdentityScore = validateIdentityScoreRepository
                    .findByAcquisitionId(query.getAcquisitionId());
            if (validateIdentityScore == null) {
                String message = ERROR_MSG_IDENTITY_SCORE_NOT_FOUND + SPACE + query.getAcquisitionId();
                throwExceptionRabbit(message);
            }
            String accumulated = validateIdentityScore.getAccumulated().toString();
            Date questionnaireCreatedDate = null;
            ExpoQuestionSave expoQuestionSave = expoQuestionSaveRepository
                    .findByAcquisitionId(query.getAcquisitionId());
            if (expoQuestionSave != null && expoQuestionSave.getQuestionnaire() != null) {
                questionnaireCreatedDate = expoQuestionSave.getQuestionnaire().getCreatedDate();
            }
            return IdentityValResultReply.builder()
                    .validateIdentityScoreAccumulated(accumulated)
                    .questionnaireCreatedDate(questionnaireCreatedDate)
                    .valid(true).build();
        } catch (ValidationException | CustomException ex) {
            return IdentityValResultReply.builder().valid(false)
                    .errorList(getErrorFromException(ex)).build();
        }
    }

    private void validateMandatory(AcquisitionIdQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
            fieldList.add("acquisitionId");
        }
        errorMandatory(fieldList);
    }
}
