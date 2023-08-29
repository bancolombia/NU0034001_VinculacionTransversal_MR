package co.com.bancolombia.model.expoquestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireRequest {
    private List<QuestionnaireList> data;
}
