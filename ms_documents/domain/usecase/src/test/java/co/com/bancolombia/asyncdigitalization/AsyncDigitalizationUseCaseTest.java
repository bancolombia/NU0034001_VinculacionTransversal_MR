package co.com.bancolombia.asyncdigitalization;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.asyncdigitalization.gateways.AsyncDigitalizationRepository;
import co.com.bancolombia.model.sqs.SqsMessage;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class AsyncDigitalizationUseCaseTest {

    @InjectMocks
    @Spy
    private AsyncDigitalizationUseCaseImpl asyncDigitalizationUseCase;

    @Mock
    private AsyncDigitalizationRepository asyncDigitalizationRepository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void saveTest() {
        AsyncDigitalization asyncDigitalization = AsyncDigitalization.builder().build();
        doReturn(asyncDigitalization).when(asyncDigitalizationRepository).save(any(AsyncDigitalization.class));

        asyncDigitalizationUseCase.save(asyncDigitalization);
        verify(asyncDigitalizationRepository, times(1)).save(any(AsyncDigitalization.class));
    }

    @Test
    public void findByAcquisitionTest() {
        AsyncDigitalization ad = AsyncDigitalization.builder().build();
        doReturn(ad).when(asyncDigitalizationRepository).findByAcquisition(any(Acquisition.class));

        Acquisition acquisition = Acquisition.builder().id(uuid).build();
        Optional<AsyncDigitalization> async = asyncDigitalizationUseCase.findByAcquisition(acquisition);
        assertTrue(async.isPresent());
    }

    @Test
    public void findBySqsMessageTest() {
        AsyncDigitalization ad = AsyncDigitalization.builder().build();
        doReturn(ad).when(asyncDigitalizationRepository).findBySqsMessage(any(SqsMessage.class));

        SqsMessage sqsMessage = SqsMessage.builder().build();
        Optional<AsyncDigitalization> async = asyncDigitalizationUseCase.findBySqsMessage(sqsMessage);
        assertTrue(async.isPresent());
    }
}
