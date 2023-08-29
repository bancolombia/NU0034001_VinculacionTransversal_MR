package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentFilesListRequest;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequest;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.model.documentretries.DocumentRetries;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentRutUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentUseCase;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class UploadDocumentControllerUtilTest {

    @InjectMocks
    @Spy
    private UploadDocumentControllerUtil uploadDocumentControllerUtil;

    @Mock
    private UploadDocumentUseCase uploadDocumentUseCase;

    @Mock
    private UploadDocumentRutUseCase uploadDocumentRutUseCase;

    @Mock
    private UploadDocumentControllerEco uploadDocumentControllerEco;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private DocumentRetriesUseCase documentRetriesUseCase;

    @Mock
    private UploadDocumentControllerFields uploadDocumentControllerFields;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void finallyStepTest() {
        doReturn(new HashMap<>()).when(uploadDocumentControllerEco).findCoincidenceCiiu(
                any(Acquisition.class), any(UploadDocumentRequestData.class));
        doNothing().when(genericStep).finallyStep(anyString(), any(InfoReuseCommon.class), anyString());

        UUID uuidCode = UUID.randomUUID();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder().acquisitionId(uuidCode.toString()).build();
        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();

        uploadDocumentControllerUtil.finallyStep(genericStep, data, acquisition, infoReuseCommon);
        verify(uploadDocumentControllerEco, times(1)).findCoincidenceCiiu(
                any(Acquisition.class), any(UploadDocumentRequestData.class));
    }

    @Test
    public void getDocumentSubTypeTest() {
        UUID uuidCode = UUID.randomUUID();
        String documentSubtype = "002";

        UploadDocumentFilesListRequest uploadDocumentFilesListRequest = UploadDocumentFilesListRequest.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode(documentSubtype)
                .flagDataExtraction("RESPUE_S")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .flagSynchronous("RESPUE_S")
                .build();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .filesList(Collections.singletonList(uploadDocumentFilesListRequest))
                .build();

        MetaRequest meta = TestUtils.buildMetaRequest("upload-document");

        UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest(meta, data);

        String documentSubtypeResponse = uploadDocumentControllerUtil.getDocumentSubtype(uploadDocumentRequest);
        assertEquals(documentSubtype, documentSubtypeResponse);
    }

    @Test
    public void getUploadDocumentWithLogRutTest() {
        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListRequest uploadDocumentFilesListRequest = UploadDocumentFilesListRequest.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("002")
                .flagDataExtraction("RESPUE_S")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .flagSynchronous("RESPUE_S")
                .build();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .filesList(Collections.singletonList(uploadDocumentFilesListRequest))
                .build();

        MetaRequest meta = TestUtils.buildMetaRequest("upload-document");
        UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest(meta, data);
        Acquisition acquisition = Acquisition.builder().documentNumber("202011100003").build();
        List<ProcessDocumentFiles> processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder().build());
        List<UploadDocumentFile> listGet = Arrays.asList(UploadDocumentFile.builder().build());

        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .infoReuseCommon(InfoReuseCommon.builder()
                        .requestReuse("")
                        .dateRequestReuse(new Date())
                        .responseReuse("")
                        .dateResponseReuse(new Date())
                        .build())
                .build();

        doReturn(uploadDocumentWithLog).when(uploadDocumentRutUseCase).processDocument(
                any(UploadDocumentParameter.class), any());
        doNothing().when(uploadDocumentControllerEco).saveProcessedDocumentsSuccessCase(
                any(UploadDocumentParameter.class), any(UploadDocumentWithLog.class), anyList());

        uploadDocumentControllerUtil.getUploadDocumentWithLog(
                acquisition, processDocumentFiles, uploadDocumentRequest, listGet);
        verify(uploadDocumentRutUseCase, times(1)).processDocument(any(UploadDocumentParameter.class), any());
    }

    @Test
    public void getUploadDocumentWithLogCCTest() {
        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListRequest uploadDocumentFilesListRequest = UploadDocumentFilesListRequest.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("001")
                .flagDataExtraction("RESPUE_S")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .flagSynchronous("RESPUE_S")
                .build();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .filesList(Collections.singletonList(uploadDocumentFilesListRequest))
                .build();

        MetaRequest meta = TestUtils.buildMetaRequest("upload-document");
        UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest(meta, data);
        Acquisition acquisition = Acquisition.builder().documentNumber("202011100003").build();
        List<ProcessDocumentFiles> processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder().build());
        List<UploadDocumentFile> listGet = Arrays.asList(UploadDocumentFile.builder().build());

        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .infoReuseCommon(InfoReuseCommon.builder()
                        .requestReuse("")
                        .dateRequestReuse(new Date())
                        .responseReuse("")
                        .dateResponseReuse(new Date())
                        .build())
                .build();

        doReturn(uploadDocumentWithLog).when(uploadDocumentUseCase).processDocument(
                any(UploadDocumentParameter.class), any());
        doNothing().when(uploadDocumentControllerEco).saveProcessedDocumentsSuccessCase(
                any(UploadDocumentParameter.class), any(UploadDocumentWithLog.class), anyList());

        uploadDocumentControllerUtil.getUploadDocumentWithLog(
                acquisition, processDocumentFiles, uploadDocumentRequest, listGet);
        verify(uploadDocumentUseCase, times(1)).processDocument(any(UploadDocumentParameter.class), any());
    }

    @Test
    public void getAcquisitionNoRetriesTest() {
        UUID uuidCode = UUID.randomUUID();

        AcquisitionReply reply = AcquisitionReply.builder()
                .acquisitionId(uuidCode.toString()).documentNumber("1061000000")
                .documentType("TIPDOC_FS001").build();

        doReturn(reply).when(vinculationUpdateUseCase).validateAcquisition(
                anyString(), anyString(), anyString(), anyString());
        doReturn(Optional.empty()).when(documentRetriesUseCase).findByAcquisition(any(Acquisition.class));

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString()).documentNumber("1061000000")
                .documentType("TIPDOC_FS001").build();

        Acquisition acquisition = uploadDocumentControllerUtil.getAcquisition(data);
        assertNotNull(acquisition);
    }

    @Test
    public void getAcquisitionRetriesTest() {
        UUID uuidCode = UUID.randomUUID();

        AcquisitionReply reply = AcquisitionReply.builder()
                .acquisitionId(uuidCode.toString()).documentNumber("1061000000")
                .documentType("TIPDOC_FS001").build();
        DocumentRetries retries = DocumentRetries.builder().build();

        doReturn(reply).when(vinculationUpdateUseCase).validateAcquisition(
                anyString(), anyString(), anyString(), anyString());
        doReturn(Optional.of(retries)).when(documentRetriesUseCase).findByAcquisition(any(Acquisition.class));

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString()).documentNumber("1061000000")
                .documentType("TIPDOC_FS001").build();

        Acquisition acquisition = uploadDocumentControllerUtil.getAcquisition(data);
        assertNotNull(acquisition);
    }

    @Test
    public void validateOptionalListTest() {
        doNothing().when(uploadDocumentControllerFields).validateOptionalList(any(UploadDocumentRequestData.class));

        UploadDocumentRequestData data = UploadDocumentRequestData.builder().build();
        uploadDocumentControllerUtil.validateOptionalList(data);
        verify(uploadDocumentControllerFields, times(1)).validateOptionalList(any(UploadDocumentRequestData.class));
    }
}
