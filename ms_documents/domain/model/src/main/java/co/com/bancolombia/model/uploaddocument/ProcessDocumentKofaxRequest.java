package co.com.bancolombia.model.uploaddocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ProcessDocumentKofaxRequest {
    private Integer userCode;
    private String processName;
    private String processCode;
    private List<ProcessDocumentFiles> files;
}
