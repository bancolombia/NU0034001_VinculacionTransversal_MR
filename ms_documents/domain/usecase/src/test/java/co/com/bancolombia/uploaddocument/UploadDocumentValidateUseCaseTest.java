package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class UploadDocumentValidateUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentValidateUseCaseImpl uploadDocumentValidateUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;

    @Mock
    private DataFileRepository dataFileRepository;
    
    @Mock
    private UploadDocumentExcepUseCase uploadDocumentExcepUseCase;

    private SqsMessageParamAllObject obj;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        obj = SqsMessageParamAllObject.builder().build();
    }

    @Test
    public void validateRutDataExtractionRequiredRutTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(true).requiredRut(true).build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();

        boolean res = uploadDocumentValidateUseCase.validateRutDataExtraction(acquisition, obj);
        assertTrue(res);
    }

    @Test
    public void validateRutDataExtractionNotValidTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(false).requiredRut(false).build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();

        boolean res = uploadDocumentValidateUseCase.validateRutDataExtraction(acquisition, obj);
        assertFalse(res);
    }

    @Test
    public void validateRutDataExtractionNotRequiredRutTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(true).requiredRut(false).build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();

        uploadDocumentValidateUseCase.validateRutDataExtraction(acquisition, obj);
        verify(vinculationUpdateUseCase, times(1)).markOperation(any(UUID.class), anyString(), anyString());
    }

    @Test
    public void getProcessDocumentFilesNotValidNameCcTest() {
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        String documentSubtype = "001";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("01")
                        .documentnumber("9999999555").fileName("001_TIPDOC_FS001_9999999555.pdf")
                        .flagDataExtraction("RESPUE_S").flagSynchronous("RESPUE_S").build());

        uploadDocumentValidateUseCase.getProcessDocumentFiles(documentSubtype, documents, obj);
        verify(uploadDocumentExcepUseCase, times(1)).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));
    }

    @Test
    public void getProcessDocumentFilesNotValidNameRutTest() {
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        String documentSubtype = "002";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("FS001")
                        .documentnumber("9999999555").fileName("002_TIPDOC_FS001_9999999555.pdf")
                        .flagDataExtraction("RESPUE_S").flagSynchronous("RESPUE_S").build());

        uploadDocumentValidateUseCase.getProcessDocumentFiles(documentSubtype, documents, obj);
        verify(uploadDocumentExcepUseCase, times(1)).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));
    }

    @Test
    public void getProcessDocumentFilesRutJpgTest() throws IOException {
        doReturn("").when(dataFileRepository).getBase64File(anyString(), anyString());

        String documentSubtype = "002";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("TIPDOC_FS001")
                        .documentnumber("9999999555").fileName("002_TIPDOC_FS001_9999999555.jpg")
                        .flagDataExtraction("RESPUE_S").flagSynchronous("RESPUE_N").build());

        List<ProcessDocumentFiles> files = uploadDocumentValidateUseCase.getProcessDocumentFiles(
                documentSubtype, documents, obj);
        assertFalse(files.isEmpty());
    }

    @Test
    public void getProcessDocumentFilesRutJpegTest() throws IOException {
        doReturn("").when(dataFileRepository).getBase64File(anyString(), anyString());

        String documentSubtype = "002";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("TIPDOC_FS001")
                        .documentnumber("9999999555").fileName("002_TIPDOC_FS001_9999999555.jpeg")
                        .flagDataExtraction("RESPUE_N").flagSynchronous("RESPUE_S").build());

        List<ProcessDocumentFiles> files = uploadDocumentValidateUseCase.getProcessDocumentFiles(
                documentSubtype, documents, obj);
        assertFalse(files.isEmpty());
    }

    @Test
    public void getProcessDocumentFilesIdentityTifTest () throws IOException {
        doReturn("").when(dataFileRepository).getBase64File(anyString(), anyString());

        String documentSubtype = "001";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("TIPDOC_FS001")
                        .documentnumber("9999999555").fileName("001_TIPDOC_FS001_9999999555.tif")
                        .flagDataExtraction("RESPUE_N").flagSynchronous("RESPUE_N").build());

        List<ProcessDocumentFiles> files = uploadDocumentValidateUseCase.getProcessDocumentFiles(
                documentSubtype, documents, obj);
        assertFalse(files.isEmpty());
    }

    @Test
    public void getProcessDocumentFilesIdentityTiffTest() throws IOException {
        doReturn("").when(dataFileRepository).getBase64File(anyString(), anyString());

        String documentSubtype = "001";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("TIPDOC_FS001")
                        .documentnumber("9999999555").fileName("001_TIPDOC_FS001_9999999555.tiff").build());

        List<ProcessDocumentFiles> files = uploadDocumentValidateUseCase.getProcessDocumentFiles(
                documentSubtype, documents, obj);
        assertFalse(files.isEmpty());
    }

    @Test
    public void getProcessDocumentFilesIdentityPngTest() throws IOException {
        doReturn("").when(dataFileRepository).getBase64File(anyString(), anyString());

        String documentSubtype = "001";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("TIPDOC_FS001")
                        .documentnumber("9999999555").fileName("001_TIPDOC_FS001_9999999555.png").build());

        List<ProcessDocumentFiles> files = uploadDocumentValidateUseCase.getProcessDocumentFiles(
                documentSubtype, documents, obj);
        assertFalse(files.isEmpty());
    }

    @Test
    public void getProcessDocumentFilesBase64CcExceptionTest() throws IOException {
        doThrow(IOException.class).when(dataFileRepository).getBase64File(anyString(), anyString());
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        String documentSubtype = "001";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("TIPDOC_FS001")
                        .documentnumber("9999999555").fileName("001_TIPDOC_FS001_9999999555.png").build());

        List<ProcessDocumentFiles> files = uploadDocumentValidateUseCase.getProcessDocumentFiles(
                documentSubtype, documents, obj);
        assertFalse(files.isEmpty());
    }

    @Test
    public void getProcessDocumentFilesBase64RutExceptionTest() throws IOException {
        doThrow(IOException.class).when(dataFileRepository).getBase64File(anyString(), anyString());
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        String documentSubtype = "002";
        List<UploadDocumentFile> documents = Arrays.asList(
                UploadDocumentFile.builder()
                        .documentalSubTypeCode(documentSubtype).documentType("TIPDOC_FS001")
                        .documentnumber("9999999555").fileName("002_TIPDOC_FS001_9999999555.png").build());

        List<ProcessDocumentFiles> files = uploadDocumentValidateUseCase.getProcessDocumentFiles(
                documentSubtype, documents, obj);
        assertFalse(files.isEmpty());
    }
}
