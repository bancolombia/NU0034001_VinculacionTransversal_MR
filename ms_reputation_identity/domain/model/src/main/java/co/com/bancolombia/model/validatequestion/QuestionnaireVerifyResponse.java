package co.com.bancolombia.model.validatequestion;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
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
public class QuestionnaireVerifyResponse {

    private Meta meta;
    private List<QuestionnaireVerify> data;
    private InfoReuseCommon infoReuseCommon;
}
