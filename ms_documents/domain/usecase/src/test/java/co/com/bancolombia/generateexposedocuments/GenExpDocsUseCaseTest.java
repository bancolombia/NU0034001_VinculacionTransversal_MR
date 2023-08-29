package co.com.bancolombia.generateexposedocuments;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.generatepdf.GeneratePdfUseCase;
import co.com.bancolombia.model.exposedocuments.ExposeDocs;
import co.com.bancolombia.model.generatepdf.GeneratePdf;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENEPDF_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENPDF_EXITO;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GenExpDocsUseCaseTest {

    @InjectMocks
    @Spy
    private GenExpDocsUseCaseImpl genExpDocsUseCase;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GeneratePdfUseCase generatePdfUseCase;

    @Mock
    private Exceptions exceptions;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    private Acquisition acquisition;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        TypeAcquisition typeAcquisition = TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build();
        DocumentType documentType = DocumentType.builder().codeHomologation("asd").build();

        acquisition = Acquisition.builder().id(UUID.randomUUID())
                .createdBy("asd").typeAcquisition(typeAcquisition)
                .documentType(documentType).documentNumber("number").build();
    }

    @Test
    public void getPdfTest() {
        GeneratePdf generatePdf = GeneratePdf.builder()
                .dateRequest(new Date()).dateResponse(new Date()).codeResult(GENPDF_EXITO)
                .urlFileClient("https://path.to.my.file").urlFileCustody("https://path.to.my.file").build();

        doReturn(generatePdf).when(generatePdfUseCase).generatePdf(any(Acquisition.class), anyString());

        GeneratePdf genPdf = genExpDocsUseCase.getPdf(acquisition);
        assertNotNull(genPdf);
    }

    @Test
    public void urlErrorTest() {
        doNothing().when(exceptions).createException(any(), anyString(), anyString(), anyString());

        GeneratePdf generatePdf = GeneratePdf.builder().codeResult(GENEPDF_ERROR).build();
        ExposeDocs exposeDocs = genExpDocsUseCase.urlFinal(acquisition, generatePdf);
        assertNotNull(exposeDocs);
    }

    @Test
    public void urlSuccessTest() {
        doReturn(new Date()).when(coreFunctionDate).expirationTimeMillis(anyInt());
        doReturn("https://path.to.my.file").when(dataFileRepository).generateUrl(anyString(), any(Date.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        GeneratePdf generatePdf = GeneratePdf.builder().codeResult(GENPDF_EXITO).build();
        ExposeDocs exposeDocs = genExpDocsUseCase.urlFinal(acquisition, generatePdf);
        assertNotNull(exposeDocs);
    }
}
