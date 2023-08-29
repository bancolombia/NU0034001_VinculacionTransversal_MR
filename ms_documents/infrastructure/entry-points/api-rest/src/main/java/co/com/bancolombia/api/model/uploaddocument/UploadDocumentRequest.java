package co.com.bancolombia.api.model.uploaddocument;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.Request;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UploadDocumentRequest extends Request {

    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private UploadDocumentRequestData data;

    public UploadDocumentRequest(MetaRequest metaRequest, UploadDocumentRequestData data) {
        super(metaRequest);
        this.data = data;
    }
}
