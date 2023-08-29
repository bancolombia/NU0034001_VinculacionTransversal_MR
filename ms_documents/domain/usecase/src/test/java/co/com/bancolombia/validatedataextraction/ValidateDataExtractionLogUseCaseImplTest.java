package co.com.bancolombia.validatedataextraction;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentApiResponseData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.context.request.WebRequest;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

public class ValidateDataExtractionLogUseCaseImplTest {
    @InjectMocks
    @Spy
    private ValidateDataExtractionLogUseCaseImpl validateDataExtractionLogUseCase;

    @Mock
    private WebRequest webRequest;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveInfoLog(){
        AsyncDigitalization asyncDigitalization = AsyncDigitalization.builder()
                .requestReuse("")
                .requestDateReuse(coreFunctionDate.getDatetime())
                .responseReuse("")
                .responseDateReuse(coreFunctionDate.getDatetime())
                .build();

        this.validateDataExtractionLogUseCase.saveInfoLog(asyncDigitalization);
        verify(this.validateDataExtractionLogUseCase).saveInfoLog(asyncDigitalization);
    }

    @Test
    public void getObjectValid(){
        AsyncDigitalization asyncDigitalization = AsyncDigitalization.builder().build();
        UploadDocumentApiResponseData objSuccess = UploadDocumentApiResponseData.builder().build();

        assertNotNull(this.validateDataExtractionLogUseCase.getObjectValid(asyncDigitalization,objSuccess));
    }


}
