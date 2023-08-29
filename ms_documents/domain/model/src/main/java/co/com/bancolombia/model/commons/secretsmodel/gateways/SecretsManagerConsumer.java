package co.com.bancolombia.model.commons.secretsmodel.gateways;

import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;

public interface SecretsManagerConsumer<T> {
    T getSecrets(Class<T> cls, String secretRegion, String secretName) throws SecretException;
}
