package co.com.bancolombia.config.bd.secret;

import co.com.bancolombia.model.commons.secretsmodel.SecretsModel;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;

public interface SecretConfig {
    public SecretsModel getModel(SecretsManagerConsumer<SecretsModel> consumer);
    public String getDbConnectionString(SecretsModel secretsModel);
}
