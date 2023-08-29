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
public class NaturalLegalPerson {

    private String economicActivity;
    private String rut;
    private String idCIIU;
    private String names;
    private String firstSurname;
    private String secondSurname;
    private String fullName;
    private String gender;
    private String maritalStatus;
    private String checkInformation;
    private IdDetail idDetail;
    private Age age;
    private String antiquity;
}
