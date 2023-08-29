package co.com.bancolombia.model.persistencedocument;

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
public class TdcDocumentsFile {

    private String documentalTypeCode;
    private String documentalSubTypeCode;
    private List<String> fileNames;
}
