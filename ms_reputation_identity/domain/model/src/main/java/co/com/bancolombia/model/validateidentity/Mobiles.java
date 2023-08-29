package co.com.bancolombia.model.validateidentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Mobiles {

    private String entities;
    private String lastConfirmation;
    private String type;
    private String source;
    private String mobile;
    private Date creationDate;
    private String idMobile;
    private Date updateDate;
    private String reporting;
    private String numberReports;
}
