package co.com.bancolombia.api.model.acquisition;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * Activity
 */
@Validated
@Data
@Builder
public class Activity {
    @JsonProperty("idActivity")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 5)
    @Valid
    private String idActivity;

    @JsonProperty("nameActivity")
    @ApiModelProperty(example = "Perfilar Vinculacion", value = "")
    @Size(min = 1, max = 100)
    @Valid
    private String nameActivity;

    @JsonProperty("codeStateActivity")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 1)
    @Valid
    private String codeStateActivity;

    @JsonProperty("nameStateActivity")
    @ApiModelProperty(example = "COMPLETADO", value = "")
    @Size(min = 1, max = 15)
    @Valid
    private String nameStateActivity;
}
