package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateTokenReply;
import co.com.bancolombia.generatepdf.GeneratePdfUseCase;
import co.com.bancolombia.model.generatepdf.GeneratePdf;
import co.com.bancolombia.model.signdocument.SDRequest;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.signdocument.txt.SDTxtAttachUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SignDocumentRequestTest {

    @InjectMocks
    @Spy
    SignDocumentRequestUseCaseImpl createRequest;

    @Mock
    SDTxtAttachUseCase sdTxtAttachUseCase;

    @Mock
    DataFileRepository dataFileRepository;

    @Mock
    GeneratePdfUseCase generatePdfUseCase;

    @Mock
    NaturalPersonUseCase naturalPersonUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createRequestTest() throws IOException {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).documentType(DocumentType.builder()
                .codeGenericType("asd").build()).documentNumber("asd").build();
        InputStream inputStream = new ByteArrayInputStream("asd".getBytes());
        doReturn(inputStream).when(dataFileRepository).retrieveFileInputStream(anyString());
        GeneratePdf generatePdf = GeneratePdf.builder().urlFileCustody("asd").build();
        doReturn(generatePdf).when(generatePdfUseCase).findByAcquisition(any(Acquisition.class));
        SignDocumentReply signDocumentReply = SignDocumentReply.builder().validateToken(ValidateTokenReply.builder()
                .build()).build();
        doReturn(signDocumentReply).when(naturalPersonUseCase).getSignDocumentReply(any(UUID.class));
        doReturn("asd").when(sdTxtAttachUseCase).attachFileToPdf(any(Acquisition.class),
                any(SDRequestTxt.class), any(InputStream.class), any(SignDocumentReply.class));
        SDRequest request = createRequest.createRequest(acquisition, SDRequestTxt.builder().build());
        assertNotNull(request);
    }
}
