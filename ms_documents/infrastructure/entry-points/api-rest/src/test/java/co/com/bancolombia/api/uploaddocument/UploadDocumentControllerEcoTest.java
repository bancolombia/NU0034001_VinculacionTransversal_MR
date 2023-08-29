package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.api.model.uploaddocument.UploadDocumentFilesListRequest;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentProcessedDocumentsUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COINCIDENCE_CIIU_FIELD;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT_APPLY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class UploadDocumentControllerEcoTest {

    @InjectMocks
    @Spy
    private UploadDocumentControllerEco uploadDocumentControllerEco;

    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;

    @Mock
    private UploadDocumentProcessedDocumentsUseCase uploadDocumentProcessedDocumentsUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findCoincidenceCiiuRutValidInfoTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(true).ciiu("CIIU_08299").build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));

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

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();

        uploadDocumentControllerEco.findCoincidenceCiiu(acquisition, data);
        verify(naturalPersonUseCase, times(1)).getRequiredRut(any(UUID.class));
    }

    @Test
    public void findCoincidenceCiiuRutNullCiiuTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(true).build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));

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

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();

        Map<String, String> map = uploadDocumentControllerEco.findCoincidenceCiiu(acquisition, data);
        assertEquals(NOT_APPLY, map.get(COINCIDENCE_CIIU_FIELD));
    }

    @Test
    public void findCoincidenceCiiuRutNotValidTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(false).build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));

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

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();

        Map<String, String> map = uploadDocumentControllerEco.findCoincidenceCiiu(acquisition, data);
        assertEquals(NOT_APPLY, map.get(COINCIDENCE_CIIU_FIELD));
    }

    @Test
    public void findCoincidenceCiiuCcTest() {
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

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();

        uploadDocumentControllerEco.findCoincidenceCiiu(acquisition, data);
        verify(naturalPersonUseCase, never()).getRequiredRut(any(UUID.class));
    }

    @Test
    public void saveProcessedDocumentsSuccessCaseAsynchronousTest() {
        doReturn(true).when(uploadDocumentProcessedDocumentsUseCase).validateAsynchronousProcess(anyList());

        UploadDocumentParameter docParameter = UploadDocumentParameter.builder().build();
        UploadDocumentWithLog docWithLog = UploadDocumentWithLog.builder().build();
        List<UploadDocumentFile> listGet = new ArrayList<>();

        uploadDocumentControllerEco.saveProcessedDocumentsSuccessCase(docParameter, docWithLog, listGet);
        verify(uploadDocumentProcessedDocumentsUseCase, never()).saveProcessedDocuments(
                any(UploadDocumentParameter.class), any(Date.class), any(SqsMessageParamAllObject.class), anyMap());
    }

    @Test
    public void saveProcessedDocumentsSuccessCaseSynchronousTest() {
        doReturn(false).when(uploadDocumentProcessedDocumentsUseCase).validateAsynchronousProcess(anyList());

        UploadDocumentParameter docParameter = UploadDocumentParameter.builder().build();
        UploadDocumentWithLog docWithLog = UploadDocumentWithLog.builder()
                .infoReuseCommon(InfoReuseCommon.builder().dateResponseReuse(new Date()).build()).build();
        List<UploadDocumentFile> listGet = new ArrayList<>();

        uploadDocumentControllerEco.saveProcessedDocumentsSuccessCase(docParameter, docWithLog, listGet);
        verify(uploadDocumentProcessedDocumentsUseCase, times(1)).saveProcessedDocuments(
                any(UploadDocumentParameter.class), any(Date.class), any(SqsMessageParamAllObject.class), anyMap());
    }
}
