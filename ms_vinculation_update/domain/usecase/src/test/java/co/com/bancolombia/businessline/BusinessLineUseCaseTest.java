package co.com.bancolombia.businessline;

import co.com.bancolombia.bussinesline.BusinessLineUseCaseImpl;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class BusinessLineUseCaseTest {

    @InjectMocks
    @Spy
    private BusinessLineUseCaseImpl businessLineUseCase;

    @Mock
    private BusinessLineRepository businessLineRepository;

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
    public void validateInactiveTest() {
        BusinessLine bl = BusinessLine.builder().active(false).build();
        List<ErrorField> error = businessLineUseCase.validateActive(Optional.of(bl));
        assertEquals(1, error.size());
    }

    @Test
    public void validateNotFoundTest() {
        List<ErrorField> error = businessLineUseCase.validate("", Optional.empty());
        assertEquals(1, error.size());
    }

    @Test
    public void validatePresentTest() {
        BusinessLine bl = BusinessLine.builder().build();
        List<ErrorField> error = businessLineUseCase.validate("", Optional.of(bl));
        assertEquals(0, error.size());
    }

    @Test
    public void validateActiveTest() {
        BusinessLine bl = BusinessLine.builder().active(true).build();
        List<ErrorField> error = businessLineUseCase.validateActive(Optional.of(bl));
        assertEquals(0, error.size());
    }
}
