package co.com.bancolombia.bussinesline;

import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.model.businessline.gateways.BusinessLineRepository;
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
public class BusinessLineUseCaseTest {

    @InjectMocks
    @Spy
    BusinessLineUseCaseImpl businessLineUseCase;

    @Mock
    BusinessLineRepository businessLineRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findBusinessLineTest() {

        BusinessLine bl = BusinessLine.builder().code("001").active(true).build();

        Mockito.doReturn(Optional.of(bl)).when(this.businessLineRepository).findByCode(any(String.class));

        Optional<BusinessLine> bll = this.businessLineUseCase.findByCode("");

        assertNotNull(bll);

    }

    @Test
    public void validateInactive() {
        BusinessLine bl = BusinessLine.builder().active(false).build();

        this.businessLineUseCase.validateActive(Optional.of(bl));
    }

    @Test
    public void validateNotFound() {
        this.businessLineUseCase.validate("", Optional.empty());
    }

    @Test
    public void validate() {
        BusinessLine bl = BusinessLine.builder().build();

        this.businessLineUseCase.validate("", Optional.of(bl));
    }

    @Test
    public void validateActive() {
        BusinessLine bl = BusinessLine.builder().active(true).build();

        this.businessLineUseCase.validateActive(Optional.of(bl));
    }
}

