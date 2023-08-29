package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.api.model.uploaddocument.UploadDocumentFilesListRequest;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequestData;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class UploadDocumentControllerFieldsTest {

    @InjectMocks
    @Spy
    private UploadDocumentControllerFields uploadDocumentControllerFields;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateOptionalListTest() {

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

        uploadDocumentControllerFields.validateOptionalList(data);
        verify(uploadDocumentControllerFields, times(1)).validateOptionalList(any(UploadDocumentRequestData.class));
    }
}
