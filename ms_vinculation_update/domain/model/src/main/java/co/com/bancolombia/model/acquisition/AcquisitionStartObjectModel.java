package co.com.bancolombia.model.acquisition;

import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AcquisitionStartObjectModel {
    private DocumentType documentType;
    private String documentNumber;
    private TypePerson typePerson;
    private TypeProduct typeProduct;
    private TypeChannel typeChannel;
    private BusinessLine businessLine;
    private TypeAcquisition typeAcquisition;
}
