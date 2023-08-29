package co.com.bancolombia.acquisition;

import co.com.bancolombia.bussinesline.BusinessLineUseCaseImpl;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import co.com.bancolombia.documenttype.DocumentTypeUseCaseImpl;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.typechannel.TypeChannelUseCaseImpl;
import co.com.bancolombia.typeproduct.TypeProductUseCaseImpl;
import co.com.bancolombia.util.catalog.CatalogUtilUseCaseImpl;
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

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class CatalogUtilUseCaseImplTest {

    @InjectMocks
    @Spy
    private CatalogUtilUseCaseImpl catalogUtilUseCase;

    @Mock
    private BusinessLineUseCaseImpl businessLineUseCase;

    @Mock
    private TypeChannelUseCaseImpl typeChannelUseCase;

    @Mock
    private DocumentTypeUseCaseImpl documentTypeUseCase;

    @Mock
    private TypeProductUseCaseImpl typeProductUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateCatalogs() {
        AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder().businessLine("001").typeChannel("001").documentType("001").typeProduct("001").build();
        List<ErrorField> errorFields = new ArrayList<>();
        Optional<BusinessLine> businessLine = Optional.of(BusinessLine.builder().code("").build());
        Optional<TypeChannel> typeChannel = Optional.of(TypeChannel.builder().code("").build());
        Optional<DocumentType> documentType = Optional.of(DocumentType.builder().code("").build());
        Optional<TypeProduct> typeProduct = Optional.of(TypeProduct.builder().code("").build());
        Mockito.doReturn(businessLine).when(this.businessLineUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeChannel).when(this.typeChannelUseCase).findByCode(any(String.class));
        Mockito.doReturn(documentType).when(this.documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeProduct).when(this.typeProductUseCase).findByCode(any(String.class));
        Mockito.doReturn(errorFields).when(this.businessLineUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.typeChannelUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.documentTypeUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.typeProductUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.businessLineUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFields).when(this.typeChannelUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFields).when(this.documentTypeUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFields).when(this.typeProductUseCase).validateActive(any(Optional.class));
        this.catalogUtilUseCase.validateCatalogs(acquisitionRequestModel);
    }

    @Test(expected = ValidationException.class)
    public void validateCatalogsErrorTest() {
        AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder().businessLine("001").typeChannel("001").documentType("001").typeProduct("001").build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().name("error").build());
        Optional<BusinessLine> businessLine = Optional.of(BusinessLine.builder().code("").build());
        Optional<TypeChannel> typeChannel = Optional.of(TypeChannel.builder().code("").build());
        Optional<DocumentType> documentType = Optional.of(DocumentType.builder().code("").build());
        Optional<TypeProduct> typeProduct = Optional.of(TypeProduct.builder().code("").build());
        Mockito.doReturn(businessLine).when(this.businessLineUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeChannel).when(this.typeChannelUseCase).findByCode(any(String.class));
        Mockito.doReturn(documentType).when(this.documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeProduct).when(this.typeProductUseCase).findByCode(any(String.class));
        Mockito.doReturn(errorFields).when(this.businessLineUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.typeChannelUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.documentTypeUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.typeProductUseCase).validate(any(String.class), any(Optional.class));

        this.catalogUtilUseCase.validateCatalogs(acquisitionRequestModel);
    }

    @Test(expected = ValidationException.class)
    public void validateCatalogsErrorInactiveTest() {
        AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder().businessLine("001").typeChannel("001").documentType("001").typeProduct("001").build();
        List<ErrorField> errorFields = new ArrayList<>();
        List<ErrorField> errorFieldsInactive = Collections.singletonList(ErrorField.builder().name("error").build());
        Optional<BusinessLine> businessLine = Optional.of(BusinessLine.builder().code("").build());
        Optional<TypeChannel> typeChannel = Optional.of(TypeChannel.builder().code("").build());
        Optional<DocumentType> documentType = Optional.of(DocumentType.builder().code("").build());
        Optional<TypeProduct> typeProduct = Optional.of(TypeProduct.builder().code("").build());
        Mockito.doReturn(businessLine).when(this.businessLineUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeChannel).when(this.typeChannelUseCase).findByCode(any(String.class));
        Mockito.doReturn(documentType).when(this.documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(typeProduct).when(this.typeProductUseCase).findByCode(any(String.class));
        Mockito.doReturn(errorFields).when(this.businessLineUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.typeChannelUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.documentTypeUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFields).when(this.typeProductUseCase).validate(any(String.class), any(Optional.class));
        Mockito.doReturn(errorFieldsInactive).when(this.businessLineUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFieldsInactive).when(this.typeChannelUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFieldsInactive).when(this.documentTypeUseCase).validateActive(any(Optional.class));
        Mockito.doReturn(errorFieldsInactive).when(this.typeProductUseCase).validateActive(any(Optional.class));

        this.catalogUtilUseCase.validateCatalogs(acquisitionRequestModel);
    }

    @Test
    public void transformObjectCatalog() {
        DocumentType documentType = DocumentType.builder().code("001").build();
        TypeChannel typeChannel = TypeChannel.builder().code("001").build();
        TypeProduct typeProduct = TypeProduct.builder().code("001").build();
        BusinessLine businessLine = BusinessLine.builder().code("001").build();
        AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder().businessLine(businessLine.getCode())
                .typeChannel(typeChannel.getCode()).typeProduct(typeProduct.getCode()).documentType(documentType.getCode()).build();

        Mockito.doReturn(Optional.of(businessLine)).when(this.businessLineUseCase).findByCode(any(String.class));
        Mockito.doReturn(Optional.of(typeChannel)).when(this.documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(Optional.of(documentType)).when(this.documentTypeUseCase).findByCode(any(String.class));
        Mockito.doReturn(Optional.of(typeProduct)).when(this.typeProductUseCase).findByCode(any(String.class));

        AcquisitionStartObjectModel aSom = this.catalogUtilUseCase.transformObjectCatalog(acquisitionRequestModel);
        assertNotNull(aSom);
    }
}

