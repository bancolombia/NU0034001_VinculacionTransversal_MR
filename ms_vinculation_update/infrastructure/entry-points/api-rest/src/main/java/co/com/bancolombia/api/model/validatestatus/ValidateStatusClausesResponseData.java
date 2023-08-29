package co.com.bancolombia.api.model.validatestatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * ValidateStatusClausesResponseData
 */
@Validated
@Data
@Builder
public class ValidateStatusClausesResponseData {

    @JsonProperty("clauseCode")
    @ApiModelProperty(example = "AAA", value = "")
    @Size(min = 1, max = 10)
    @Valid
    private String clauseCode;

    @JsonProperty("clauseName")
    @ApiModelProperty(example = "Example clauses", value = "")
    @Size(min = 1, max = 1000)
    @Valid
    private String clauseName;

    @JsonProperty("acceptClauses")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 2)
    @Valid
    private String acceptClauses;

    @JsonProperty("acceptanceDateClauses")
    @ApiModelProperty(example = "yyyy-MM-ddTHH:mm:ss.sss", value = "")
    @Size(min = 15, max = 23)
    @Valid
    private String acceptanceDateClauses;
}
