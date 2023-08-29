package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.markcustomer.Meta;
import co.com.bancolombia.model.markcustomer.RegisterControlListResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListSave;
import co.com.bancolombia.model.markcustomer.gateways.RegisterControlListRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class MarkCustomerSaveUseCaseTest {

    @InjectMocks
    @Spy
    private MarkCustomerSaveUseCaseImpl markCustomerSaveUseCase;

    @Mock
    private RegisterControlListRepository repository;

    @Mock
    private CoreFunctionDate coreFD;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveInfoTest() {
        UUID uuid = UUID.randomUUID();
        Meta meta = Meta.builder().messageId("").build();
        Mockito.doReturn(RegisterControlListSave.builder().build()).when(repository).save(any(RegisterControlListSave.class));
        this.markCustomerSaveUseCase.saveInfo(RegisterControlListResponse.builder().meta(meta).build(),
                Acquisition.builder().id(uuid).build(), BasicAcquisitionRequest.builder().userTransaction("").build());
        Mockito.verify(this.markCustomerSaveUseCase, Mockito.times(1))
                .saveInfo(RegisterControlListResponse.builder().meta(meta).build(),
                        Acquisition.builder().id(uuid).build(), BasicAcquisitionRequest.builder().userTransaction("").build());
    }
}
