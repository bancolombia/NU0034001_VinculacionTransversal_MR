package co.com.bancolombia.foreigninformation.model;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ForeignCurrencyInfoRequestData extends UserInfoRequestData {

    @JsonProperty("foreignCurrencyList")
    @ApiModelProperty(value = "")
    private List<ForeignCurrencyInfoRequestDataList> foreignCurrencyInfoRequestDataList;

    @JsonProperty("foreignCurrencyTransactions")
    @ApiModelProperty(value = "RESPUE_S")
    @Size(min = 1, max = 8, groups = ValidationMandatory.class)
    @NotNull(groups = ValidationMandatory.class)
    private String foreignCurrencyTransactions;
}