package co.com.bancolombia.model.matrixtypeacquisition;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class MatrixTypeAcquisition extends Auditing {
    private UUID id;
    private DocumentType documentType;
    private TypePerson typePerson;
    private TypeProduct typeProduct;
    private TypeChannel typeChannel;
    private BusinessLine businessLine;
    private TypeAcquisition typeAcquisition;

}
