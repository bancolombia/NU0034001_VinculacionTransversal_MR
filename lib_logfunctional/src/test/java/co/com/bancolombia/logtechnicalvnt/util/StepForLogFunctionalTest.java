package co.com.bancolombia.logtechnicalvnt.util;

import co.com.bancolombia.commonsvnt.api.model.util.Meta;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.jpa.LogFunctionalDataRepositoryAdapter;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.LogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.LogFuncLogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.LogFuncLogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.model.StepForLogClass;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.springframework.web.context.request.WebRequest;

import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class StepForLogFunctionalTest {
    @InjectMocks
    @Spy
    private StepForLogFunctional stepForLogFunctional;
    
    @Mock
    private WebRequest webRequest;

    @Mock
    private LogFuncAcquisitionUseCase logFuncAcquisitionUseCase;
    
    @Mock
    private LogFuncLogFuncCheckListUseCase logFuncCheckListUseCase;
    
    @Mock
    private LogFuncLogFuncFieldUseCase logFuncFieldUseCase;
    
    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private LogFunctionalDataRepositoryAdapter logFunctionalDataRepositoryAdapter;
    
    private StepForLogClass stepForLogClass;
    private Acquisition acquisition;
    private StateAcquisition stateAcquisition; 
    private CheckList checkList;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        
        Map<String , String> mapaField = new HashMap<>();
    	mapaField.put("KEY", "value");
    	
    	checkList = CheckList.builder()
    			.step(Step.builder()
    					.code("code")
    					.name("name")
    					.build())
    			.build();
    	
    	stateAcquisition = StateAcquisition.builder()
    			.name("STATE")
    			.code("CODE")
    			.build();

    	acquisition = Acquisition.builder()
    			.id(UUID.randomUUID())
    			.stateAcquisition(stateAcquisition)
    			.build();
    	
    	stepForLogClass = StepForLogClass.builder()
    			.idAcquisition(acquisition.getId())
    			.mapaField(mapaField)
                .build();

    
    }

    @Test
    public void continueValidation_test_isnull(){
    	stepForLogClass.setMapaField(null);
    	this.stepForLogFunctional.continueValidation(stepForLogClass);
    	Mockito.verify(this.stepForLogFunctional, Mockito.times(1)).continueValidation(stepForLogClass);
    }

    @Test
    public void continueValidation_test_isnotnull(){
    	
    	
    	this.stepForLogFunctional.continueValidation(stepForLogClass);
    	Mockito.verify(this.stepForLogFunctional, Mockito.times(1)).continueValidation(stepForLogClass);
    }
    

    //@Test
    public void finallyStepForLogFunctional_old_test_no_acquisition() {
    	acquisition.setId(null);
    	this.stepForLogFunctional.finallyStepForLogFunctional_old(stepForLogClass);
    }

    
    @Test
    public void finallyStepForLogFunctional_old_test() {
    	doReturn(acquisition).when(this.logFuncAcquisitionUseCase).findByIdWitOutState(acquisition.getId());
    	doReturn(Arrays.asList(checkList)).when(this.logFuncCheckListUseCase).getCheckListByAcquisition(acquisition);
    	this.stepForLogFunctional.finallyStepForLogFunctional_old(stepForLogClass);
    }

    @Test
    public void finallyStepForLogFunctional_test() {
        doReturn(acquisition).when(this.logFuncAcquisitionUseCase).findByIdWitOutState(acquisition.getId());
        doReturn(Arrays.asList(checkList)).when(this.logFuncCheckListUseCase).getCheckListByAcquisition(acquisition);
        doReturn(AcquisitionStateReply.builder()
                .stateAcquisitionCode("ACTIVO")
                .stateStepCode("COMPLETADO")
                .build()).when(this.logFunctionalDataRepositoryAdapter).acquisitionStateReply(
                AcquisitionStateQuery.builder()
                        .acquisitionId(UUID.randomUUID().toString())
                        .operation("OPERATION")
                        .build()
        );
        this.stepForLogFunctional.finallyStepForLogFunctional(stepForLogClass);
    }


    @Test
    public void firstStepForLogFunctional_old_test() {
    	AcquisitionInitial acquisitionInitial = AcquisitionInitial.builder()
    			.build();  
    	
    	acquisitionInitial.setData(MyDataInitial.builder()
				.acquisitionId(acquisition.getId().toString())
				.build());
    	
    	acquisitionInitial.setMeta(new MetaRequest());
    	
    	this.stepForLogFunctional.firstStepForLogFunctional("OPERATIONS", acquisitionInitial);
    }

    @Test
    public void firstStepForLogFunctional_old_Meta_test() {
        AcquisitionInitial acquisitionInitial = AcquisitionInitial.builder()
                .build();

        acquisitionInitial.setData(MyDataInitial.builder()
                .acquisitionId(acquisition.getId().toString())
                .build());

        acquisitionInitial.setMeta(new Meta());

        this.stepForLogFunctional.firstStepForLogFunctional("OPERATIONS", acquisitionInitial);
    }
    
    
}
