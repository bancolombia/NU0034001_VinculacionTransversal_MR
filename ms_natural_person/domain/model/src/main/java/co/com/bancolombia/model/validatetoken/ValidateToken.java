package co.com.bancolombia.model.validatetoken;

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
public class ValidateToken extends Auditing {
    private UUID id;
    private String tokenCode;
    private String customerDocumentType;
    private String customerDocumentNumber;
    private String cellphoneNumber;
    private String answerCode;
    private String answerDescription;
    private Date requestDate;
    private Date responseDate;
    private String messageId;
    private Acquisition acquisition;

    private InfoReuseCommon infoReuseCommon;
}
