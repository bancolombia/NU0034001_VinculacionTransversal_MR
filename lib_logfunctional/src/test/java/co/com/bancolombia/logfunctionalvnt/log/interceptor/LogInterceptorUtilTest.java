package co.com.bancolombia.logfunctionalvnt.log.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.method.HandlerMethod;

import co.com.bancolombia.commonsvnt.util.ConstantLog;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;

import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;


public class LogInterceptorUtilTest {
    @InjectMocks
    @Spy
    private LogInterceptorUtil logInterceptorUtil;
    
    
    @Mock
    HttpServletRequest request;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void continueValidation_test_isnull(){
    	logInterceptorUtil.nada();
    	
    }
    
    @Test
    public void getReuseStr1_test() {
    	LogFunctionalReuse logFunctionalReuse = LogFunctionalReuse.builder()
    			
    			.build();
    	this.logInterceptorUtil.getReuseStr(logFunctionalReuse, ConstantLog.LOGFIELD_REQUEST);
    }
    
    @Test
    public void getReuseStr2_test() {
    	LogFunctionalReuse logFunctionalReuse = LogFunctionalReuse.builder()
    			
    			.build();
    	this.logInterceptorUtil.getReuseStr(logFunctionalReuse, ConstantLog.LOGFIELD_RESPONSE);
    }

    @Test
    public void getReuseStr3_test() {
    	LogFunctionalReuse logFunctionalReuse = LogFunctionalReuse.builder()
    			
    			.build();
    	this.logInterceptorUtil.getReuseStr(logFunctionalReuse, "");
    }

    @Test
    public void getReuseStr4_test() {
    	LogFunctionalReuse logFunctionalReuse = null;
    	this.logInterceptorUtil.getReuseStr(logFunctionalReuse, "");
    }
    
    //@Test
    public void getLogFunctionalDTO_test() {
    	
    	HandlerMethod mockHandler = mock(HandlerMethod.class);
    	
    	ParameterLogFunctional paLoFo = ParameterLogFunctional.builder()
    			.logFunctionalDTO(LogFunctionalDTO.builder()
    					.request("")
    					.build())
    			//.logRegister(mockHandler)
    			.request(request)
    			.build();
    	this.logInterceptorUtil.getLogFunctionalDTO(paLoFo);
    }
    
}
