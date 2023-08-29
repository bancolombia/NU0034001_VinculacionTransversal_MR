
package co.com.bancolombia.secretmanager;

import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;
import co.com.bancolombia.model.commons.secretsmodel.SecretsModel;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
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