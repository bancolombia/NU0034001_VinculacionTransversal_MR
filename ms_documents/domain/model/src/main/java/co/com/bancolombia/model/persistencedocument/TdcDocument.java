package co.com.bancolombia.model.persistencedocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class TdcDocument {

    private String acquisitionId;
    private List<TdcDocumentsFile> documentsFileList;
    private boolean flagRutRequired;
    private Date expedionDateCC;
    private String messageId;
    private String documentNumber;
    private String documentType;
}
