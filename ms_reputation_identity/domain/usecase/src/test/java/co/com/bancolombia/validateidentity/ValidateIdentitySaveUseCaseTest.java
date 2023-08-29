package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.gateways.ValidateSaveRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class ValidateIdentitySaveUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateIdentitySaveUseCaseImpl vISaveUseCase;

    @Mock
    private ValidateIdentityTransformUseCase vIdentityTUseCase;

    @Mock
    private ValidateSaveRepository saveRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save() {
        Mockito.doReturn(ValidateIdentitySave.builder().build()).when(saveRepository)
                .save(any(ValidateIdentitySave.class));
        Mockito.doReturn(ValidateIdentitySave.builder().build()).when(vIdentityTUseCase)
                .transformValidateIdentitySave(any(ValidateIdentityResponse.class), any(AcquisitionReply.class),
                        any(BasicAcquisitionRequest.class));
        ValidateIdentitySave validateIdentitySave = vISaveUseCase.save(ValidateIdentityResponse.builder().build(),
                AcquisitionReply.builder().build(), BasicAcquisitionRequest.builder().build());
        assertNotNull(validateIdentitySave);
    }
}
