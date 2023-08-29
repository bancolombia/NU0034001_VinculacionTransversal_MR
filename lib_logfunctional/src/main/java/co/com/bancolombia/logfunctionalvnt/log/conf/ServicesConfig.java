package co.com.bancolombia.logfunctionalvnt.log.conf;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.interceptor.LogInterceptorUtil;
import co.com.bancolombia.logfunctionalvnt.log.model.gateways.ILogFunctionalRepository;
import co.com.bancolombia.logfunctionalvnt.log.service.ILogFunctionalService;
import co.com.bancolombia.logfunctionalvnt.log.service.LogFunctionalService;
import org.springframework.context.annotation.Bean;

public class ServicesConfig {

    @Bean
    public ILogFunctionalService createModuleLogFunctional(
            ILogFunctionalRepository logFunctionalRepository, CoreFunctionDate coreFunctionDate
    ) {
        return new LogFunctionalService(logFunctionalRepository, coreFunctionDate);
    }

    @Bean
    public LogInterceptorUtil createModuleLogInterceptorUtil(){
        return new LogInterceptorUtil();
    }
}
