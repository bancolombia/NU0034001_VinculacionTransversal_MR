package co.com.bancolombia.config;

import co.com.bancolombia.common.secretsmodel.gateways.SecretsManagerConsumer;
import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;
import co.com.bancolombia.commonsvnt.model.secretsmodel.SecretModelRabbit;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.secrets.SecretsManager;
import com.rabbitmq.client.ConnectionFactory;
import org.reactivecommons.async.rabbit.config.ConnectionFactoryProvider;
import org.reactivecommons.async.rabbit.config.RabbitProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_ERROR_API;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;

@Configuration
public class RabbitConfig {

    @Value("${rabbit.host}")
    private String host;

    @Value("${rabbit.port}")
    private int port;

    @Value("${rabbit.secret.region}")
    private String secretRegion;

    @Value("${rabbit.secret.name}")
    private String secretName;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, "CONNECTION");

    private void configureSsl(ConnectionFactory connectionFactory) {
        try {
            connectionFactory.useSslProtocol();
        } catch (NoSuchAlgorithmException | KeyManagementException exception) {
            adapter.error(SI_ERROR_API, exception);
        }
    }

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public SecretModelRabbit getCredentialRabbit(SecretsManagerConsumer<SecretModelRabbit> consumer)
            throws SecretException {
        SecretsManager secretsManager = new SecretsManager();
        return (SecretModelRabbit) secretsManager.getSecrets(SecretModelRabbit.class,
                this.secretRegion, this.secretName);
    }

    @Bean
    @Profile({"dev-local", "test"})
    public SecretModelRabbit getCredentialRabbitTest() {
        return SecretModelRabbit.builder()
                .username(secretRegion)
                .password(secretName)
                .build();
    }

    @Bean
    @Primary
    public RabbitProperties rabbitProperties(SecretModelRabbit model){
        RabbitProperties rabbitProperties = new RabbitProperties();
        rabbitProperties.setHost(host);
        rabbitProperties.setPort(port);
        rabbitProperties.setUsername(model.getUsername());
        rabbitProperties.setPassword(model.getPassword());
        return rabbitProperties;
    }

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public ConnectionFactoryProvider connection(RabbitProperties rabbitProperties) {
        ConnectionFactory connectionFactory = connectionGlobal(rabbitProperties);
        configureSsl(connectionFactory);
        return () -> connectionFactory;
    }

    @Bean
    @Profile({"dev-local", "test"})
    public ConnectionFactoryProvider connectionTest(RabbitProperties rabbitProperties) {
        return () -> connectionGlobal(rabbitProperties);
    }

    public ConnectionFactory connectionGlobal(RabbitProperties rabbitProperties){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return connectionFactory;
    }
}
