package co.com.bancolombia.digitalizationprocesseddocuments;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.digitalizationprocesseddocuments.DigitalizationProcessedDocuments;
import co.com.bancolombia.model.digitalizationprocesseddocuments.gateways.DigitalizationProcessedDocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class DigitalizationProcessedDocumentsUseCaseTest {

    @InjectMocks
    @Spy
    private DigitalizationProcessedDocumentsUseCaseImpl useCase;

    @Mock
    private DigitalizationProcessedDocumentsRepository repository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void saveTest() {
        DigitalizationProcessedDocuments digitalization = DigitalizationProcessedDocuments.builder().build();
        doReturn(digitalization).when(repository).save(any(DigitalizationProcessedDocuments.class));

        useCase.save(digitalization);
        verify(repository, times(1)).save(any(DigitalizationProcessedDocuments.class));
    }

    @Test
    public void findByAcquisitionTest() {
        DigitalizationProcessedDocuments dpd = DigitalizationProcessedDocuments.builder().build();
        doReturn(dpd).when(repository).findByAcquisition(any(Acquisition.class));

        Acquisition acquisition = Acquisition.builder().id(uuid).build();
        Optional<DigitalizationProcessedDocuments> digitalization = useCase.findByAcquisition(acquisition);
        assertTrue(digitalization.isPresent());
    }

    @Test
    public void findLastDigitalizationProcessedDocumentsTest() {
        DigitalizationProcessedDocuments dpd = DigitalizationProcessedDocuments.builder().build();
        doReturn(dpd).when(repository).findLastDigitalizationProcessedDocuments(
                any(Acquisition.class), anyString(), anyString());

        Acquisition acquisition = Acquisition.builder().id(uuid).build();
        Optional<DigitalizationProcessedDocuments> digitalization = useCase.findLastDigitalizationProcessedDocuments(
                acquisition, "01", "001");
        assertTrue(digitalization.isPresent());
    }
}
