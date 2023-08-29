package co.com.bancolombia.model.uploaddocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ProcessDocumentKofaxTotalRequest {
    private ProcessDocumentKofaxRequest data;
}
