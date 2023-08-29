package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.UtilContentStream;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.uploadedfile.DataFile;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.util.constants.Constants.NAME_FILE;
import static co.com.bancolombia.util.constants.Constants.RUTA_ARCHIVO_CLIENTE;
import static co.com.bancolombia.util.constants.Constants.RUTA_ARCHIVO_CUSTODIA;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class GeneratePdfUtilAmazonTest {

    @InjectMocks
    @Spy
    private GeneratePdfUtilAmazonImpl generatePdfUtilAmazon;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private GeneratePdfDocumentUseCase generatePdfDocumentUseCase;

    @Mock
    private ParametersRepository parametersRepository;

    @Mock
    private GenPdfA1B genPdfA1B;

    @Mock
    private PDDocument document;

    private AcquisitionPdf acquisitionPdf;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        acquisitionPdf = AcquisitionPdf.builder()
                .documentNumber("123456").documentType("TIPDOC_FS001").build();
    }

    @Test
    public void saveInAmazonSuccessTest() throws IOException {
        DataFile dataFile = DataFile.builder()
                .folder("/ruta/archivo/custodia").name("archivo").build();

        doReturn(getParameters()).when(parametersRepository).findByParent(anyString());
        doReturn(new PDDocument()).when(genPdfA1B).convertPdfA1B(any(PDDocument.class));
        doNothing().when(generatePdfDocumentUseCase).configureContentStream(
                any(UtilContentStream.class), any(PDDocument.class), any(PDPage.class));
        doReturn(dataFile).when(dataFileRepository).save(any(DataFile.class));

        PDPage page = new PDPage();

        Map<String, String> result = generatePdfUtilAmazon.saveInAmazon(acquisitionPdf, document, page);
        assertEquals(2, result.size());
    }

    @Test
    public void saveInAmazonExceptionTest() throws IOException {
        DataFile dataFile = DataFile.builder()
                .folder("/ruta/archivo/custodia").name("archivo").build();

        doReturn(getParameters()).when(parametersRepository).findByParent(anyString());
        doReturn(document).when(genPdfA1B).convertPdfA1B(any(PDDocument.class));
        doNothing().when(generatePdfDocumentUseCase).configureContentStream(
                any(UtilContentStream.class), any(PDDocument.class), any(PDPage.class));
        doThrow(IOException.class).when(document).protect(any(StandardProtectionPolicy.class));
        doReturn(dataFile).when(dataFileRepository).save(any(DataFile.class));

        PDPage page = new PDPage();

        Map<String, String> result = generatePdfUtilAmazon.saveInAmazon(acquisitionPdf, document, page);
        assertEquals(2, result.size());
    }

    private List<Parameters> getParameters() {
        List<Parameters> parameters = Arrays.asList(
                Parameters.builder().code(RUTA_ARCHIVO_CUSTODIA).name("/ruta/archivo/custodia").build(),
                Parameters.builder().code(RUTA_ARCHIVO_CLIENTE).name("/ruta/archivo/cliente").build(),
                Parameters.builder().code(NAME_FILE).name("archivo").build());

        return parameters;
    }
}
