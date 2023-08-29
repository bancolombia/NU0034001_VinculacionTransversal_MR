package co.com.bancolombia.api.validatedataextraction;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentApiResponseData;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentFilesListResponse;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COINCIDENCE_CIIU_FIELD;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT_APPLY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ValidateDataExtractionCoincidenceCiiuValidateTest {

    @InjectMocks
    @Spy
    private ValidateDataExtractionCoincidenceCiiuValidate validateCiiu;

    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findCoincidenceCiiuRutValidInfoTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(true).ciiu("CIIU_08299").build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));

        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListResponse uploadDocumentFilesList = UploadDocumentFilesListResponse.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("002")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .build();

        UploadDocumentApiResponseData data = UploadDocumentApiResponseData.builder()
                .processedDocuments(Collections.singletonList(uploadDocumentFilesList))
                .build();

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();

        validateCiiu.findCoincidenceCiiu(acquisition, data);
        verify(naturalPersonUseCase, times(1)).getRequiredRut(any(UUID.class));
    }

    @Test
    public void findCoincidenceCiiuRutNullCiiuTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(true).build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));

        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListResponse uploadDocumentFilesList = UploadDocumentFilesListResponse.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("002")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .build();

        UploadDocumentApiResponseData data = UploadDocumentApiResponseData.builder()
                .processedDocuments(Collections.singletonList(uploadDocumentFilesList))
                .build();

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();

        Map<String, String> map = validateCiiu.findCoincidenceCiiu(acquisition, data);
        assertEquals(NOT_APPLY, map.get(COINCIDENCE_CIIU_FIELD));
    }

    @Test
    public void findCoincidenceCiiuRutNotValidTest() {
        InfoRutReply reply = InfoRutReply.builder().valid(false).build();
        doReturn(reply).when(naturalPersonUseCase).getRequiredRut(any(UUID.class));

        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListResponse uploadDocumentFilesList = UploadDocumentFilesListResponse.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("002")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .build();

        UploadDocumentApiResponseData data = UploadDocumentApiResponseData.builder()
                .processedDocuments(Collections.singletonList(uploadDocumentFilesList))
                .build();

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();

        Map<String, String> map = validateCiiu.findCoincidenceCiiu(acquisition, data);
        assertEquals(NOT_APPLY, map.get(COINCIDENCE_CIIU_FIELD));
    }

    @Test
    public void findCoincidenceCiiuCcTest() {
        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListResponse uploadDocumentFilesList = UploadDocumentFilesListResponse.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("001")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .build();

        UploadDocumentApiResponseData data = UploadDocumentApiResponseData.builder()
                .processedDocuments(Collections.singletonList(uploadDocumentFilesList))
                .build();

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("202011100003").build();

        validateCiiu.findCoincidenceCiiu(acquisition, data);
        verify(naturalPersonUseCase, never()).getRequiredRut(any(UUID.class));
    }
}
