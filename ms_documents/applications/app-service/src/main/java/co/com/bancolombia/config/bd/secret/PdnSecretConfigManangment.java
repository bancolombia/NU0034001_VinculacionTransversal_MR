package co.com.bancolombia.config.bd.secret;

import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;
import co.com.bancolombia.model.commons.secretsmodel.SecretsModel;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import co.com.bancolombia.secretmanager.SecretManagerUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "qa", "pdn"})
@Qualifier("secretConfig")
public class PdnSecretConfigManangment implements SecretConfig {
    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    @Value("${db.manangment.secret.region}")
    private String secretRegion;

    @Value("${db.manangment.secret.name}")
    private String secretName;

    @Value("${db.manangment.secret.url}")
    private String dbConnectionString;

    @Override
    public SecretsModel getModel(SecretsManagerConsumer<SecretsModel> consumer) {
        SecretManagerUseCase secrets = new SecretManagerUseCase(consumer);
        SecretsModel model = null;
        try {
            model = (SecretsModel) secrets.getSecrets(this.secretRegion, this.secretName);
        } catch (SecretException e) {
            //adapter.info("Secreto no encontrado: " + e.getMessage());
        }
        return model;
    }

    @Override
    public String getDbConnectionString(SecretsModel secretsModel) {
        return String.format(this.dbConnectionString,
                secretsModel.getHost(),
                secretsModel.getPort(),
                secretsModel.getDbname());
    }
}