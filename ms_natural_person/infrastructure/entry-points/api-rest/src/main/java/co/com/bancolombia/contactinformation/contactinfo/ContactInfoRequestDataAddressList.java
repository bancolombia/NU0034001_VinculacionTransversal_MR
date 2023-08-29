package co.com.bancolombia.contactinformation.contactinfo;

import co.com.bancolombia.commonsvnt.api.validations.ValidationInvalidInputList;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatoryInputList;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.usecase.util.ValidationJsonFields;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STRINGS_ADDRESS_ERROR;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ContactInfoRequestDataAddressList {
    @JsonProperty("addressType")
    @ApiModelProperty(example = "CLASED_Z001", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 1, max = 11, groups = ValidationInvalidInputList.class)
    private String addressType;

    @JsonProperty("brand")
    @ApiModelProperty(example = "MARCAC_XXDEFAULT", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 1, max = 16, groups = ValidationInvalidInputList.class)
    private String brand;

    @JsonProperty("companyName")
    @ApiModelProperty(example = "Google", value = "")
    @Size(min = 1, max = 35, groups = ValidationOptional.class)
    @Pattern(regexp = ConstantsRegex.REGEX_COMPANY_NAME, groups = ValidationOptional.class)
    private String companyName;

    @JsonProperty("address")
    @ApiModelProperty(example = "Calle 4 # 23 -11", value = "")
    @Size(min = 5, max = 60, groups = ValidationOptional.class)
    @Pattern(regexp = ConstantsRegex.REGEX_ADDRESS, groups = ValidationOptional.class)
    private String address;

    @JsonProperty("neighborhood")
    @ApiModelProperty(example = "Poblado", value = "")
    @Size(min = 1, max = 40, groups = ValidationOptional.class)
    @Pattern(regexp = ConstantsRegex.REGEX_NEIGHBORHOOD, groups = ValidationOptional.class)
    private String neighborhood;

    @JsonProperty("city")
    @ApiModelProperty(example = "CIUDAD_05001000", value = "")
    @Size(min = 1, max = 15, groups = ValidationOptional.class)
    private String city;

    @JsonProperty("department")
    @ApiModelProperty(example = "DPTO_CO_000005", value = "")
    @Size(min = 1, max = 14, groups = ValidationOptional.class)
    private String department;

    @JsonProperty("country")
    @ApiModelProperty(example = "PAIS_CO", value = "")
    @Size(min = 1, max = 7, groups = ValidationOptional.class)
    private String country;

    @JsonProperty("phone")
    @ApiModelProperty(example = "67854321", value = "")
    @Size(min = 7, max = 30, groups = ValidationOptional.class)
    @Pattern(regexp = ConstantsRegex.REGEX_PHONE, groups = ValidationOptional.class)
    private String phone;

    @ApiModelProperty(example = "3001239873", value = "")
    @Size(min = 10, max = 30, groups = ValidationOptional.class)
    @Pattern(regexp = ConstantsRegex.REGEX_CELLPHONE
            , groups = ValidationOptional.class)
    @JsonProperty("cellphone")
    private String cellphone;

    @JsonProperty("ext")
    @ApiModelProperty(example = "4329", value = "")
    @Size(min = 1, max = 10, groups = ValidationOptional.class)
    @Pattern(regexp = ConstantsRegex.REGEX_EXT, groups = ValidationOptional.class)
    private String ext;

    @ApiModelProperty(example = "user@test.com", value = "")
    @Size(min = 1, max = 241, groups = ValidationOptional.class)
    @Pattern(regexp = ConstantsRegex.REGEX_EMAIL, groups = ValidationOptional.class)
    @JsonProperty("email")
    private String email;

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "Validaci�n direcci�n: 'SIN INFORMACION', 'SIN DIRECCION', 'NO REGISTRA'"
            , groups = ValidationOptional.class)
    private boolean isAddress() {
        if (this.address != null && this.address.matches(ConstantsRegex.REGEX_ADDRESS)) {
            this.address = this.address.toUpperCase().replace(" ", "");
            return !(STRINGS_ADDRESS_ERROR.contains(this.address));
        }

        return true;
    }

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "Validate email: SININFORMACION', 'NOINFORMA', 'SINCORREO"
            , groups = ValidationOptional.class)
    private boolean isEmail() {
        return ValidationJsonFields.validationEmail(this.email);
    }

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "validate CompanyName injection HTML"
            , groups = ValidationOptional.class)
    private boolean isCompanyName() {
        return ValidationJsonFields.validationInjectionHTML(this.companyName);
    }

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "the field phone is not null", groups = ValidationOptional.class)
    private boolean isPhone() {
        return (this.ext == null || this.phone != null);
    }
}
