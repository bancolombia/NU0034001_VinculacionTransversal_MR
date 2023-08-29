package co.com.bancolombia.usecase.tokenretries;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.model.tokenretries.gateways.TokenRetriesRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class TokenRetriesUseCaseImplTest {

    @InjectMocks
    @Spy
    private TokenRetriesUseCaseImpl tokenRetriesUseCase;

    @Mock
    private TokenRetriesRepository tokenRetriesRepository;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveTest() {
        TokenRetries tokenRetries = TokenRetries.builder().build();
        doReturn(tokenRetries).when(tokenRetriesRepository).save(any(TokenRetries.class));
        TokenRetries tokenRetries1 = this.tokenRetriesUseCase.save(tokenRetries);
        assertNotNull(tokenRetries1);
    }

    @Test
    public void findByAcquisition() {
        TokenRetries tokenRetries = TokenRetries.builder().build();
        doReturn(tokenRetries).when(tokenRetriesRepository).findByAcquisition(any(Acquisition.class));
        Optional<TokenRetries> tokenRetries1 = tokenRetriesUseCase
                .findByAcquisition(Acquisition.builder().build());
        assertNotNull(tokenRetries1);
    }

    @Test
    public void initTokenRetriesIsPresentTest() {

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        TokenRetries tokenRetries = TokenRetries.builder().acquisition(acquisition).build();
        doReturn(tokenRetries).when(tokenRetriesRepository).findByAcquisition(any(Acquisition.class));

        String userTransaction = "BIZAGI";
        TokenRetries tokenRetries1 = tokenRetriesUseCase.initTokenRetries(acquisition, userTransaction);
        assertNotNull(tokenRetries1);
    }

    @Test
    public void initTokenRetriesIsNotPresentTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String userTransaction = "BIZAGI";
        doReturn(null).when(tokenRetriesRepository).findByAcquisition(any(Acquisition.class));

        TokenRetries tokenRetries = TokenRetries.builder().acquisition(acquisition).build();
        doReturn(tokenRetries).when(tokenRetriesRepository).save(any(TokenRetries.class));
        TokenRetries tokenRetries1 = tokenRetriesUseCase.initTokenRetries(acquisition, userTransaction);
        assertNotNull(tokenRetries1);
    }
}