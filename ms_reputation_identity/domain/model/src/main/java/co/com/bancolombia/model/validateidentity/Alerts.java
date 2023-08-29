package co.com.bancolombia.model.validateidentity;

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
public class Alerts {

    private String typeAlert;
    private String idCountry;
    private String lengthRecord;
    private String typeRecord;
    private String text;
    private String idAlert;
    private String modificationDate;
    private String expirationDate;
    private String recordDate;
}
