package co.com.bancolombia.api.model.acquisition;

import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Validated
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestUser {

    @JsonProperty("documentType")
    @ApiModelProperty(required = true, value = "", example = "TIPDOC_FS003")
    @Size(min = 12, max = 12)
    @NotNull
    @Valid
    private String documentType;

    @JsonProperty("documentNumber")
    @ApiModelProperty(required = true, value = "1061000000", example = "900185170")
    @Pattern(regexp = ConstantsRegex.REGEX_ALPHANUMERIC_WITHOUT_SPECIALS)
    @NotNull
    @Size(min = 1, max = 20)
    private String documentNumber;

    @JsonProperty("channelId")
    @NotNull
    @Size(min = 1, max = 9)
    @ApiModelProperty(required = true, value = "", example = "004")
    private String channelId;

    @JsonProperty("businessLineId")
    @NotNull
    @Size(min = 1, max = 3)
    @ApiModelProperty(required = true, value = "", example = "001")
    private String businessLineId;

    @JsonProperty("token")
    @ApiModelProperty(required = true, value = "", example = "123456")
    @NotNull
    @Size(min = 1, max = 150)
    private String token;
}
