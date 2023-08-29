package co.com.bancolombia.config;

import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;
import co.com.bancolombia.model.commons.ApiRiskCredential;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.SecretModelApiKeyInt;
import co.com.bancolombia.model.commons.SecretModelApiOnPremise;
import co.com.bancolombia.model.commons.SecretModelApiRisk;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import co.com.bancolombia.secrets.SecretsManager;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONNECT_TIMEOUT_MILLIS_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.READ_TIMEOUT_HANDLER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.WRITE_TIMEOUT_HANDLER;

@Configuration
public class ApiConfig {

    @Value("${apionpremise.secret.region}")
    private String secretRegionOnPremise;

    @Value("${apionpremise.secret.name}")
    private String secretNameOnPremise;

    @Value("${apikeyint.secret.region}")
    private String secretRegionApiKey;

    @Value("${apikeyint.secret.name}")
    private String secretNameApiKey;

    @Value("${apirisk.secret.region}")
    private String secretRegionApiRisk;

    @Value("${apirisk.secret.name}")
    private String secretNameApiRisk;

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public OnPremiseCredential getCredentialApiOnPremise(SecretsManagerConsumer<SecretModelApiOnPremise> consumer)
            throws SecretException {

        SecretsManager secretsManager = new SecretsManager();
        SecretModelApiOnPremise model = (SecretModelApiOnPremise) secretsManager.getSecrets(SecretModelApiOnPremise.class,
                this.secretRegionOnPremise, this.secretNameOnPremise);

        return OnPremiseCredential.builder().clientId(model.getClientid()).clientSecret(model.getClientsecret())
                .build();
    }

    @Bean
    @Profile({"dev-local", "test"})
    public OnPremiseCredential getCredentialApiOnPremiseTest() {
        return OnPremiseCredential.builder()
                .clientId(secretRegionOnPremise)
                .clientSecret(secretNameOnPremise)
                .build();
    }

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public SecretModelApiKeyInt getCredentialApiKeyInt(SecretsManagerConsumer<SecretModelApiKeyInt> consumer)
            throws SecretException {
        SecretsManager secretsManager = new SecretsManager();

        return (SecretModelApiKeyInt) secretsManager.getSecrets(SecretModelApiKeyInt.class,
                this.secretRegionApiKey, this.secretNameApiKey);

    }

    @Bean
    @Profile({"dev-local", "test"})
    public SecretModelApiKeyInt getCredentialApiKeyIntTest() {
        return SecretModelApiKeyInt.builder().apiKeyInt(this.secretNameApiKey).build();
    }

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public TpcClientGeneric getTcpClient() {
        TcpClient client = TcpClient.newConnection()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS_VALUE)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_HANDLER, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_HANDLER, TimeUnit.MILLISECONDS));
                });
        return TpcClientGeneric.builder().client(client).build();
    }

    @Bean
    @Profile({"dev-local", "test"})
    public TpcClientGeneric getTcpClientTest() throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        TcpClient client = TcpClient.newConnection().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS_VALUE)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_HANDLER, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_HANDLER, TimeUnit.MILLISECONDS));
                });
        return TpcClientGeneric.builder().client(client).build();
    }

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public ApiRiskCredential getCredentialApiRisk(SecretsManagerConsumer<SecretModelApiRisk> consumer)
            throws SecretException {

        SecretsManager secretsManager = new SecretsManager();
        SecretModelApiRisk model = (SecretModelApiRisk) secretsManager.getSecrets(SecretModelApiRisk.class,
                this.secretRegionApiRisk, this.secretNameApiRisk);

        return ApiRiskCredential.builder().userId(model.getUserId()).parameterCode(model.getParameterCode())
                .productVersion(model.getProductVersion()).build();
    }

    @Bean
    @Profile({"dev-local", "test"})
    public ApiRiskCredential getCredentialApiRiskTest() {
        return ApiRiskCredential.builder()
                .userId(secretNameApiRisk)
                .productVersion(secretRegionApiRisk)
                .parameterCode(
                        PARAMETER_CODE)
                .build();
    }
}
