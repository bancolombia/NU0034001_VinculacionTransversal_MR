package co.com.bancolombia.model.basicinformation;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class BasicInformation extends Auditing implements Mergeable {
    private UUID id;
    @ExecFieldAnnotation("GENDER")
    private String gender;
    @ExecFieldAnnotation("BIRHTCITY")
    private String birthCity;
    @ExecFieldAnnotation("COUNTRY")
    private String country;
    @ExecFieldAnnotation("BIRTHDEPART")
    private String birthDepartment;
    @ExecFieldAnnotation("CIVILSTA")
    private String civilStatus;
    @ExecFieldAnnotation("NATIONAL")
    private String nationality;
    @ExecFieldAnnotation("DEPENDAN")
    private Integer dependants;
    @ExecFieldAnnotation("EDUCLEVL")
    private String educationLevel;
    @ExecFieldAnnotation("SOCIALSTRA")
    private String socialStratum;
    @ExecFieldAnnotation("HOUSTYPE")
    private String housingType;
    @ExecFieldAnnotation("PEP")
    private String pep;
    @ExecFieldAnnotation("CONTRATYPE")
    private String contractType;
    @ExecFieldAnnotation("ENCMPDAT")
    private Date entryCompanyDate;
    private Acquisition acquisition;
}