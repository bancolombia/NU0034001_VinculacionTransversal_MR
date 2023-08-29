package co.com.bancolombia.api.model.util;

import co.com.bancolombia.api.model.MetaResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RequiredArgsConstructor
@AllArgsConstructor
public class Error {
    @JsonProperty("errors")
    @ApiModelProperty(value = "")
    @Valid
    @NotNull
    private List<ErrorItem> errors = null;

    @JsonProperty("meta")
    @ApiModelProperty(value = "")
    @Valid
    @NotNull
    private MetaResponse meta = null;
}
