package co.com.bancolombia.commonsvnt.api.model.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

/**
 * ErrorItem
 */
@Validated
@Data
@AllArgsConstructor
public class ErrorItem {
    @JsonProperty("title")
    @ApiModelProperty(example = "A short, human-readable summary of the problem", value = "")
    @Size(max = 200)
    private String title = null;

    @JsonProperty("code")
    @ApiModelProperty(example = "ERR_PROF_CATALOGTYPE", value = "")
    @Size(max = 10)
    private String code = null;

    @JsonProperty("detail")
    @ApiModelProperty(example = "CATALOG NOT FOUND", value = "")
    @Size(max = 40)
    private String detail = null;

}
