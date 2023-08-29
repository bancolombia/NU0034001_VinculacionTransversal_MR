package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.validatetoken.Datum;
import co.com.bancolombia.model.validatetoken.Meta;
import co.com.bancolombia.model.validatetoken.Response;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponse;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class ValidateTokenMapperUseCaseImplTest {

    @InjectMocks
    @Spy
    private ValidateTokenMapperUseCaseImpl validateTokenMapperUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);}


    @Test
    public void mapperFromValidateTokenTest(){
        ValidateTokenResponse validateTokenResponse = ValidateTokenResponse.builder()
                .meta(Meta.builder().messageId("asd").requestDate(coreFunctionDate.getDatetime()).build())
                .data(Datum.builder().generateTokenResponse(
                        Response.builder()
                                .answerDescription("asd").answerCode("asd")
                                .responseDate(coreFunctionDate.getDatetime()).build()).build()).build();

        BasicAcquisitionRequest request = BasicAcquisitionRequest.builder()
                .documentNumber("asd").documentType("asd").userTransaction("asd").build();
        ValidateToken validateToken1 = ValidateToken.builder().build();
        ValidateToken validateToken = validateTokenMapperUseCase.mapperFromValidateToken(
                validateToken1, validateTokenResponse, request, "310");
        assertNotNull(validateToken);
    }
}