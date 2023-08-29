package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class UploadDocumentRutModifyCiiuUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentRutModifyCiiuUseCaseImpl uploadDocumentRutModifyCiiuUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;

    @Mock
    private UploadDocumentValidateErrors uploadDocumentValidateErrors;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void modifyCiiuSuccessTest() {
        CatalogReply catalogReply = CatalogReply.builder().valid(true).code("CIIU_00000").build();
        EmptyReply emptyReply = EmptyReply.builder().valid(true).build();

        doReturn(catalogReply).when(vinculationUpdateUseCase).findCatalog(anyString(), anyString());
        doNothing().when(uploadDocumentValidateErrors).validateExceptionRutRetries(
                any(Acquisition.class), anyString(), anyString(), any(SqsMessageParamAllObject.class));
        doReturn(emptyReply).when(naturalPersonUseCase).updateCiiu(any(UUID.class), anyString(), anyString());

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        SqsMessageParamAllObject param = SqsMessageParamAllObject.builder().build();

        boolean b = uploadDocumentRutModifyCiiuUseCase.modifyCiiu("CIIU_00000", acquisition, "USRTRANS", param);
        assertTrue(b);
    }

    @Test
    public void modifyCiiuEmptyCatalogTest() {
        CatalogReply catalogReply = CatalogReply.builder().valid(false).code("CIIU_00000").build();
        EmptyReply emptyReply = EmptyReply.builder().valid(true).build();

        doReturn(catalogReply).when(vinculationUpdateUseCase).findCatalog(anyString(), anyString());
        doReturn(emptyReply).when(naturalPersonUseCase).updateCiiu(any(UUID.class), anyString(), anyString());

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        SqsMessageParamAllObject param = SqsMessageParamAllObject.builder().build();

        boolean b = uploadDocumentRutModifyCiiuUseCase.modifyCiiu("CIIU_00000", acquisition, "USRTRANS", param);
        assertTrue(b);
    }
}
