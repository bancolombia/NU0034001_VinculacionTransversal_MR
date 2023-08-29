package co.com.bancolombia.logfunctionalvnt.log.usescases.checklist;

import java.util.UUID;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import co.com.bancolombia.logfunctionalvnt.log.jpa.LogFunctionalDataRepositoryAdapter;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;

public class LogFunctionalDataRepositoryAdapterTest {
	@InjectMocks
    @Spy
    private LogFunctionalDataRepositoryAdapter logFunctionalDataRepositoryAdapter;

	
	
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    
    }
    
    final Acquisition acquisition = Acquisition.builder()
            .id(UUID.randomUUID())
            .documentNumber("123456789")
            .build();

    
    //@Test
    public void getCheckListByAcquisition_test() {
    	LogFunctionalDTO logFunctionalDTO = LogFunctionalDTO.builder()
    			.build();
    	this.logFunctionalDataRepositoryAdapter.save(logFunctionalDTO);
    }
	
	
}
