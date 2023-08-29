package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import com.google.common.io.ByteSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class PdfMergeTest {

    @InjectMocks
    @Spy
    PdfMerger pdfMerger;

    @Mock
    private DataFileRepository dataFileRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mergePdfTest() throws IOException {
        List<String> fileNames = Arrays.asList("1.png", "2.png");
        byte[] byteOne = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 5, 0, 0, 0, 4, 8, 6,
                0, 0, 0, 70, 51, -11, 64, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 103, 65, 77, 65,
                0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -60, 0, 0, 14, -60, 1, -107, 43,
                14, 27, 0, 0, 0, 69, 73, 68, 65, 84, 24, 87, 99, 0, -126, -1, 32, 60, 115, -26, -52, -1, 108, 108, 108,
                96, 54, 88, 112, -61, -122, 13, -1, 63, 126, -4, -8, -1, -40, -79, 99, 16, -63, -128, -128, -128, -1,
                -56, 32, 53, 53, -11, 63, -45, -81, 95, -65, 24, 56, 57, 57, 25, -26, -51, -101, -57, 0, -44, -50, -16,
                -11, -21, 87, 6, 0, -72, -26, 42, -16, -60, -110, -16, -123, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        InputStream targetStream = ByteSource.wrap(byteOne).openStream();
        Mockito.doReturn(new File("prueba")).when(dataFileRepository).convertInputStreamToFile(any(InputStream.class),
                any(File.class));
        Mockito.doReturn(targetStream).when(dataFileRepository).getInputStreamOfFile(anyString(), anyString());

        String base = pdfMerger.mergePdf(fileNames);
        assertNotNull(base);
    }

    @Test
    public void deleteFilesTest() throws IOException {
        byte[] byteOne = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 5, 0, 0, 0, 4, 8, 6,
                0, 0, 0, 70, 51, -11, 64, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 103, 65, 77, 65,
                0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -60, 0, 0, 14, -60, 1, -107, 43,
                14, 27, 0, 0, 0, 69, 73, 68, 65, 84, 24, 87, 99, 0, -126, -1, 32, 60, 115, -26, -52, -1, 108, 108, 108,
                96, 54, 88, 112, -61, -122, 13, -1, 63, 126, -4, -8, -1, -40, -79, 99, 16, -63, -128, -128, -128, -1,
                -56, 32, 53, 53, -11, 63, -45, -81, 95, -65, 24, 56, 57, 57, 25, -26, -51, -101, -57, 0, -44, -50, -16,
                -11, -21, 87, 6, 0, -72, -26, 42, -16, -60, -110, -16, -123, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        InputStream targetStream = ByteSource.wrap(byteOne).openStream();
        File file = new File("prueba");
        FileUtils.copyInputStreamToFile(targetStream, file);
        pdfMerger.deleteFiles(file,file,file);
        Mockito.verify(this.pdfMerger, Mockito.times(1))
                .deleteFiles(file,file,file);
    }
}
