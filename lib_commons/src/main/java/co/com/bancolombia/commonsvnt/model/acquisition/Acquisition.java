package co.com.bancolombia.commonsvnt.model.acquisition;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Acquisition extends Representative {
    private UUID id;
    private DocumentType documentType;
    private String documentNumber;
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private TypePerson typePerson;
    private TypeProduct typeProduct;
    private TypeChannel typeChannel;
    private BusinessLine businessLine;
    private TypeAcquisition typeAcquisition;
    private String email;
    private String cellPhone;
    private StateAcquisition stateAcquisition;
    private Boolean suitable;
    private Integer uploadDocumentRetries;
    private Integer uploadRutRetries;
    private Integer signDocRetries;
    private InfoReuseCommon infoReuse;

    public String getFullName() {
        return Stream.of(this.firstName, this.secondName, this.firstSurname, this.secondSurname)
                .filter(value -> value != null && !value.isEmpty()).collect(Collectors.joining(" "));
    }

    public String getFullNameInverse() {
        return Stream.of(this.firstSurname, this.secondSurname, this.firstName, this.secondName)
                .filter(value -> value != null && !value.isEmpty()).collect(Collectors.joining(" "));
    }
}
