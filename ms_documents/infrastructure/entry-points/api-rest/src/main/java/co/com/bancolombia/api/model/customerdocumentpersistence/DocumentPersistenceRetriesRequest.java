package co.com.bancolombia.api.model.customerdocumentpersistence;

import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DocumentPersistenceRetriesRequest {

    @JsonProperty("data")
    @NotNull(groups = {ValidationMandatory.class})
    @Valid
    private List<PersistenceDocument> data;

    public DocumentPersistenceRetriesRequest(List<PersistenceDocument> data) {
        this.data = data;
    }
}
