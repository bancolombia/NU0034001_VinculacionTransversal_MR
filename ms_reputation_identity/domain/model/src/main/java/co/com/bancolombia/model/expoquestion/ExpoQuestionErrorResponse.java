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
public class ExpoQuestionErrorResponse {

    private Meta meta;
    private List<ExpoQuestionError> errors;
}
