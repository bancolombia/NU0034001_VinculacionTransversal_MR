package co.com.bancolombia.model.expoquestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireQuestion {
    private String identifier;
    private String text;
    private String order;
    private String correctAnswerId;
    private String weight;
}
