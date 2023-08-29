package co.com.bancolombia.sqs;

import co.com.bancolombia.commonsvnt.api.model.util.Meta;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.model.sqs.SqsMessDataObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessFileObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import co.com.bancolombia.model.uploaddocument.ProcessedDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SqsMessageUplDocUtilUseCaseTest {

	@InjectMocks
	@Spy
	private SqsMessageUplDocUtilUseCaseImpl sqsMessageUplDocUtilUseCase;
	
	@Mock
	private SqsMessageUplDocAcqUseCase sqsMessageUplDocAcqUseCase;
	
	private UploadDocumentResponse upd;

    private Meta meta;

    private UploadDocument data;

	private SqsMetaUploadDocument metaSqs;

	private SqsMessObjUploadDoc obj;

	private SqsMessDataObjUploadDoc dataSqsMessData;

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
			+ "        'base64': null,"
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
	
	private String bodyMessage;

	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        meta = new Meta();

        data = UploadDocument.builder()
        		.processedDocument(Collections.singletonList(ProcessedDocument.builder().build()))
        		.build();

        upd = UploadDocumentResponse.builder()
        		.meta(meta)
        		.data(data)
        		.build();

		metaSqs = SqsMetaUploadDocument.builder()
				.usrMod("")
				.ip("")
				.systemId("")
				.messageId("")
				.version("")
				.requestDate("")
				.service("")
				.operation("").build();

		dataSqsMessData = SqsMessDataObjUploadDoc.builder()
				.files(Collections.singletonList(SqsMessFileObjUploadDoc.builder().build()))
				.build();
		
		obj = SqsMessObjUploadDoc.builder()
				.meta(metaSqs)
				.data(dataSqsMessData)
				.build();

		Gson g = new Gson();
		bodyMessage = g.toJson(MESSAGE_TEST);
	}
	
	@Test
	public void returnJsonTest() {
		String json = sqsMessageUplDocUtilUseCase.returnJson(upd);
		assertNotNull(json);
	}
	
	@Test
	public void castMetaTest() {
		MetaRequest metaRequest = sqsMessageUplDocUtilUseCase.castMeta(metaSqs);
		assertNotNull(metaRequest);
	}

	@Test
	public void forEachObjectJsonFieldsNotSaveAddedTest() {
		List<String> fieldsCanNotSave = Arrays.asList("files");
		String json = sqsMessageUplDocUtilUseCase.forEachObjectJson(MESSAGE_TEST, fieldsCanNotSave);
		assertNotNull(json);
	}

	@Test
	public void forEachObjectJsonFieldsNotSaveNullTest() {
		String json = sqsMessageUplDocUtilUseCase.forEachObjectJson(MESSAGE_TEST, null);
		assertNotNull(json);
	}

	@Test
	public void forEachObjectJsonExceptionTest() {
		List<String> fieldsCanNotSave = Arrays.asList("files");
		String json = sqsMessageUplDocUtilUseCase.forEachObjectJson(bodyMessage, fieldsCanNotSave);
		assertEquals(bodyMessage, json);
	}

	@Test
	public void goThroughListTest() {
		List<String> fieldsCanNotSave = Arrays.asList("filess");

		JsonArray body = new JsonArray();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(bodyMessage);
		body.add(element);

		String response = sqsMessageUplDocUtilUseCase.goThroughList(body, fieldsCanNotSave);
		assertNotNull(response);
	}
	
	@Test
	public void buildUploadDocumentResponseNullReasonTest() {
		upd.getData().getProcessedDocument().get(0).setReason(null);
		String json = sqsMessageUplDocUtilUseCase.buildUploadDocumentResponse(obj, upd);
		assertNotNull(json);
	}

	@Test
	public void buildUploadDocumentResponseSetReasonTest() {
		upd.getData().getProcessedDocument().get(0).setReason("001");
		String json = sqsMessageUplDocUtilUseCase.buildUploadDocumentResponse(obj, upd);
		assertNotNull(json);
	}
	
	@Test
	public void constructSqsMessObjUploadDocTest() {
		SqsMessObjUploadDoc sqsMess = sqsMessageUplDocUtilUseCase.constructSqsMessObjUploadDoc("");
		assertNull(sqsMess);
	}
	
	@Test
	public void getListGetTest() {
		doReturn(UploadDocumentFile.builder().build()).when(sqsMessageUplDocAcqUseCase).constructUploadDocumentFile(
				any(SqsMessObjUploadDoc.class), any(SqsMessFileObjUploadDoc.class));

		List<UploadDocumentFile> listGet = sqsMessageUplDocUtilUseCase.getListGet(obj);
		assertEquals(obj.getData().getFiles().size(), listGet.size());
	}
}
