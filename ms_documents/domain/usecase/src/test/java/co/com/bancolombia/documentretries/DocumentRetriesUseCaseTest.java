package co.com.bancolombia.documentretries;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.documentretries.DocumentRetries;
import co.com.bancolombia.model.documentretries.gateways.DocumentRetriesRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class DocumentRetriesUseCaseTest {

    @InjectMocks
    @Spy
    private DocumentRetriesUseCaseImpl documentRetriesUseCase;

    @Mock
    private DocumentRetriesRepository repository;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void savePresentTest() {
        DocumentRetries retries = DocumentRetries.builder().build();
        doReturn(retries).when(repository).findByAcquisition(any(Acquisition.class));
        doReturn(retries).when(repository).save(any(DocumentRetries.class));

        Acquisition acquisition = Acquisition.builder().id(uuid).build();
        DocumentRetries docRetries = documentRetriesUseCase.save(acquisition);
        assertNotNull(docRetries);
    }

    @Test
    public void saveNotPresentTest() {
        DocumentRetries retries = DocumentRetries.builder().build();
        doReturn(null).when(repository).findByAcquisition(any(Acquisition.class));
        doReturn(retries).when(repository).save(any(DocumentRetries.class));

        Acquisition acquisition = Acquisition.builder().id(uuid).build();
        DocumentRetries docRetries = documentRetriesUseCase.save(acquisition);
        assertNotNull(docRetries);
    }
}
