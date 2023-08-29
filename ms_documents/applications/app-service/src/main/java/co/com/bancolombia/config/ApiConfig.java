package co.com.bancolombia.config;

import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.SecretModelApiKeyInt;
import co.com.bancolombia.model.commons.SecretModelApiOnPremise;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.commons.TpcClientGenericSign;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import co.com.bancolombia.secrets.SecretsManager;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.axis2.transport.http.security.SSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONNECT_TIMEOUT_MILLIS_VALUE;
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
    public SecretModelApiKeyInt getCredentialApiKeyInt(
            SecretsManagerConsumer<SecretModelApiKeyInt> consumer) throws SecretException {
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
    @Profile({"test", "dev-local"})
    public TpcClientGenericSign getTcpClientSignIgnoredCert() throws SSLException {
        return TpcClientGenericSign.builder().client(getTcpClientIgnoredCert()).build();
    }

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public TpcClientGenericSign getTcpClientSign() throws SSLException {
        return TpcClientGenericSign.builder().client(getTcpClientIgnoredCert()).build();
    }

    public TcpClient getTcpClientIgnoredCert() throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        return TcpClient.newConnection()
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext).handlerConfigurator(
                        sslHandler -> {
                            SSLEngine engine = sslHandler.engine();
                            SSLParameters params = new SSLParameters();
                            List<SNIMatcher> matchers = new LinkedList<>();
                            SNIMatcher matcher = new SNIMatcher(0) {
                                @Override
                                public boolean matches(SNIServerName serverName) {
                                    return true;
                                }
                            };
                            matchers.add(matcher);
                            params.setSNIMatchers(matchers);
                            engine.setSSLParameters(params);
                        }))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Constants.CONNECT_TIMEOUT_MILLIS_VALUE)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(Constants.READ_TIMEOUT_HANDLER,
                            TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(Constants.WRITE_TIMEOUT_HANDLER,
                            TimeUnit.MILLISECONDS));
                });
    }

    @Bean
    @Profile({ "test", "dev-local" })
    public ProtocolSocketFactory getSslProtocolSocketFactoryTest() throws NoSuchAlgorithmException,
            KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        return new SSLProtocolSocketFactory(sc);
    }

    @Bean
    @Profile({ "dev", "qa", "pdn" })
    public ProtocolSocketFactory getSslProtocolSocketFactory(TrustManagerFactory getTrustManagerFactory ) throws
            SSLException, NoSuchAlgorithmException, KeyManagementException {

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLSv1.3");
        sc.init(null,getTrustManagerFactory.getTrustManagers() , new java.security.SecureRandom());

        return new SSLProtocolSocketFactory(sc);
    }
}