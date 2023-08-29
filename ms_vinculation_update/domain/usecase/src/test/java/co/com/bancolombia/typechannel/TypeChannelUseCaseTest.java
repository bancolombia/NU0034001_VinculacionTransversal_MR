package co.com.bancolombia.typechannel;

import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import co.com.bancolombia.model.typechannel.gateways.TypeChannelRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class TypeChannelUseCaseTest {

    @InjectMocks
    @Spy
    TypeChannelUseCaseImpl typeChannelUseCase;

    @Mock
    TypeChannelRepository typeChannelRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeTest() {

        TypeChannel tc = TypeChannel.builder().code("001").active(true).build();

        Mockito.doReturn(Optional.of(tc)).when(this.typeChannelRepository).findByCode(any(String.class));

        Optional<TypeChannel> tcc = this.typeChannelUseCase.findByCode("");

        assertNotNull(tcc);

    }

    @Test
    public void validateInactive() {
        TypeChannel td = TypeChannel.builder().active(false).build();
        this.typeChannelUseCase.validateActive(Optional.of(td));
    }

    @Test
    public void validateNotFound() {
        TypeChannel td = null;
        this.typeChannelUseCase.validate("", Optional.ofNullable(td));
    }

    @Test
    public void validate() {
        TypeChannel td = TypeChannel.builder().active(true).build();
        this.typeChannelUseCase.validate("", Optional.of(td));
    }

    @Test
    public void validateActive() {
        TypeChannel td = TypeChannel.builder().active(true).build();
        this.typeChannelUseCase.validateActive(Optional.of(td));
    }

}

