package co.com.bancolombia.logfunctionalvnt.log.service;

import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;
import co.com.bancolombia.logfunctionalvnt.log.model.LogObjectAttribute;
import co.com.bancolombia.logfunctionalvnt.log.model.gateways.ILogFunctionalRepository;

import static org.mockito.Mockito.doReturn;

import com.google.gson.Gson;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class LogFunctionalServiceTest {
    @InjectMocks
    @Spy
    private LogFunctionalService logFunctionalService;
    
    @Mock
    private CoreFunctionDate coreFunctionDate;
    
    @Mock
    private ILogFunctionalRepository logFunctionalRepository;
    
    Gson gson;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gson = new Gson();
        
        Map<String , String> mapaField = new HashMap<>();
    	mapaField.put("KEY", "value");
    	
    
    }

    
    @Test
    public void save_test() {
    	
    	 
		AcquisitionInitial acquisitionInitial = AcquisitionInitial
				.builder()
				.data(MyDataInitial.builder()
						.acquisitionId(UUID.randomUUID().toString())
						.documentNumber("123456789")
						.documentType("001")
						.build())
				.build(); 
    			
		Map<String, List<String>> fieldNotSve = new HashMap<>();
		List<String> listString = new ArrayList<>();
		listString.add("LLAVE");
		
		fieldNotSve.put("KEY", listString);
		
		
		List<LogObjectAttribute> response = 
				Arrays.asList(LogObjectAttribute.builder()
						.fieldNotSve(fieldNotSve)
						.build());
		
		
    	LogFunctionalDTO logFunctional = LogFunctionalDTO.builder()
    			.request(gson.toJson(acquisitionInitial))
    			.response(gson.toJson(response))
    			.manangment("")
    			.acquisitionId("")
    			.documentNumber("")
    			.documentType("")
    			.operation("")
    			.dateInitOperation(coreFunctionDate.getDatetime())
    			.dateFinalOperation(coreFunctionDate.getDatetime())
    			.stateOperation("")
    			.stateAcquisition("")
    			.build();
    	
    	Map<String, List<String>> fieldsCanNotSave = new HashMap<String, List<String>>();
    	
    	ZoneId zoneId = ZoneId.of("America/Bogota"); 
    	
    	doReturn(zoneId).when(this.coreFunctionDate).getZoneId();
    	this.logFunctionalService.save(logFunctional, fieldsCanNotSave);
    }
    
    @Test
    public void save_test_null() {
    	

    	LogFunctionalDTO logFunctional = LogFunctionalDTO.builder()
    			.request("")
    			.response("")
    			.manangment("")
    			.acquisitionId("")
    			.documentNumber("")
    			.documentType("")
    			.operation("")
    			.dateInitOperation(coreFunctionDate.getDatetime())
    			.dateFinalOperation(coreFunctionDate.getDatetime())
    			.stateOperation("")
    			.stateAcquisition("")
    			.build();
    	
    	Map<String, List<String>> fieldsCanNotSave = null;
    	
    	this.logFunctionalService.save(logFunctional, fieldsCanNotSave);
    }


}
