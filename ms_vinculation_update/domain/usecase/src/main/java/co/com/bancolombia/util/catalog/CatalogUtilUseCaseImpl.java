package co.com.bancolombia.util.catalog;

import co.com.bancolombia.bussinesline.BusinessLineUseCase;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import co.com.bancolombia.documenttype.DocumentTypeUseCase;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.typechannel.TypeChannelUseCase;
import co.com.bancolombia.typeproduct.TypeProductUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_SIN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_INACTIVE_CATALOG;

@RequiredArgsConstructor
public class CatalogUtilUseCaseImpl implements CatalogUtilUseCase {

    private final BusinessLineUseCase businessLineUseCase;
    private final TypeChannelUseCase typeChannelUseCase;
    private final DocumentTypeUseCase documentTypeUseCase;
    private final TypeProductUseCase typeProductUseCase;

    @Override
    public void validateCatalogs(AcquisitionRequestModel acquisitionRequestModel) {
        List<ErrorField> error = new ArrayList<>();
        HashMap<String, List<ErrorField>> errorMap = new HashMap<>();
        Optional<BusinessLine> oBuLin = businessLineUseCase.findByCode(acquisitionRequestModel.getBusinessLine());
        Optional<TypeChannel> oTyCha = typeChannelUseCase.findByCode(acquisitionRequestModel.getTypeChannel());
        Optional<DocumentType> oTyDoc = documentTypeUseCase.findByCode(acquisitionRequestModel.getDocumentType());
        Optional<TypeProduct> oTyPro = typeProductUseCase.findByCode(acquisitionRequestModel.getTypeProduct());
        error.addAll(businessLineUseCase.validate(acquisitionRequestModel.getBusinessLine(), oBuLin));
        error.addAll(typeChannelUseCase.validate(acquisitionRequestModel.getTypeChannel(), oTyCha));
        error.addAll(documentTypeUseCase.validate(acquisitionRequestModel.getDocumentType(), oTyDoc));

        if (acquisitionRequestModel.getTypeProduct() != null) {
            error.addAll(typeProductUseCase.validate(acquisitionRequestModel.getTypeProduct(), oTyPro));
        }
        if (!error.isEmpty()) {
            errorMap.put(ERROR_CODE_CATALOG_SIN, error);
            throw new ValidationException(errorMap);
        }
        error.addAll(businessLineUseCase.validateActive(oBuLin));
        error.addAll(typeChannelUseCase.validateActive(oTyCha));
        error.addAll(documentTypeUseCase.validateActive(oTyDoc));
        if (acquisitionRequestModel.getTypeProduct() != null) {
            error.addAll(typeProductUseCase.validateActive(oTyPro));
        }
        if (!error.isEmpty()) {
            errorMap.put(ERROR_CODE_INACTIVE_CATALOG, error);
            throw new ValidationException(errorMap);
        }
    }

    @Override
    public AcquisitionStartObjectModel transformObjectCatalog(AcquisitionRequestModel acquisitionRequestModel) {
        Optional<DocumentType> oTyDoc = documentTypeUseCase.findByCode(acquisitionRequestModel.getDocumentType());
        DocumentType tyDoc = oTyDoc.orElse(DocumentType.builder().typePerson(null).build());

        Optional<TypeChannel> oTyCha = typeChannelUseCase.findByCode(acquisitionRequestModel.getTypeChannel());
        TypeChannel tyChannel = oTyCha.orElse(null);

        Optional<TypeProduct> oTyPro = typeProductUseCase.findByCode(acquisitionRequestModel.getTypeProduct());
        TypeProduct tyProduct = oTyPro.orElse(null);

        Optional<BusinessLine> oBuLin = businessLineUseCase.findByCode(acquisitionRequestModel.getBusinessLine());
        BusinessLine buLine = oBuLin.orElse(null);

        return AcquisitionStartObjectModel.builder().documentNumber(acquisitionRequestModel.getDocumentNumber())
                .documentType(tyDoc).typePerson(tyDoc.getTypePerson()).typeProduct(tyProduct).typeChannel(tyChannel)
                .businessLine(buLine).build();
    }
}
