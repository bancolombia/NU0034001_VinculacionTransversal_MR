package co.com.bancolombia.secrets;

import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import co.com.bancolombia.secretsmanager.api.GenericManager;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnector;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

@Repository
public class SecretsManager implements SecretsManagerConsumer {

    public SecretsManager() {
    }

    @Override
    public Object getSecrets(Class cls, String secretRegion, String secretName) throws SecretException {
        GenericManager connector = new AWSSecretManagerConnector(secretRegion);
        Object secret = null;
        try {
            secret = connector.getSecret(secretName, cls);
        } catch(SecretsManagerException e) {
            //TODO: SE DEBE ANEXAR LOG PARA TRAZA
        }
        return secret;


    }
}
