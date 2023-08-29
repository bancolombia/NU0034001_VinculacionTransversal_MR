package co.com.bancolombia.model.uploaddocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@Validated
@AllArgsConstructor
public class KofaxInformation {
    private String documentNumber;
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private Date birthDate;
    private Date expeditionDate;
    private String gender;
}
