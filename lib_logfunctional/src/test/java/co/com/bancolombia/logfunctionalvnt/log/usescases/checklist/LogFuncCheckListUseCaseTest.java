package co.com.bancolombia.logfunctionalvnt.log.usescases.checklist;

import java.util.UUID;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class LogFuncCheckListUseCaseTest {
	@InjectMocks
    @Spy
    private LogFuncLogFuncCheckListUseCase logFuncCheckListUseCase;

	
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    
    }
    
    final Acquisition acquisition = Acquisition.builder()
            .id(UUID.randomUUID())
            .documentNumber("123456789")
            .build();

    
    @Test
    public void getCheckListByAcquisition_test() {
    	this.logFuncCheckListUseCase.getCheckListByAcquisition(acquisition);
    }
	
	
}
