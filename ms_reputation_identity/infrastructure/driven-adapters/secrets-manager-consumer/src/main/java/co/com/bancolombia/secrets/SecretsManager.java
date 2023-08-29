package co.com.bancolombia.secrets;


import co.com.bancolombia.commons.secretsmanager.connector.AbstractConnector;
import co.com.bancolombia.commons.secretsmanager.connector.clients.AWSSecretManagerConnector;
import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;
import co.com.bancolombia.commons.secretsmanager.manager.GenericManager;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import org.springframework.stereotype.Repository;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PROFILE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;

@Repository
public class SecretsManager implements SecretsManagerConsumer {

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_PROFILE, "SECRETS");

    public SecretsManager() {
    }

    @Override
    public Object getSecrets(Class cls, String secretRegion, String secretName) throws SecretException {
        AbstractConnector connector = new AWSSecretManagerConnector(secretRegion);
        GenericManager manager = new GenericManager(connector);
        return manager.getSecretModel(secretName, cls);
    }
}
