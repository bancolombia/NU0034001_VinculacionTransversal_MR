package co.com.bancolombia.contactinformation.contactinfo;

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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ContactInfoRequestData
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ContactInfoRequestData extends UserInfoRequestData {

    @JsonProperty("addressList")
    @ApiModelProperty(value = "")
    @NotNull(groups = ValidationMandatory.class)
    @NotEmpty(groups = ValidationMandatory.class)
    @Valid
    private List<ContactInfoRequestDataAddressList> contactInfoRequestDataAddressListList;
}
