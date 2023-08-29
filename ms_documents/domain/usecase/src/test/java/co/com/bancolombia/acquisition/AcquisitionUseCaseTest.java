package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.model.documentretries.DocumentRetries;
import co.com.bancolombia.model.sqs.SqsMessAcqObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessDataObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class AcquisitionUseCaseTest {

    @InjectMocks
    @Spy
    private AcquisitionUseCaseImpl acquisitionUseCase;

    @Mock
    private DocumentRetriesUseCase documentRetriesUseCase;

    private SqsMessObjUploadDoc objUploadDoc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        objUploadDoc = SqsMessObjUploadDoc.builder()
                .data(SqsMessDataObjUploadDoc.builder()
                        .acquisition(
                                SqsMessAcqObjUploadDoc.builder()
                                        .id(UUID.randomUUID().toString())
                                        .documentNumber("123456")
                                        .documentType("FS001")
                                        .build()
                        )
                        .build())
                .build();
    }

    @Test
    public void getAcquisitionPresentRetriesTest() {
        DocumentRetries retries = DocumentRetries.builder().uploadDocumentRetries(2).uploadRutRetries(2).build();
        doReturn(Optional.of(retries)).when(documentRetriesUseCase).findByAcquisition(any(Acquisition.class));

        Acquisition acquisition = acquisitionUseCase.getAcquisition(objUploadDoc);
        assertNotNull(acquisition);
    }

    @Test
    public void getAcquisitionEmptyRetriesTest() {
        doReturn(Optional.empty()).when(documentRetriesUseCase).findByAcquisition(any(Acquisition.class));

        Acquisition acquisition = acquisitionUseCase.getAcquisition(objUploadDoc);
        assertNotNull(acquisition);
    }
}
