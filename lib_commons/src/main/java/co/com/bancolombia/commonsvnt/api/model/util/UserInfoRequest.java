package co.com.bancolombia.commonsvnt.api.model.util;

import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * UserInfoRequest
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserInfoRequest extends Request {
    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull(groups = {ValidationMandatory.class})
    @Valid
    private UserInfoRequestData data;

    public UserInfoRequest(MetaRequest metaRequest, UserInfoRequestData data) {
        super(metaRequest);
        this.data = data;
    }
}
