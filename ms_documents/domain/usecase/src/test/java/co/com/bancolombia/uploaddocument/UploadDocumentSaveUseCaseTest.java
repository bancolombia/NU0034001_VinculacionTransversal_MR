package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.api.model.util.Meta;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.uploaddocument.KofaxInformation;
import co.com.bancolombia.model.uploaddocument.UploadDocumentErrorResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENDER_CODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UploadDocumentSaveUseCaseTest {

	@InjectMocks
	@Spy
	private UploadDocumentSaveUseCaseImpl uploadDocumentSaveUseCaseImpl;
	
	@Mock
    private CoreFunctionDate coreFunctionDate;

	@Mock
	private NaturalPersonUseCase naturalPersonUseCase;

	@Mock
	private VinculationUpdateUseCase vinculationUpdateUseCase;

	private KofaxInformation kofaxInformation;

	private Acquisition acquisition;

	private String usrTransaction;
	
	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        kofaxInformation = KofaxInformation.builder().build();
        acquisition = Acquisition.builder().id(UUID.randomUUID()).uploadDocumentRetries(0).uploadRutRetries(0).build();
        usrTransaction = "BIZAGI";
	}
	
	@Test
	public void savePersonalInfoSuccessTest() {
		EmptyReply reply = EmptyReply.builder().valid(true).build();
		doReturn(reply).when(naturalPersonUseCase).savePersonalInfo(
				any(UUID.class), any(KofaxInformation.class), anyString());

		uploadDocumentSaveUseCaseImpl.savePersonalInfo(kofaxInformation, acquisition, usrTransaction);
		verify(naturalPersonUseCase, times(1)).savePersonalInfo(
				any(UUID.class), any(KofaxInformation.class), anyString());
	}

	@Test
	public void savePersonalInfoNotValidNull() {
		EmptyReply reply = EmptyReply.builder().valid(false).build();
		doReturn(reply).when(naturalPersonUseCase).savePersonalInfo(
				any(UUID.class), any(KofaxInformation.class), anyString());

		uploadDocumentSaveUseCaseImpl.savePersonalInfo(kofaxInformation, acquisition, usrTransaction);
		verify(naturalPersonUseCase, times(1)).savePersonalInfo(
				any(UUID.class), any(KofaxInformation.class), anyString());
	}

	@Test
	public void saveBasicInfoSuccessTest() {
		EmptyReply reply = EmptyReply.builder().valid(true).build();

		doReturn(reply).when(naturalPersonUseCase).saveBasicInfo(any(UUID.class), anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

		uploadDocumentSaveUseCaseImpl.saveBasicInfo(kofaxInformation, acquisition, "M", usrTransaction);
		verify(vinculationUpdateUseCase, times(1)).markOperation(any(UUID.class), anyString(), anyString());
	}

	@Test
	public void saveBasicInfoNotValidTest() {
		EmptyReply reply = EmptyReply.builder().valid(false).build();
		doReturn(reply).when(naturalPersonUseCase).saveBasicInfo(any(UUID.class), anyString(), anyString());

		uploadDocumentSaveUseCaseImpl.saveBasicInfo(kofaxInformation, acquisition, "M", usrTransaction);
		verify(naturalPersonUseCase, times(1)).saveBasicInfo(any(UUID.class), anyString(), anyString());
	}

	@Test
	public void transformKofaxGenderFieldSuccessTest() {
		String male = GENDER_CODE + "M";
		CatalogReply reply = CatalogReply.builder().valid(true).code(male).build();
		doReturn(reply).when(vinculationUpdateUseCase).findCatalog(anyString(), anyString());

		String result = uploadDocumentSaveUseCaseImpl.transformKofaxGenderField("M");
		assertEquals(male, result);
	}

	@Test
	public void transformKofaxGenderFieldNotValidTest() {
		CatalogReply reply = CatalogReply.builder().valid(false).code(EMPTY).build();
		doReturn(reply).when(vinculationUpdateUseCase).findCatalog(anyString(), anyString());

		String result = uploadDocumentSaveUseCaseImpl.transformKofaxGenderField("M");
		assertEquals(EMPTY, result);
	}
	
	@Test
	public void validateQualityFieldsNullValuesTest() {
		assertTrue(uploadDocumentSaveUseCaseImpl.validateQualityFields(kofaxInformation));
	}

	@Test
	public void validateQualityFieldsEmptyValuesTest() {
		kofaxInformation = KofaxInformation.builder()
				.firstName(EMPTY).secondName(EMPTY).firstSurname(EMPTY).secondSurname(EMPTY).build();

		assertTrue(uploadDocumentSaveUseCaseImpl.validateQualityFields(kofaxInformation));
	}

	@Test
	public void validateQualityFieldsSuccessTest() {
		Date date = new Date();

		kofaxInformation = KofaxInformation.builder()
				.firstName("FIRST NAME").secondName("SECOND NAME")
				.firstSurname("FIRST SURNAME").secondSurname("SECOND SURNAME")
				.expeditionDate(date).birthDate(date).build();

		doReturn(date).when(coreFunctionDate).getDatetime();

		assertTrue(uploadDocumentSaveUseCaseImpl.validateQualityFields(kofaxInformation));
	}

	@Test
	public void validateQualityFieldsErrorsTest() {
		Date today = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, 1);

		Date tomorrow = calendar.getTime();

		kofaxInformation = KofaxInformation.builder()
				.firstName("FIRST_NAME").secondName("SECOND_NAME")
				.firstSurname("FIRST_SURNAME").secondSurname("SECOND_SURNAME")
				.expeditionDate(tomorrow).birthDate(tomorrow).build();

		doReturn(today).when(coreFunctionDate).getDatetime();

		assertFalse(uploadDocumentSaveUseCaseImpl.validateQualityFields(kofaxInformation));
	}

	@Test
	public void validateKofaxMessageIdResponseTest() {
		Meta meta = new Meta();
		meta.setMessageId("123456");

		UploadDocumentResponse docResponse = UploadDocumentResponse.builder().meta(meta).build();
		UploadDocumentTotal docTotal = UploadDocumentTotal.builder().uploadDocumentResponse(docResponse).build();

		assertTrue(uploadDocumentSaveUseCaseImpl.validateKofaxMessageId(docTotal, "123456"));
	}

	@Test
	public void validateKofaxMessageIdErrorTest() {
		Meta meta = new Meta();
		meta.setMessageId("123456");

		UploadDocumentErrorResponse errorResponse = UploadDocumentErrorResponse.builder().meta(meta).build();
		UploadDocumentTotal docTotal = UploadDocumentTotal.builder().uploadDocumentErrorResponse(errorResponse).build();

		assertTrue(uploadDocumentSaveUseCaseImpl.validateKofaxMessageId(docTotal, "123456"));
	}

	@Test
	public void getMessageIdTest() {
		doReturn(new Date()).when(coreFunctionDate).toDate(any(LocalDateTime.class));

		String messageId = uploadDocumentSaveUseCaseImpl.getMessageId();
		assertNotNull(messageId);
	}
}
