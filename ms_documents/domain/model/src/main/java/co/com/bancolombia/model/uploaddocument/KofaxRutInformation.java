package co.com.bancolombia.model.uploaddocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@Validated
@AllArgsConstructor
public class KofaxRutInformation {
    private String mainActivity;
    private String identificationType;
    private String documentNumber;
    private String firstName;
    private String firstSurname;
    private String emissionDate;
}
