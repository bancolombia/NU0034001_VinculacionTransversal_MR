package co.com.bancolombia.api.model.uploaddocument;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class UploadDocumentRequestData extends UserInfoRequestData {

    @JsonProperty("filesList")
    @ApiModelProperty(value = "")
    @Valid
    @NotNull(groups = ValidationOptional.class)
    private List<UploadDocumentFilesListRequest> filesList;
}
