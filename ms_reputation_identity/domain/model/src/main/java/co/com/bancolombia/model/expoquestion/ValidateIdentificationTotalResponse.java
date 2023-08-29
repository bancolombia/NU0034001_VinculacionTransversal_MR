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
public class ValidateIdentificationTotalResponse {
    private ValidateIdentificationResponse validateIdentificationResponse;
    private ExpoQuestionErrorResponse errors;
    private InfoReuseCommon infoReuseCommon;
}
