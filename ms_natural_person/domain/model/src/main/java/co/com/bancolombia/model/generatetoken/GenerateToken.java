package co.com.bancolombia.model.generatetoken;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class GenerateToken extends Auditing {
    private UUID id;
    private String messageId;
    private Date requestDate;
    private Date responseDate;
    private String answerCode;
    private String answerName;
    private String cellphone;
    private String email;
    private Acquisition acquisition;

    private InfoReuseCommon infoReuseCommon;
}