package co.com.bancolombia.secretmanager;

import co.com.bancolombia.commons.secretsmanager.exceptions.SecretException;
import co.com.bancolombia.model.commons.secretsmodel.SecretsModel;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SecretManagerUseCaseTest {

    @InjectMocks
    @Spy
    private SecretManagerUseCase secretManagerUseCase;

    @Mock
    private SecretsManagerConsumer<SecretsModel> consumer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSecretsTest() throws SecretException {
        Object sm = new Object();
        doReturn(sm).when(consumer).getSecrets(any(Class.class), anyString(), anyString());

        Object secretsModel = secretManagerUseCase.getSecrets("region", "name");
        assertNotNull(secretsModel);
    }
}
