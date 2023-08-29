package co.com.bancolombia.model.markcustomer;

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
public class RegisterControlListList {

    private String department;
    private String category;
    private String subCategory;
    private String reason;
    private String customerType;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String surname;
    private String secondSurName;
    private String secondName;
}
