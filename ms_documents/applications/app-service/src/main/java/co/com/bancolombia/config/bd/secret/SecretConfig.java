package co.com.bancolombia.config.bd.secret;

import co.com.bancolombia.model.commons.secretsmodel.SecretsModel;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;

public interface SecretConfig {
    SecretsModel getModel(SecretsManagerConsumer<SecretsModel> consumer);
    String getDbConnectionString(SecretsModel secretsModel);
}