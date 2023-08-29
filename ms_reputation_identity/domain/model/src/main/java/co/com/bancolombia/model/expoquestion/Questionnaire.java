package co.com.bancolombia.model.expoquestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Questionnaire {
    private String questionnarieId;
    private String questionnaireRecordId;
    private String questionnarieResult;
    private String numberAttemptsYear;
    private String numberAttemptsMonth;
    private String numberAttemptsDay;
    private String excludeCustomerParameterized;
    private AlertQuestionnaire alert;
    private List<Question> question;
    private List<ResponseOption> responseOptions;
}
