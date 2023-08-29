package co.com.bancolombia.model.personalinformation;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class PersonalInformation extends Auditing implements Mergeable {
    private UUID id;
    @ExecFieldAnnotation("FIRSTNAME")
    private String firstName;
    @ExecFieldAnnotation("SECONAME")
    private String secondName;
    @ExecFieldAnnotation("FSURNAME")
    private String firstSurname;
    @ExecFieldAnnotation("SSURNAME")
    private String secondSurname;
    @ExecFieldAnnotation("EXPTDATE")
    private Date expeditionDate;
    @ExecFieldAnnotation("EXPPLACE")
    private String expeditionPlace;
    @ExecFieldAnnotation("EXPCOUNT")
    private String expeditionCountry;
    @ExecFieldAnnotation("EXPDEPART")
    private String expeditionDepartment;
    @ExecFieldAnnotation("BIRTHDAT")
    private Date birthdate;
    @ExecFieldAnnotation("PRCPHONE")
    private String cellphone;
    @ExecFieldAnnotation("PREMAIL")
    private String email;
    private Acquisition acquisition;

    public String getFullName() {
        return Stream.of(this.firstName, this.secondName, this.firstSurname, this.secondSurname)
                .filter(cadena -> cadena != null && !cadena.isEmpty()).collect(Collectors.joining(" "));
    }
    
    public String getFullNameInverse() {
        return Stream.of(this.firstSurname, this.secondSurname, this.firstName, this.secondName)
                .filter(cadena -> cadena != null && !cadena.isEmpty()).collect(Collectors.joining(" "));
    }
    
    public String getNames() {
        return Stream.of(this.firstName, this.secondName)
                .filter(cadena -> cadena != null && !cadena.isEmpty()).collect(Collectors.joining(" "));
    }
}