package co.com.bancolombia.sqs;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.sqs.SqsMessFileObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class SqsMessageUplDocAcqUseCaseTest {
	
	@InjectMocks	
	private SqsMessageUplDocAcqUseCaseImpl sqsMessageUplDocAcqUseCase;

	private SqsMessObjUploadDoc obj;

	private SqsMessFileObjUploadDoc item;
	
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
	
	private Acquisition acquisition;
	
	private List<ProcessDocumentFiles> processDocumentFiles;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		Gson g = new Gson();
		obj = g.fromJson(MESSAGE_TEST, SqsMessObjUploadDoc.class);

		item = SqsMessFileObjUploadDoc.builder()
			.base64("")
			.extension("")
			.fileName("")
			.flagDataExtraction("")
			.flagSynchronous("")
			.documentalSubTypeCode("")
			.documentalTypeCode("").build();
		
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
	
		processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder()
				.base64("Base64")
				.extension("application/pdf")
				.fileName("001_TIPDOC_FS001_1000000_1.pdf")
				.build());
	}
	
	@Test
	public void constructUploadDocumentFileTest() {
		UploadDocumentFile uploadDocumentFile = sqsMessageUplDocAcqUseCase.constructUploadDocumentFile(obj, item);
		assertNotNull(uploadDocumentFile);
	}
	
	@Test
	public void constructUploadDocumentParameterTest() {
		UploadDocumentParameter uploadDocumentParameter = sqsMessageUplDocAcqUseCase.constructUploadDocumentParameter(
				acquisition, obj, processDocumentFiles);
		assertNotNull(uploadDocumentParameter);
	}
}
