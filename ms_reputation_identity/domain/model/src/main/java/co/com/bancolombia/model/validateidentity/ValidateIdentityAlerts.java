package co.com.bancolombia.model.validateidentity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityAlerts {

    private String typeAlert;
    private String idCountry;
    private String lengthRecord;
    private String typeRecord;
    private String text;
    private String idAlert;
    private Date modificationDate;
    private Date expirationDate;
    private Date recordDate;
}
