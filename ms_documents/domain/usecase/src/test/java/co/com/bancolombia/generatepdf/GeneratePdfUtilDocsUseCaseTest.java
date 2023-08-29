package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.UtilContentStream;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.pdfobject.PdfObject;
import co.com.bancolombia.model.pdfobject.gateway.PdfObjectRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GeneratePdfUtilDocsUseCaseTest {

	@InjectMocks
	@Spy
	private GeneratePdfUtilDocsUseCaseImpl generatePdfUtilDocsUseCase;

	@Mock
	private ParametersRepository parametersRepository;

	@Mock
	private PdfObjectRepository pdfObjectRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findUrlTemplatePresentTest() {
		Optional<Parameters> parameters = Optional.of(Parameters.builder().name("file/url").build());
		doReturn(parameters).when(parametersRepository).findByCodeAndParent(anyString(), anyString());

		String url = generatePdfUtilDocsUseCase.findUrlTemplate("VT001");
		assertEquals("file/url", url);
	}

	@Test
	public void findUrlTemplateNotPresentTest() {
		doReturn(Optional.empty()).when(parametersRepository).findByCodeAndParent(anyString(), anyString());

		String url = generatePdfUtilDocsUseCase.findUrlTemplate("VT001");
		assertEquals("", url);
	}

	@Test
	public void getCoordinatesSuccessTest() {
		List<PdfObject> listObjectPdf = Arrays.asList(
				PdfObject.builder()
						.uuid(UUID.randomUUID()).key("").page(0).color("BLACK")
						.fontSize(11).fontName("HELVETICA").positionX(0).positionY(0)
						.typeAcquisition("VT001").build());

		doReturn(listObjectPdf).when(pdfObjectRepository).findByTypeAcquisition(anyString());

		List<UtilContentStream> list = generatePdfUtilDocsUseCase.getCoordinates("VT001");
		assertEquals(1, list.size());
	}

	@Test()
	public void getCoordinatesNotValidPropertiesTest() {
		List<PdfObject> listObjectPdf = Arrays.asList(
				PdfObject.builder()
						.uuid(UUID.randomUUID()).key("").page(0).color("KKKKK")
						.fontSize(11).fontName("KKKKK").positionX(0).positionY(0)
						.typeAcquisition("TYPE_ACQUISITION").build());

		doReturn(listObjectPdf).when(pdfObjectRepository).findByTypeAcquisition(anyString());

		List<UtilContentStream> list = generatePdfUtilDocsUseCase.getCoordinates("VT001");
		assertEquals(0, list.size());
	}
}
