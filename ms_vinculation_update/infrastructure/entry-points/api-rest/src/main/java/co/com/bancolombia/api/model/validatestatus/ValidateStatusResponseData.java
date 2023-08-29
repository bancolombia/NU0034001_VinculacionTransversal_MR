package co.com.bancolombia.api.model.validatestatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * ValidateStatusResponseData
 */
@Validated
@Data
@Builder
public class ValidateStatusResponseData {
    @JsonProperty("startList")
    @ApiModelProperty(value = "")
    @Valid
    private List<ValidateStatusResponseDataStartList> startList;
}
