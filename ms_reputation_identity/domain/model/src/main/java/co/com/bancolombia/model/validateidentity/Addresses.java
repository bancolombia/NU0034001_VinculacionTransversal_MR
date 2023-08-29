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
public class Addresses {

    private String entities;
    private String area;
    private String department;
    private String city;
    private String lastConfirmation;
    private String idAddress;
    private String idCountry;
    private String idDepartment;
    private String addressType;
    private String source;
    private Date creationDate;
    private Date updateDate;
    private String numberReports;
    private String socioEconomicStratum;
    private String deliveryProbality;
    private String typeResidence;
    private String useType;
    private String typeCorrespondence;
    private String address;
}
