package co.com.bancolombia.config;

import co.com.bancolombia.model.commons.SecretModelApiKeyInt;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import co.com.bancolombia.secrets.SecretsManager;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ApiConfig {

    @Value("${apikeyint.secret.region}")
    private String secretRegionApiKey;

    @Value("${apikeyint.secret.name}")
    private String secretNameApiKey;


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
}
