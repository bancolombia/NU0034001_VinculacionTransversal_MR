package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ImageConverterTest {

    @InjectMocks
    @Spy
    ImageConverter imageConverter;

    @Mock
    DataFileRepository dataFileRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convertImageTest() {
        byte[] byteOne = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 5, 0, 0, 0, 4, 8, 6,
                0, 0, 0, 70, 51, -11, 64, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 103, 65, 77, 65,
                0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -60, 0, 0, 14, -60, 1, -107, 43,
                14, 27, 0, 0, 0, 69, 73, 68, 65, 84, 24, 87, 99, 0, -126, -1, 32, 60, 115, -26, -52, -1, 108, 108, 108,
                96, 54, 88, 112, -61, -122, 13, -1, 63, 126, -4, -8, -1, -40, -79, 99, 16, -63, -128, -128, -128, -1,
                -56, 32, 53, 53, -11, 63, -45, -81, 95, -65, 24, 56, 57, 57, 25, -26, -51, -101, -57, 0, -44, -50, -16,
                -11, -21, 87, 6, 0, -72, -26, 42, -16, -60, -110, -16, -123, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        byte[] byteTwo = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 5, 0, 0, 0, 4, 8, 6,
                0, 0, 0, 70, 51, -11, 64, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 103, 65, 77, 65,
                0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -60, 0, 0, 14, -60, 1, -107, 43,
                14, 27, 0, 0, 0, 69, 73, 68, 65, 84, 24, 87, 99, 0, -126, -1, 32, 60, 115, -26, -52, -1, 108, 108, 108,
                96, 54, 88, 112, -61, -122, 13, -1, 63, 126, -4, -8, -1, -40, -79, 99, 16, -63, -128, -128, -128, -1,
                -56, 32, 53, 53, -11, 63, -45, -81, 95, -65, 24, 56, 57, 57, 25, -26, -51, -101, -57, 0, -44, -50, -16,
                -11, -21, 87, 6, 0, -72, -26, 42, -16, -60, -110, -16, -123, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        this.imageConverter.convert(byteOne, byteTwo);
        Mockito.verify(this.imageConverter, Mockito.times(1))
                .convert(byteOne, byteTwo);
    }

    @Test(expected = com.itextpdf.text.ExceptionConverter.class)
    public void convertTiffTest() throws IOException {
        byte[] byteOne = {01, 02};
        byte[] byteTwo = {06, 03};

        List<byte[]> byteList = new ArrayList<>();
        byteList.add(byteOne);
        byteList.add(byteTwo);

        this.imageConverter.convertTIFFToPDF(byteList);
        Mockito.verify(this.imageConverter, Mockito.times(1))
                .convertTIFFToPDF(byteList);
    }

    @Test
    public void convertImageExceptionTest() {
        byte[] byteOne = {01, 02};
        byte[] byteTwo = {06, 03};
        this.imageConverter.convert(byteOne, byteTwo);
        Mockito.verify(this.imageConverter, Mockito.times(1))
                .convert(byteOne, byteTwo);
    }
}
