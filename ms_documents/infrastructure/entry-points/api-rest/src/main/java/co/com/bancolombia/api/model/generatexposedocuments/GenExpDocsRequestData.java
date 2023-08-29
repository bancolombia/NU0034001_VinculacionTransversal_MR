package co.com.bancolombia.api.model.generatexposedocuments;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class GenExpDocsRequestData extends UserInfoRequestData {
    @ApiModelProperty(example = "0", value = "Formato conocimiento cliente")
    @Size(min = 1, max = 2, groups = ValidationMandatory.class)
    @Pattern(regexp = "0", groups = ValidationMandatory.class)
    @NotNull(groups = ValidationMandatory.class)
    @JsonProperty("formatCode")
    @Valid
    private String formatCode;
}