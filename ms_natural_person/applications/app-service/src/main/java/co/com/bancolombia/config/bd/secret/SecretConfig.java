package co.com.bancolombia.config.bd.secret;

import co.com.bancolombia.common.secretsmodel.SecretsModel;
import co.com.bancolombia.common.secretsmodel.gateways.SecretsManagerConsumer;

public interface SecretConfig {
    SecretsModel getModel(SecretsManagerConsumer<SecretsModel> consumer);
    String getDbConnectionString(SecretsModel secretsModel);
}