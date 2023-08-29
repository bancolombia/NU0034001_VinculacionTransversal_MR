package co.com.bancolombia.model.generatetoken.reuseresponse;

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
public class GTResponse {
    String answerCode;
    String answerDescription;
    String tokenGenerated;
    String responseDate;
}
