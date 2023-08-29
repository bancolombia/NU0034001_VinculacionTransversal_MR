package co.com.bancolombia.sqs;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessaPararmSave;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.uploaddocument.UploadDocumentProcessedDocumentsUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentRutUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentValidateUseCase;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SqsMessageUplDocUseCaseTest {

	@InjectMocks
	@Spy
	private SqsMessageUplDocUseCaseImpl sqsMessageUplDocUseCase;

	@Mock
	private UploadDocumentValidateUseCase uploadDocumentValidateUseCase;

	@Mock
	private UploadDocumentUseCase uploadDocumentUseCase;

	@Mock
	private UploadDocumentRutUseCase uploadDocumentRutUseCase;

	@Mock
	private SqsMessageUplDocUtilUseCase sqsMessageUplDocUtilUseCase;

	@Mock
	private SqsMessageSaveUseCase sqsMessageSaveUseCase;

	@Mock
	private AcquisitionUseCase acquisitionUseCase;

	@Mock
	private UploadDocumentProcessedDocumentsUseCase uploadDocumentProcessedDocumentsUseCase;

	private static final String MESSAGE_TEST = "{"
			+ "  'data': {"
			+ "    'acquisition': {"
			+ "      'documentNumber': '10000000',"
			+ "      'documentType': 'TIPDOC_FS001',"
			+ "      'id': 'c7250f5b-b167-4c45-b58b-4a87c4eb8eba',"
			+ "      'uploadDocumentRetries': 0"
			+ "    },"
			+ "    'files': ["
			+ "      {"
			+ "        'base64': '',"
			+ "        'extension': 'application/pdf',"
			+ "        'fileName': '001_TIPDOC_FS001_10000000.pdf',"
			+ "        'flagDataExtraction': 'RESPUE_S',"
			+ "        'flagSynchronous': 'RESPUE_N',"
			+ "        'documentalSubTypeName': '001',"
			+ "        'documentalTypeCode': '01'"
			+ "      }"
			+ "    ],"
			+ "    'messageId': '09042021204725022',"
			+ "    'processCode': '001',"
			+ "    'processName': 'VinculacionTransversal',"
			+ "    'userCode': 0"
			+ "  },"
			+ "  'meta': {"
			+ "    'usrMod': 'BIZAGI',"
			+ "    'ip': 'ip',"
			+ "    'systemId': 'AAAAAA',"
			+ "    'messageId': '3aSJDUAJ-1111-JJKJ-OKLO-KSLAJSND73',"
			+ "    'version': '1',"
			+ "    'requestDate': '20150625200000',"
			+ "    'service': 'management',"
			+ "    'operation': 'start-acquisition'"
			+ "  }"
			+ "}";

	private SqsMessObjUploadDoc obj;

	private Acquisition acquisition;

	private SqsMessage sqsMessage;

	private List<UploadDocumentFile> uploadDocumentFiles;

	private List<ProcessDocumentFiles> processDocumentFiles;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		Gson g = new Gson();
		obj = g.fromJson(MESSAGE_TEST, SqsMessObjUploadDoc.class);

		acquisition = Acquisition.builder()
				.id(UUID.randomUUID())
				.documentNumber("10000000")
				.documentType(DocumentType.builder()
						.code("TIPDOC_FS001")
						.name("CEDULA")
						.build())
				.typeAcquisition(TypeAcquisition.builder()
						.code("TVN001")
						.name("001")
						.build())
				.uploadDocumentRetries(0)
				.uploadRutRetries(0)
				.build();

		sqsMessage = SqsMessage.builder()
				.id(UUID.randomUUID())
				.operation("")
				.typeAcquisition("")
				.acquisition(acquisition)
				.documentType("")
				.documentNumber("")
				.request("")
				.response("")
				.message("")
				.statusMessage("")
				.helpField("")
				.build();

		processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder().build());

		uploadDocumentFiles = Arrays.asList(UploadDocumentFile.builder().build());
	}

	@Test
	public void readMessageCcSubtypeTest() {
		obj.getData().getFiles().get(0).setDocumentalSubTypeCode(CEDULA_SUBTYPE);

		UploadDocumentWithLog uploadDocLog = UploadDocumentWithLog.builder()
				.processDocumentKofaxTotal(ProcessDocumentKofaxTotal.builder().build())
				.infoReuseCommon(InfoReuseCommon.builder().build())
				.build();

		doReturn(obj).when(sqsMessageUplDocUtilUseCase).constructSqsMessObjUploadDoc(anyString());
		doReturn(acquisition).when(acquisitionUseCase).getAcquisition(any(SqsMessObjUploadDoc.class));
		doReturn(uploadDocumentFiles).when(sqsMessageUplDocUtilUseCase).getListGet(any(SqsMessObjUploadDoc.class));
		doReturn(UploadDocumentParameter.builder().build()).when(sqsMessageSaveUseCase)
				.constructUploadDocumentParameter(any(Acquisition.class), any(SqsMessObjUploadDoc.class),
						any(List.class));
		doReturn(processDocumentFiles).when(uploadDocumentValidateUseCase).getProcessDocumentFiles(
				anyString(), any(List.class), any());
		doReturn(uploadDocLog).when(uploadDocumentUseCase).processDocument(
				any(UploadDocumentParameter.class), any(SqsMessObjUploadDoc.class));
		doNothing().when(sqsMessageSaveUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
		doNothing().when(uploadDocumentProcessedDocumentsUseCase).saveProcessedDocuments(
				any(UploadDocumentParameter.class), any(), any(SqsMessageParamAllObject.class), any(Map.class));

		sqsMessageUplDocUseCase.readMessage(MESSAGE_TEST, new HashMap<>(), SqsMessage.builder().build());
		verify(uploadDocumentUseCase, times(1)).processDocument(
				any(UploadDocumentParameter.class), any(SqsMessObjUploadDoc.class));
	}

	@Test
	public void readMessageRutSubtypeTest() {
		obj.getData().getFiles().get(0).setDocumentalSubTypeCode(RUT_SUBTYPE);

		UploadDocumentWithLog uploadDocLog = UploadDocumentWithLog.builder()
				.processDocumentKofaxTotal(ProcessDocumentKofaxTotal.builder()
						.processDocumentKofaxError(ProcessDocumentKofaxError.builder().build())
						.build())
				.build();

		doReturn(obj).when(sqsMessageUplDocUtilUseCase).constructSqsMessObjUploadDoc(anyString());
		doReturn(acquisition).when(acquisitionUseCase).getAcquisition(any(SqsMessObjUploadDoc.class));
		doReturn(uploadDocumentFiles).when(sqsMessageUplDocUtilUseCase).getListGet(any(SqsMessObjUploadDoc.class));
		doReturn(UploadDocumentParameter.builder().build()).when(sqsMessageSaveUseCase)
				.constructUploadDocumentParameter(any(Acquisition.class), any(SqsMessObjUploadDoc.class),
						any(List.class));
		doReturn(processDocumentFiles).when(uploadDocumentValidateUseCase).getProcessDocumentFiles(
				anyString(), any(List.class), any());
		doReturn(uploadDocLog).when(uploadDocumentRutUseCase).processDocument(
				any(UploadDocumentParameter.class), any(SqsMessObjUploadDoc.class));
		doNothing().when(sqsMessageSaveUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
		doNothing().when(uploadDocumentProcessedDocumentsUseCase).saveProcessedDocuments(
				any(UploadDocumentParameter.class), any(), any(SqsMessageParamAllObject.class), any(Map.class));

		sqsMessageUplDocUseCase.readMessage(MESSAGE_TEST, new HashMap<>(), SqsMessage.builder().build());
		verify(uploadDocumentRutUseCase, times(1)).processDocument(
				any(UploadDocumentParameter.class), any(SqsMessObjUploadDoc.class));
	}

	@Test
	public void saveAndUpdateSqsMessageTest() {
		doNothing().when(sqsMessageSaveUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));

		SqsMessaPararmSave sqsMessaPararmSave = SqsMessaPararmSave.builder().saveAsync(false).build();
		sqsMessageUplDocUseCase.saveAndUpdateSqsMessage(sqsMessaPararmSave);
		verify(sqsMessageSaveUseCase, times(1)).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}
}
