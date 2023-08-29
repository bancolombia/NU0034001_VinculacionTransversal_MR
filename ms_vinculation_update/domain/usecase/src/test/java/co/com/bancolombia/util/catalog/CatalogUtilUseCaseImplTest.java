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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class CatalogUtilUseCaseImplTest {

    @InjectMocks
    @Spy
    private CatalogUtilUseCaseImpl catalogUtilUseCase;

    @Mock
    private BusinessLineUseCase businessLineUseCase;

    @Mock
    private TypeChannelUseCase typeChannelUseCase;

    @Mock
    private DocumentTypeUseCase documentTypeUseCase;

    @Mock
    private TypeProductUseCase typeProductUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateCatalogsTest() {
        AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder()
                .businessLine("001").typeChannel("001").documentType("001").typeProduct("001").build();
        List<ErrorField> errorFields = new ArrayList<>();
        Optional<BusinessLine> businessLine = Optional.of(BusinessLine.builder().code("").build());
        Optional<TypeChannel> typeChannel = Optional.of(TypeChannel.builder().code("").build());
        Optional<DocumentType> documentType = Optional.of(DocumentType.builder().code("").build());
        Optional<TypeProduct> typeProduct = Optional.of(TypeProduct.builder().code("").build());
        Mockito.doReturn(businessLine).when(businessLineUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeChannel).when(typeChannelUseCase).findByCode(any(String.class));
        Mockito.doReturn(documentType).when(documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeProduct).when(typeProductUseCase).findByCode(any(String.class));
        Mockito.doReturn(errorFields).when(businessLineUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(typeChannelUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(documentTypeUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(typeProductUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(businessLineUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFields).when(typeChannelUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFields).when(documentTypeUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFields).when(typeProductUseCase).validateActive(any(Optional.class));

        catalogUtilUseCase.validateCatalogs(acquisitionRequestModel);
        assertEquals(0, errorFields.size());
    }

    @Test(expected = ValidationException.class)
    public void validateCatalogsErrorTest() {
        AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder()
                .businessLine("001").typeChannel("001").documentType("001").typeProduct("001").build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().name("error").build());
        Optional<BusinessLine> businessLine = Optional.of(BusinessLine.builder().code("").build());
        Optional<TypeChannel> typeChannel = Optional.of(TypeChannel.builder().code("").build());
        Optional<DocumentType> documentType = Optional.of(DocumentType.builder().code("").build());
        Optional<TypeProduct> typeProduct = Optional.of(TypeProduct.builder().code("").build());
        Mockito.doReturn(businessLine).when(businessLineUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeChannel).when(typeChannelUseCase).findByCode(any(String.class));
        Mockito.doReturn(documentType).when(documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeProduct).when(typeProductUseCase).findByCode(any(String.class));
        Mockito.doReturn(errorFields).when(businessLineUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(typeChannelUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(documentTypeUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(typeProductUseCase).validate(any(String.class), any(Optional.class));

        catalogUtilUseCase.validateCatalogs(acquisitionRequestModel);
    }

    @Test(expected = ValidationException.class)
    public void validateCatalogsErrorInactiveTest() {
        AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder()
                .businessLine("001").typeChannel("001").documentType("001").typeProduct("001").build();
        List<ErrorField> errorFields = new ArrayList<>();
        List<ErrorField> errorFieldsInactive = Collections.singletonList(ErrorField.builder().name("error").build());
        Optional<BusinessLine> businessLine = Optional.of(BusinessLine.builder().code("").build());
        Optional<TypeChannel> typeChannel = Optional.of(TypeChannel.builder().code("").build());
        Optional<DocumentType> documentType = Optional.of(DocumentType.builder().code("").build());
        Optional<TypeProduct> typeProduct = Optional.of(TypeProduct.builder().code("").build());
        Mockito.doReturn(businessLine).when(businessLineUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeChannel).when(typeChannelUseCase).findByCode(any(String.class));
        Mockito.doReturn(documentType).when(documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeProduct).when(typeProductUseCase).findByCode(any(String.class));
        Mockito.doReturn(errorFields).when(businessLineUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(typeChannelUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(documentTypeUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(typeProductUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFieldsInactive).when(businessLineUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFieldsInactive).when(typeChannelUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFieldsInactive).when(documentTypeUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFieldsInactive).when(typeProductUseCase).validateActive(any(Optional.class));

        catalogUtilUseCase.validateCatalogs(acquisitionRequestModel);
    }

    @Test
    public void transformObjectCatalogTest() {
        DocumentType documentType = DocumentType.builder().code("001").build();
        TypeChannel typeChannel = TypeChannel.builder().code("001").build();
        TypeProduct typeProduct = TypeProduct.builder().code("001").build();
        BusinessLine businessLine = BusinessLine.builder().code("001").build();
        AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder()
                .businessLine(businessLine.getCode()).typeChannel(typeChannel.getCode())
                .typeProduct(typeProduct.getCode()).documentType(documentType.getCode()).build();

        Mockito.doReturn(Optional.of(businessLine)).when(businessLineUseCase).findByCode(any(String.class));
        Mockito.doReturn(Optional.of(typeChannel)).when(documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(Optional.of(documentType)).when(documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(Optional.of(typeProduct)).when(typeProductUseCase).findByCode(any(String.class));

        AcquisitionStartObjectModel aSom = catalogUtilUseCase.transformObjectCatalog(acquisitionRequestModel);
        assertNotNull(aSom);
    }
}
