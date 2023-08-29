package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.digitalizationprocesseddocuments.DigitalizationProcessedDocumentsUseCase;
import co.com.bancolombia.model.digitalizationprocesseddocuments.DigitalizationProcessedDocuments;
import co.com.bancolombia.model.sqs.SqsMessDataObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessFileObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class UploadDocumentProcessedDocumentsUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentProcessedDocumentsUseCaseImpl uploadDocumentProcessedDocumentsUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private DigitalizationProcessedDocumentsUseCase digitalizationProcessedDocumentsUseCase;

    @Mock
    private UploadDocumentSyncUseCase uploadDocumentSyncUseCase;

    @Mock
    private DataFileRepository dataFileRepository;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void saveProcessedDocumentsUploadDocumentsTest() {
        List<UploadDocumentFile> uploadDocumentFiles = Arrays.asList(UploadDocumentFile.builder().build());
        UploadDocumentParameter uploadDocumentParameter = UploadDocumentParameter.builder()
                .listGet(uploadDocumentFiles).build();

        Date date = new Date();
        Map<String, String> values = new HashMap<>();

        doReturn(date).when(coreFunctionDate).getDatetime();
        doReturn(DigitalizationProcessedDocuments.builder().build()).when(digitalizationProcessedDocumentsUseCase)
                .save(any(DigitalizationProcessedDocuments.class));
        doReturn(true).when(dataFileRepository).moveBucketObject(anyString(), anyString());

        uploadDocumentProcessedDocumentsUseCase.saveProcessedDocuments(uploadDocumentParameter, date, null, values);
        verify(digitalizationProcessedDocumentsUseCase, times(1))
                .save(any(DigitalizationProcessedDocuments.class));
    }

    @Test
    public void saveProcessedDocumentsProcessDocumentsTest() {
        List<ProcessDocumentFiles> processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder().build());
        UploadDocumentParameter uploadDocumentParameter = UploadDocumentParameter.builder()
                .processDocumentFiles(processDocumentFiles).build();

        Date date = new Date();
        Map<String, String> values = new HashMap<>();
        values.put("flagDataExtraction", "value");

        doReturn(date).when(coreFunctionDate).getDatetime();
        doReturn(DigitalizationProcessedDocuments.builder().build()).when(digitalizationProcessedDocumentsUseCase)
                .save(any(DigitalizationProcessedDocuments.class));
        doReturn(true).when(dataFileRepository).moveBucketObject(anyString(), anyString());

        uploadDocumentProcessedDocumentsUseCase.saveProcessedDocuments(uploadDocumentParameter, date, null, values);
        verify(digitalizationProcessedDocumentsUseCase, times(1))
                .save(any(DigitalizationProcessedDocuments.class));
    }

    @Test
    public void saveProcessedDocumentsSqsParamNullDocsTest() {
        SqsMessDataObjUploadDoc data = SqsMessDataObjUploadDoc.builder()
                .files(Arrays.asList(SqsMessFileObjUploadDoc.builder().build())).build();
        SqsMetaUploadDocument meta = SqsMetaUploadDocument.builder().build();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().data(data).meta(meta).build();

        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        UploadDocumentApiResponse apiResponse = UploadDocumentApiResponse.builder()
                .data(UploadDocumentWithLog.builder().infoReuseCommon(infoReuseCommon).build()).build();

        SqsMessageParamAllObject sqsMsgParam = SqsMessageParamAllObject.builder()
                .sqsMessObjUploadDoc(obj).uploadDocumentApiResponse(apiResponse).build();
        UploadDocumentParameter uploadDocumentParameter = UploadDocumentParameter.builder().build();

        Date date = new Date();
        Map<String, String> values = new HashMap<>();

        doReturn(date).when(coreFunctionDate).getDatetime();
        doReturn(DigitalizationProcessedDocuments.builder().build()).when(digitalizationProcessedDocumentsUseCase)
                .save(any(DigitalizationProcessedDocuments.class));
        doReturn(true).when(dataFileRepository).moveBucketObject(anyString(), anyString());

        uploadDocumentProcessedDocumentsUseCase.saveProcessedDocuments(
                uploadDocumentParameter, date, sqsMsgParam, values);
        verify(digitalizationProcessedDocumentsUseCase, times(1))
                .save(any(DigitalizationProcessedDocuments.class));
    }

    @Test
    public void saveProcessedDocumentsSqsParamEmptyMapTest() {
        SqsMessDataObjUploadDoc data = SqsMessDataObjUploadDoc.builder()
                .files(Arrays.asList(SqsMessFileObjUploadDoc.builder().build())).build();
        SqsMetaUploadDocument meta = SqsMetaUploadDocument.builder().build();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().data(data).meta(meta).build();

        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        UploadDocumentApiResponse apiResponse = UploadDocumentApiResponse.builder()
                .data(UploadDocumentWithLog.builder().infoReuseCommon(infoReuseCommon).build()).build();

        List<ProcessDocumentFiles> processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder().build());

        SqsMessageParamAllObject sqsMsgParam = SqsMessageParamAllObject.builder()
                .sqsMessObjUploadDoc(obj).uploadDocumentApiResponse(apiResponse).build();
        UploadDocumentParameter uploadDocumentParameter = UploadDocumentParameter.builder()
                .processDocumentFiles(processDocumentFiles).build();

        Date date = new Date();
        Map<String, String> values = new HashMap<>();

        doReturn(date).when(coreFunctionDate).getDatetime();
        doReturn(DigitalizationProcessedDocuments.builder().build()).when(digitalizationProcessedDocumentsUseCase)
                .save(any(DigitalizationProcessedDocuments.class));
        doReturn(true).when(dataFileRepository).moveBucketObject(anyString(), anyString());

        uploadDocumentProcessedDocumentsUseCase.saveProcessedDocuments(
                uploadDocumentParameter, date, sqsMsgParam, values);
        verify(digitalizationProcessedDocumentsUseCase, times(1))
                .save(any(DigitalizationProcessedDocuments.class));
    }

    @Test
    public void validateAsynchronousProcessTest() {
        doReturn(true).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));

        boolean res = uploadDocumentProcessedDocumentsUseCase.validateAsynchronousProcess(Collections.emptyList());
        assertTrue(res);
    }
}
