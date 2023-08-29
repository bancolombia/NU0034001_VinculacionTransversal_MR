package co.com.bancolombia.config;

import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_SQS_MESSAGE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;

@Configuration
public class ResourcesConfig {

    @Bean(name = "adapterSqsMessageController")
    public LoggerAdapter createResourceAdapterSqsMessageController() {
        return new LoggerAdapter(MY_APP, SERVICE_MANAGEMENT, OPER_SQS_MESSAGE);
    }
}
