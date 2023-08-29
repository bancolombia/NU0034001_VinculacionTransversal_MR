package co.com.bancolombia.logfunctionalvnt.log.usescases.checklist;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.LogFuncAcquisitionUseCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

public class AcquisitionUseCaseTest {
	@InjectMocks
    @Spy
    private LogFuncAcquisitionUseCase logFuncAcquisitionUseCase;

	
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    
    }
    
    final Acquisition acquisition = Acquisition.builder()
            .id(UUID.randomUUID())
            .documentNumber("123456789")
            .build();

    
    @Test
    public void findById_test() {
        this.logFuncAcquisitionUseCase.findById(UUID.randomUUID());
    }

    @Test
    public void findByIdAndDocumentTypeAndDocumentNumber_test() {
        this.logFuncAcquisitionUseCase.findByIdAndDocumentTypeAndDocumentNumber(UUID.randomUUID(),
                "123456789", "FS001");
    }

    @Test
    public void findByIdWitOutState_test() {
        this.logFuncAcquisitionUseCase.findByIdWitOutState(UUID.randomUUID());
    }
}
