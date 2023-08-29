package co.com.bancolombia.model.expoquestion;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireTotalResponse {

    private QuestionnaireResponse questionnaireResponse;
    private ExpoQuestionErrorResponse errors;
    private InfoReuseCommon infoReuseCommon;
}