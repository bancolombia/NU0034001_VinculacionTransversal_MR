package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.validatetoken.ValidateTokenRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class ValidateTokenCreateRequestUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateTokenCreateRequestUseCase validateTokenCreateRequestUseCase;



    private Acquisition acquisition;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        acquisition = Acquisition.builder().id(UUID.randomUUID())
                .typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build())
                .documentType(DocumentType.builder().codeHomologation("asd").build()).build();
    }

    @Test
    public void createRequestTest(){
        ValidateTokenRequest validateTokenRequest = validateTokenCreateRequestUseCase.createRequest(
                acquisition, "123", "12313", "310");
        assertNotNull(validateTokenRequest);
    }



}