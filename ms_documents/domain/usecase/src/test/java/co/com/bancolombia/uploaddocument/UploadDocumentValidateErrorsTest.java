package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.model.documentretries.DocumentRetries;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_RUT_RETRIES_FLAG_EMISSION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class UploadDocumentValidateErrorsTest {

	@InjectMocks
	@Spy
	private UploadDocumentValidateErrorsImpl uploadDocumentValidateErrors;

	@Mock
	private VinculationUpdateUseCase vinculationUpdateUseCase;

	@Mock
	private ParametersUseCase parametersUseCase;

	@Mock
	private UploadDocumentExcepUseCase uplDocExcepUseCase;

	@Mock
	private DocumentRetriesUseCase documentRetriesUseCase;
	
	private Acquisition acquisition;

	private SqsMessageParamAllObject sqsMessageParamAllObject;
	
	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        acquisition = Acquisition.builder()
        		.id(UUID.randomUUID())
        		.uploadDocumentRetries(0)
        		.uploadRutRetries(0)
        		.build();

        sqsMessageParamAllObject = SqsMessageParamAllObject.builder().build();
    }
    
    @Test
    public void validateExceptionRetriesPendingRetriesTest() {
		Parameters parameters = Parameters.builder().code("1").build();
		DocumentRetries retries = DocumentRetries.builder().build();

    	doReturn(Optional.of(parameters)).when(parametersUseCase).findByNameAndParent(anyString(), anyString());
    	doReturn(retries).when(documentRetriesUseCase).save(any(Acquisition.class));
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(uplDocExcepUseCase).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

    	uploadDocumentValidateErrors.validateExceptionRetries(acquisition, "", sqsMessageParamAllObject);
    	verify(uplDocExcepUseCase, times(1)).validateException(
    			anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));
    }
    
    @Test
    public void validateExceptionRetriesNoRetriesTest() {
    	doReturn(Optional.empty()).when(parametersUseCase).findByNameAndParent(anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(uplDocExcepUseCase).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

    	uploadDocumentValidateErrors.validateExceptionRetries(acquisition, "", sqsMessageParamAllObject);
		verify(uplDocExcepUseCase, times(1)).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));
    }

    @Test
    public void validateExceptionRutRetriesTrueFlagEmissionTest() {
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(uplDocExcepUseCase).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

    	uploadDocumentValidateErrors.validateExceptionRutRetries(
    			acquisition, "", VALIDATE_RUT_RETRIES_FLAG_EMISSION, sqsMessageParamAllObject);
		verify(uplDocExcepUseCase, times(1)).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));
    }
    
    @Test
    public void validateExceptionRutRetriesPendingRetriesTest() {
		Parameters parameters = Parameters.builder().code("1").build();
		DocumentRetries retries = DocumentRetries.builder().build();

    	doReturn(Optional.of(parameters)).when(parametersUseCase).findByNameAndParent(anyString(), anyString());
		doReturn(retries).when(documentRetriesUseCase).save(any(Acquisition.class));
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(uplDocExcepUseCase).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

    	uploadDocumentValidateErrors.validateExceptionRutRetries(
    			acquisition, "", "PERSONAL_INFO", sqsMessageParamAllObject);
		verify(uplDocExcepUseCase, times(1)).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));
    }

	@Test
	public void validateExceptionRutRetriesNoRetriesPersonalInfoTest() {
		Parameters parameters = Parameters.builder().code("0").build();
		doReturn(Optional.of(parameters)).when(parametersUseCase).findByNameAndParent(anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(uplDocExcepUseCase).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

		acquisition.setUploadRutRetries(1);
		uploadDocumentValidateErrors.validateExceptionRutRetries(
				acquisition, "", "PERSONAL_INFO", sqsMessageParamAllObject);
		verify(uplDocExcepUseCase, times(1)).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));
	}

	@Test
	public void validateExceptionRutRetriesNoRetriesOtherInfoTest() {
		Parameters parameters = Parameters.builder().code("0").build();
		doReturn(Optional.of(parameters)).when(parametersUseCase).findByNameAndParent(anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(uplDocExcepUseCase).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

		acquisition.setUploadRutRetries(1);
		uploadDocumentValidateErrors.validateExceptionRutRetries(
				acquisition, "", "OTHER_INFO", sqsMessageParamAllObject);
		verify(uplDocExcepUseCase, times(1)).validateException(
				anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));
	}
}
