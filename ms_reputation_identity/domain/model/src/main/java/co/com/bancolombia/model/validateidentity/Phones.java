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
public class Phones {

    private String phoneNumber;
    private String  entities;
    private String  department;
    private String  idCountry;
    private String  city;
    private String  address;
    private String  lastConfirmation;
    private String  idArea;
    private String  idAddress;
    private String  useType;
    private String  source;
    private Date creationDate;
    private Date updateDate;
    private String  numberReports;
    private String  typeResidence;
    private String  typeCorrespondence;
}
