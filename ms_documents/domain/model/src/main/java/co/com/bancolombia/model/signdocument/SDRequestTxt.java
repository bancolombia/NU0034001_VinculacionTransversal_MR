package co.com.bancolombia.model.signdocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public class SDRequestTxt {

    private String acquisitionId;
    private String documentNumber;
    private String documentType;
    private String messageId;
    private String dateRequest;
    private String ip;
    private String usrMod;
    private Integer iteration;
}
