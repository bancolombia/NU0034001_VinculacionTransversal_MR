package co.com.bancolombia.config;


import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.TriggerExceptionUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.TriggerExceptionUseCaseImpl;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCaseImpl;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCaseTwo;
import co.com.bancolombia.usecase.validatelegalrep.ValidateLegalRepUseCase;
import co.com.bancolombia.usecase.validatelegalrep.ValidateLegalRepUseCaseImp;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public CoreFunctionDate createModuleCoreFunctionDate() {
        return new CoreFunctionDate();
    }

    @Bean
    public TriggerExceptionUseCase triggerExceptionUseCase() {
        return new TriggerExceptionUseCaseImpl();
    }

    @Bean
    public VinculationUpdateUseCase vinculationUpdateUseCase(
            DirectAsyncGateway directAsyncGateway, TriggerExceptionUseCase triggerExceptionUseCase,
            VinculationUpdateUseCaseTwo vinculationUpdateUseCaseTwo) {
        return new VinculationUpdateUseCaseImpl(directAsyncGateway, triggerExceptionUseCase,
                vinculationUpdateUseCaseTwo);
    }

    @Bean
    public VinculationUpdateUseCaseTwo vinculationUpdateUseCaseTwo(
            DirectAsyncGateway directAsyncGateway, TriggerExceptionUseCase triggerExceptionUseCase){
        return new VinculationUpdateUseCaseTwo(directAsyncGateway, triggerExceptionUseCase);
    }

    @Bean
    public ValidateLegalRepUseCase validateLegalRepUseCase(){
        return new ValidateLegalRepUseCaseImp();
    }
}

