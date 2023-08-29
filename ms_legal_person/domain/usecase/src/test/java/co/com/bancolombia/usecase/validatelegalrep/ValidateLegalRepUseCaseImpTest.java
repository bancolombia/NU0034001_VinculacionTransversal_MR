package co.com.bancolombia.usecase.validatelegalrep;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_LEGAL_REP;
import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class ValidateLegalRepUseCaseImpTest {

    @InjectMocks
    @Spy
    ValidateLegalRepUseCaseImp validateLegalRepUseCase;

    Acquisition acquisition;

    @Before
    public void setUp() {
        UUID uuid = UUID.randomUUID();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        acquisition = Acquisition.builder()
                .id(uuid)
                .documentType(documentType)
                .documentNumber("123456").build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateLegalRepTest() {
        assertNotNull(validateLegalRepUseCase.startProcessValidateLegalRep(acquisition, CODE_VALIDATE_LEGAL_REP));
    }

    @Test(expected = ValidationException.class)
    public void validateLegalRelNullAcquisitionTest() {
        validateLegalRepUseCase.startProcessValidateLegalRep(null, CODE_VALIDATE_LEGAL_REP);
    }

}