package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class PersistenceValidateTypeDocumentTest {

    @InjectMocks
    @Spy
    PersistenceValidateTypeDocumentImpl persistenceValidateTypeDocument;

    @Mock
    private PdfMerger pdfMerger;
    @Mock
    private ImageConverter imageConverter;
    @Mock
    private DataFileRepository dataFileRepositoryAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getBase64DiffExtensionPDFTest() throws IOException {
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFile = TdcDocumentsFile.builder()
                .documentalSubTypeCode("001").documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocument tdcDocument = TdcDocument.builder().build();
        doReturn("asd").when(pdfMerger).mergePdf(anyList());
        String result = persistenceValidateTypeDocument.getBase64DiffExtension(tdcDocumentsFile,tdcDocument);
        assertNotNull(result);
    }

    @Test
    public void getBase64DiffExtensionJpegTest() throws IOException {
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.jpeg");
        fileNameList.add(1, "NameTwo.jpeg");

        TdcDocumentsFile tdcDocumentsFile = TdcDocumentsFile.builder()
                .documentalSubTypeCode("001").documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocument tdcDocument = TdcDocument.builder().build();
        doReturn("asd").when(imageConverter).convert(any(byte[].class),any(byte[].class));

        byte[] byteOne = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 5, 0, 0, 0, 4, 8, 6,
                0, 0, 0, 70, 51, -11, 64, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 103, 65, 77, 65,
                0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -60, 0, 0, 14, -60, 1, -107, 43,
                14, 27, 0, 0, 0, 69, 73, 68, 65, 84, 24, 87, 99, 0, -126, -1, 32, 60, 115, -26, -52, -1, 108, 108, 108,
                96, 54, 88, 112, -61, -122, 13, -1, 63, 126, -4, -8, -1, -40, -79, 99, 16, -63, -128, -128, -128, -1,
                -56, 32, 53, 53, -11, 63, -45, -81, 95, -65, 24, 56, 57, 57, 25, -26, -51, -101, -57, 0, -44, -50, -16,
                -11, -21, 87, 6, 0, -72, -26, 42, -16, -60, -110, -16, -123, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};

        doReturn(byteOne).when(dataFileRepositoryAdapter).getBase64Bytes(anyString(),anyString());

        String result = persistenceValidateTypeDocument.getBase64DiffExtension(tdcDocumentsFile,tdcDocument);
        assertNotNull(result);
    }

    @Test
    public void getBase64DiffExtensionOtherTest() throws IOException {
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.gif");
        fileNameList.add(1, "NameTwo.gif");

        TdcDocumentsFile tdcDocumentsFile = TdcDocumentsFile.builder()
                .documentalSubTypeCode("001").documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocument tdcDocument = TdcDocument.builder().build();

        byte[] byteOne = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 5, 0, 0, 0, 4, 8, 6,
                0, 0, 0, 70, 51, -11, 64, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 103, 65, 77, 65,
                0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -60, 0, 0, 14, -60, 1, -107, 43,
                14, 27, 0, 0, 0, 69, 73, 68, 65, 84, 24, 87, 99, 0, -126, -1, 32, 60, 115, -26, -52, -1, 108, 108, 108,
                96, 54, 88, 112, -61, -122, 13, -1, 63, 126, -4, -8, -1, -40, -79, 99, 16, -63, -128, -128, -128, -1,
                -56, 32, 53, 53, -11, 63, -45, -81, 95, -65, 24, 56, 57, 57, 25, -26, -51, -101, -57, 0, -44, -50, -16,
                -11, -21, 87, 6, 0, -72, -26, 42, -16, -60, -110, -16, -123, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};

        doReturn(byteOne).when(dataFileRepositoryAdapter).getBase64Bytes(anyString(),anyString());
        doReturn("asd").when(imageConverter).convertTIFFToPDF(anyList());
        String result = persistenceValidateTypeDocument.getBase64DiffExtension(tdcDocumentsFile,tdcDocument);
        assertNotNull(result);
    }
}
