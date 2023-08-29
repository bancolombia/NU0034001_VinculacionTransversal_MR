package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.sqs.SqsMessageUseCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.N_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

public class UploadDocumentSyncUseCaseTest {
	@InjectMocks
	@Spy
	private UploadDocumentSyncUseCaseImpl uploadDocumentSyncUseCase;

	@Mock
	private SqsMessageUseCase sqsMessageUseCase;

	private Acquisition acquisition;

	private List<ProcessDocumentFiles> processDocumentFiles;

	private String documentSubtype;

	private String userTransaction;

	private List<UploadDocumentFile> listGet;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		acquisition = Acquisition.builder()
				.id(UUID.randomUUID())
				.documentType(DocumentType.builder().code(DOCUMENT_TYPE).build())
				.documentNumber("123456")
				.uploadDocumentRetries(2)
				.build();

		processDocumentFiles = Arrays.asList(
				ProcessDocumentFiles.builder()
						.base64("Base64").extension("application/pdf")
						.fileName("001_TIPDOC_FS001_1000000_1.pdf").build());

		documentSubtype = "";
		userTransaction = "";

		listGet = Arrays.asList(UploadDocumentFile.builder().flagSynchronous(N_FOREIGN_INFORMATION).build());
	}

	@Test
	public void validateAsynchronousProcessNullListTest() {
		assertFalse(uploadDocumentSyncUseCase.validateAsynchronousProcess(null));
	}

	@Test
	public void validateAsynchronousProcessValidTest() {
		assertTrue(uploadDocumentSyncUseCase.validateAsynchronousProcess(listGet));
	}

	@Test
	public void validateAsynchronousProcessNotValidTest() {
		listGet = Arrays.asList(UploadDocumentFile.builder().flagSynchronous(FOREIGN_INFORMATION).build());
		assertFalse(uploadDocumentSyncUseCase.validateAsynchronousProcess(listGet));
	}

	@Test
	public void validateAsynchronousProcessMultipleDocsTest() {
		listGet = Arrays.asList(
				UploadDocumentFile.builder().flagSynchronous(FOREIGN_INFORMATION).build(),
				UploadDocumentFile.builder().flagSynchronous(N_FOREIGN_INFORMATION).build());

		assertFalse(uploadDocumentSyncUseCase.validateAsynchronousProcess(listGet));
	}

	@Test
	public void asynchronousProcessTest() {
		doNothing().when(sqsMessageUseCase).sendSqsMessageProcessDocument(
				any(UploadDocumentParameter.class), anyString());

		UploadDocumentParameter docParameter = UploadDocumentParameter.builder()
				.acquisition(acquisition).processDocumentFiles(processDocumentFiles)
				.documentSubtype(documentSubtype).usrTransaction(userTransaction)
				.listGet(listGet).build();

		UploadDocumentWithLog docWithLog = uploadDocumentSyncUseCase.asynchronousProcess(docParameter, "");
		assertNotNull(docWithLog);
	}
	
	@Test
	public void getRequestCcSubtypeTest() {
		documentSubtype = CEDULA_SUBTYPE;

		ProcessDocument processDocument = uploadDocumentSyncUseCase.getRequest(
				acquisition, processDocumentFiles, documentSubtype, "");
		assertNotNull(processDocument);
	}

	@Test
	public void getRequestRutSubtypeTest() {
		documentSubtype = RUT_SUBTYPE;

		ProcessDocument processDocument = uploadDocumentSyncUseCase.getRequest(
				acquisition, processDocumentFiles, documentSubtype, "");
		assertNotNull(processDocument);
	}
}
