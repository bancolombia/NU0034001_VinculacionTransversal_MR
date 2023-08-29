package co.com.bancolombia.api.model.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Meta {
    @JsonProperty("_systemId")
    @ApiModelProperty(example = "AW78461")
    @Size(min = 1, max = 10)
    @NotNull
    protected String systemId = null;

    @JsonProperty("_messageId")
    @ApiModelProperty(example = "3a15584b-6a7a-4ba2-8d27-3fea93c97b8s")
    @Size(min = 36, max = 255)
    @NotNull
    protected String messageId;

    @JsonProperty("_version")
    @ApiModelProperty(example = "1.0")
    @Size(min = 1, max = 20)
    @NotNull
    protected String version;

    @JsonProperty("_requestDate")
/*    @Pattern(regexp = ConstantsRegex.REGEX_REGISTRATION_DATE,
            message = "_requestDate tiene que corresponder a la expresión regular '${regexp}'")*/
    @ApiModelProperty(example = "20150625200000")
    @NotNull
    protected String requestDate;

    @JsonProperty("_service")
    @ApiModelProperty(example = "management")
    @Size(min = 1, max = 70)
    @NotNull
    protected String service;

    @JsonProperty("_operation")
    @ApiModelProperty(example = "start-acquisition")
    @Size(min = 1, max = 50)
    @NotNull
    protected String operation;
}
