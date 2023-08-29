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
public class Emails {

    private String email;
    private String lastConfirmation;
    private String entities;
    private String source;
    private String idEmail;
    private Date creationDate;
    private Date updateDate;
    private String reporting;
    private String numberReports;
}
