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
public class RegisterControlListError {
    private String title;
    private String detail;
    private String code;
    private String status;
}
