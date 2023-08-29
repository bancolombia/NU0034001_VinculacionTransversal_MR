package co.com.bancolombia.api.model.signdocument;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class SDRequestData extends UserInfoRequestData {

    @ApiModelProperty(example = "0", value = "Formato conocimiento cliente")
    @Size(min = 1, max = 2, groups = ValidationMandatory.class)
    @Pattern( regexp = "0", groups = ValidationMandatory.class)
    @NotNull(groups = ValidationMandatory.class)
    @JsonProperty("documentCode")
    private String documentCode;
}
