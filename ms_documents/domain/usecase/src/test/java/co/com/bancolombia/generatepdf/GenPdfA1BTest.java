package co.com.bancolombia.generatepdf;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class GenPdfA1BTest {

    @InjectMocks
    @Spy
    private GenPdfA1BImpl genPdfA1B;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convertPdfA1BSuccessTest() {
        PDDocument document = genPdfA1B.convertPdfA1B(new PDDocument());
        assertNotNull(document);
    }
}
