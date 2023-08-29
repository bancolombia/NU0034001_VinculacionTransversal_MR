package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.GeneratePdf;
import co.com.bancolombia.model.generatepdf.GeneratePdfResponseData;
import co.com.bancolombia.model.generatepdf.gateways.GeneratePdfRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GeneratePdfUseCaseTest {

	@InjectMocks
	@Spy
	private GeneratePdfUseCaseImpl generatePdfUseCase;

	@Mock
	private GeneratePdfProcessUseCase generatePdfProcessUseCase;

	@Mock
	private GeneratePdfUtilTwoUseCase generatePdfUtilTwoUseCase;

	@Mock
	private GeneratePdfRepository generatePdfRepository;

	@Mock
	private CoreFunctionDate coreFunctionDate;

	private Acquisition acquisition;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		acquisition = Acquisition.builder()
				.id(UUID.randomUUID())
				.documentNumber("10000000")
				.firstName("JUAN")
				.secondName("JOSE")
				.firstSurname("NEGRO")
				.secondSurname("BLANCO")
				.documentType(DocumentType.builder().code("TIPDOC_FS001").build())
				.build();
	}

	@Test
	public void generatePdfSuccessTest() {
		Optional<AcquisitionPdf> acquisitionPdf = Optional.of(AcquisitionPdf.builder().build());
		GeneratePdfResponseData responseData = GeneratePdfResponseData.builder().build();
		GeneratePdf generatePdf = GeneratePdf.builder().build();

		doReturn(acquisitionPdf).when(generatePdfUtilTwoUseCase).validateAndFindInfo(any(Acquisition.class));
		doReturn(new Date()).when(coreFunctionDate).getDatetime();
		doReturn(responseData).when(generatePdfProcessUseCase).generatePdf(any(AcquisitionPdf.class));
		doReturn(generatePdf).when(generatePdfRepository).save(any(GeneratePdf.class));

		GeneratePdf pdf = generatePdfUseCase.generatePdf(acquisition, "USER");
		assertNotNull(pdf);
	}
	
	@Test
	public void generatePdfNotPresentTest() {
		GeneratePdf generatePdf = GeneratePdf.builder().build();

		doReturn(Optional.empty()).when(generatePdfUtilTwoUseCase).validateAndFindInfo(any(Acquisition.class));
		doReturn(new Date()).when(coreFunctionDate).getDatetime();
		doReturn(generatePdf).when(generatePdfRepository).save(any(GeneratePdf.class));

		GeneratePdf pdf = generatePdfUseCase.generatePdf(acquisition, "USER");
		assertNotNull(pdf);
	}

	@Test
	public void generatePdfNullResponseTest() {
		Optional<AcquisitionPdf> acquisitionPdf = Optional.of(AcquisitionPdf.builder().build());
		GeneratePdf generatePdf = GeneratePdf.builder().build();

		doReturn(acquisitionPdf).when(generatePdfUtilTwoUseCase).validateAndFindInfo(any(Acquisition.class));
		doReturn(new Date()).when(coreFunctionDate).getDatetime();
		doReturn(null).when(generatePdfProcessUseCase).generatePdf(any(AcquisitionPdf.class));
		doReturn(generatePdf).when(generatePdfRepository).save(any(GeneratePdf.class));

		GeneratePdf pdf = generatePdfUseCase.generatePdf(acquisition, "USER");
		assertNotNull(pdf);
	}

	@Test
	public void findByAcquisitionTest() {
		GeneratePdf generatePdf = GeneratePdf.builder().build();
		doReturn(generatePdf).when(generatePdfRepository).findTopByAcquisitionOrderByCreatedDateDesc(
				any(Acquisition.class));

		GeneratePdf pdf = generatePdfUseCase.findByAcquisition(acquisition);
		assertNotNull(pdf);
	}
}
