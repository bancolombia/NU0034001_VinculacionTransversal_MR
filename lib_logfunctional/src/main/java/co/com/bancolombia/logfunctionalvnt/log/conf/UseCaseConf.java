package co.com.bancolombia.logfunctionalvnt.log.conf;

import co.com.bancolombia.logfunctionalvnt.log.jpa.LogFunctionalDataRepositoryAdapter;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.LogFuncLogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.LogFuncLogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.LogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConf {
    /**
     For Log Functional
     */

    @Bean
    public ILogFuncAcquisitionUseCase createModuleAcquisitionUseCase(
            LogFunctionalDataRepositoryAdapter logFunctionalDataRepositoryAdapter){

        return new LogFuncAcquisitionUseCase(logFunctionalDataRepositoryAdapter);
    }

    @Bean
    public ILogFuncFieldUseCase createModuleFieldUseCase(){
        return new LogFuncLogFuncFieldUseCase();
    }

    @Bean
    public ILogFuncCheckListUseCase createModuleCheckListUseCase(){
        return new LogFuncLogFuncCheckListUseCase();
    }




    /**
     For Log Functional
     */
}
