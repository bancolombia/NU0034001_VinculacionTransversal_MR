package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.UtilContentStream;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class GeneratePdfDocumentUseCaseTest {

    @InjectMocks
    @Spy
    private GeneratePdfDocumentUseCaseImpl generatePdfDocumentUseCase;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private GeneratePdfUtilAmazon generatePdfUtilAmazon;

    @Mock
    private GeneratePdfUtilDocsUseCase generatePdfUtilDocsUseCase;

    private AcquisitionPdf acquisitionPdf;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        String allInfo = "{"
                + "'FIRST_NAME'= 'JUAN',"
                + "'FIRST_SURNAME'= 'PEREZ',"
                + "'GENERO_M'= 'X',"
                + "'DOCUMENT'= '10000000'"
                + "}";

        acquisitionPdf = AcquisitionPdf.builder()
                .acquisitionId(UUID.randomUUID()).typeAcquisition("VT001").allInfo(allInfo).build();
    }

    @Test
    public void configureContentStreamTest() {
        UtilContentStream utilContentStream = UtilContentStream.builder()
                .color(Color.RED).fontSize(8).fontName(PDType1Font.HELVETICA)
                .positionX(0).positionY(0).text("TEXT").build();

        generatePdfDocumentUseCase.configureContentStream(utilContentStream, new PDDocument(), new PDPage());
        verify(generatePdfDocumentUseCase, times(1)).configureContentStream(
                any(UtilContentStream.class), any(PDDocument.class), any(PDPage.class));
    }

    @Test
    public void generatePdfSuccessTest() throws IOException {
        doReturn(getCoordinates()).when(generatePdfUtilDocsUseCase).getCoordinates(anyString());
        doReturn(getInputStream()).when(dataFileRepository).retrieveFileInputStream(anyString());
        doReturn("/file/url").when(generatePdfUtilDocsUseCase).findUrlTemplate(anyString());
        doReturn(getMapResult()).when(generatePdfUtilAmazon).saveInAmazon(
                any(AcquisitionPdf.class), any(PDDocument.class), any(PDPage.class));

        Map<String, String> result = generatePdfDocumentUseCase.generatePdf(acquisitionPdf);
        assertEquals(2, result.size());
    }

    @Test
    public void generatePdfNotPresentTest() throws IOException {
        doReturn(getCoordinates()).when(generatePdfUtilDocsUseCase).getCoordinates(anyString());
        doThrow(new IOException()).when(dataFileRepository).retrieveFileInputStream(anyString());
        doReturn("/file/url").when(generatePdfUtilDocsUseCase).findUrlTemplate(anyString());
        doReturn(getMapResult()).when(generatePdfUtilAmazon).saveInAmazon(
                any(AcquisitionPdf.class), any(PDDocument.class), any(PDPage.class));

        Map<String, String> result = generatePdfDocumentUseCase.generatePdf(acquisitionPdf);
        assertNull(result);
    }

    @Test
    public void generatePdfErrorFinalizeTest() throws IOException {
        doReturn(getCoordinates()).when(generatePdfUtilDocsUseCase).getCoordinates(anyString());
        doReturn(getInputStream()).when(dataFileRepository).retrieveFileInputStream(anyString());
        doReturn("/file/url").when(generatePdfUtilDocsUseCase).findUrlTemplate(anyString());
        doThrow(new IOException()).when(generatePdfUtilAmazon).saveInAmazon(
                any(AcquisitionPdf.class), any(PDDocument.class), any(PDPage.class));

        Map<String, String> result = generatePdfDocumentUseCase.generatePdf(acquisitionPdf);
        assertNull(result);
    }

    public List<UtilContentStream> getCoordinates() {
        List<UtilContentStream> list = Arrays.asList(
                UtilContentStream.builder()
                        .color(Color.BLACK).fontSize(8).fontName(PDType1Font.HELVETICA)
                        .positionX(0).positionY(0).key("FIRST_NAME").page(0).build(),
                UtilContentStream.builder()
                        .color(Color.BLACK).fontSize(8).fontName(PDType1Font.HELVETICA)
                        .positionX(10).positionY(0).key("FIRST_SURNAME").page(0).build(),
                UtilContentStream.builder()
                        .color(Color.BLACK).fontSize(8).fontName(PDType1Font.HELVETICA)
                        .positionX(0).positionY(0).key("GENERO_M").page(1).build(),
                UtilContentStream.builder()
                        .color(Color.BLACK).fontSize(8).fontName(PDType1Font.HELVETICA)
                        .positionX(0).positionY(0).key("DOCUMENT").page(2).build(),
                UtilContentStream.builder().page(2).build());

        return list;
    }

    public Map<String, String> getMapResult() {
        Map<String, String> result = new HashMap<>();
        result.put("KEY1", "VALUE1");
        result.put("KEY2", "VALUE2");

        return result;
    }

    public InputStream getInputStream() throws IOException {
        PDDocument document = new PDDocument();
        document.addPage(new PDPage());
        document.addPage(new PDPage());
        document.addPage(new PDPage());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);

        byte[] data = out.toByteArray();
        InputStream is = new ByteArrayInputStream(data);

        return is;
    }
}
