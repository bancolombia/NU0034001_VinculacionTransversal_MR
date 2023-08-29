package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.GeneratePdfResponseData;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENEPDF_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENPDF_EXITO;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GeneratePdfProcessUseCaseTest {

    @InjectMocks
    @Spy
    private GeneratePdfProcessUseCaseImpl generatePdfProcessUseCase;

    @Mock
    private GeneratePdfDocumentUseCase generatePdfDocumentUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void generatePdfSuccessTest() {
        doReturn(new HashMap<>()).when(generatePdfDocumentUseCase).generatePdf(any(AcquisitionPdf.class));

        AcquisitionPdf acquisitionPdf = AcquisitionPdf.builder().acquisitionId(UUID.randomUUID()).build();
        GeneratePdfResponseData gp = generatePdfProcessUseCase.generatePdf(acquisitionPdf);
        assertEquals(GENPDF_EXITO, gp.getStatusGeneracionPdf());
    }

    @Test
    public void generatePdfErrorTest() {
        doReturn(null).when(generatePdfDocumentUseCase).generatePdf(any(AcquisitionPdf.class));

        AcquisitionPdf acquisitionPdf = AcquisitionPdf.builder().acquisitionId(UUID.randomUUID()).build();
        GeneratePdfResponseData gp = generatePdfProcessUseCase.generatePdf(acquisitionPdf);
        assertEquals(GENEPDF_ERROR, gp.getStatusGeneracionPdf());
    }
}
