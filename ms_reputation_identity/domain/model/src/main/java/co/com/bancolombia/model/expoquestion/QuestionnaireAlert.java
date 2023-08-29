package co.com.bancolombia.model.expoquestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireAlert {
    private String customerAlerts;
    private String responseToAlert;
    private String alertCode;
}
