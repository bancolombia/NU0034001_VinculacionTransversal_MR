package co.com.bancolombia.model.validateidentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityItemsResponse {

    private String idType;
    private String dateConsulted;
    private NaturalLegalPerson naturalLegalPerson;
    private List<Addresses> addresses;
    private List<Phones> phones;
    private List<Mobiles> mobiles;
    private List<Emails> emails;
    private List<Alerts> alerts;
}
