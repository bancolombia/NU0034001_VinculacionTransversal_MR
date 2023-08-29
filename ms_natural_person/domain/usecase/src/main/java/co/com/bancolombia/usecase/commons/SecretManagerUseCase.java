
package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.common.secretsmodel.SecretsModel;
import co.com.bancolombia.common.secretsmodel.gateways.SecretsManagerConsumer;
import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecretManagerUseCase {
    private final SecretsManagerConsumer<SecretsModel> consumer;

    /**
     * Method for get conexion to data base through secret manager
     * @param secretRegion
     * @param secretName
     * @return
     * @throws SecretException
     */
    public Object getSecrets(String secretRegion, String secretName) throws SecretException {
        return consumer.getSecrets(SecretsModel.class, secretRegion, secretName);
    }
}