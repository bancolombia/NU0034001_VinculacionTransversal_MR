package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.signdocument.SDRequest;
import co.com.bancolombia.model.signdocument.SDRequestTotal;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.signdocument.SDResponseDatum;
import co.com.bancolombia.model.signdocument.SDResponseError;
import co.com.bancolombia.model.signdocument.SDResponseOk;
import co.com.bancolombia.model.signdocument.SDResponseTotal;
import co.com.bancolombia.model.signdocument.SDResponseTotalWithLog;
import co.com.bancolombia.model.signdocument.SignDocument;
import co.com.bancolombia.model.signdocument.gateways.SignDocumentRepository;
import co.com.bancolombia.model.signdocument.gateways.SignDocumentRestRepository;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SignDocumentTest {

    @InjectMocks
    @Spy
    SignDocumentUseCaseImpl signDocumentUseCase;

    @Mock
    SignDocumentRequestUseCase createRequest;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    SignDocumentRepository repository;

    @Mock
    DataFileRepository dataFileRepository;

    @Mock
    SignDocumentRestRepository signDocumentRestRepository;

    @Mock
    SignDocumentValidateUseCase signValidateUseCase;

    @Mock
    CoreFunctionDate coreFunctionDate;

    private Acquisition acquisition;
    private Date date;

    @Before
    public void setUp() {
        date = new Date();
        MockitoAnnotations.initMocks(this);
        acquisition = Acquisition.builder().id(UUID.randomUUID()).documentType(DocumentType.builder()
                .codeGenericType("asd").build()).documentNumber("asd").build();
    }

    @Test
    public void save() {
        SignDocument signDocument = SignDocument.builder().build();
        doReturn(signDocument).when(repository).save(any(SignDocument.class));
        SignDocument signDocument1 = signDocumentUseCase.save(signDocument);
        assertNotNull(signDocument1);
    }

    @Test
    public void startProcess() throws IOException, MessagingException {
        SDRequest request = SDRequest.builder().build();
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doReturn(request).when(createRequest).createRequest(acquisition, SDRequestTxt.builder().build());
        SignDocument signDocument = SignDocument.builder().build();
        doReturn(signDocument).when(signDocumentUseCase).buildSignature(any(Acquisition.class),
                any(SDResponseTotal.class), anyString());
        doReturn(signDocument).when(signDocumentUseCase).save(any(SignDocument.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        SDResponseTotalWithLog esResponseTotalWithLog = SDResponseTotalWithLog.builder()
                .responseTotal(SDResponseTotal.builder().build()).infoReuseCommon(InfoReuseCommon.builder().build())
                .build();
        doReturn(esResponseTotalWithLog).when(signDocumentRestRepository).getSignature(any(SDRequestTotal.class));
        signDocument = signDocumentUseCase.startProcess(acquisition, SDRequestTxt.builder().usrMod("asd").build());
        assertNotNull(signDocument);
    }

    @Test
    public void buildSignature() {
        SDResponseTotal responseTotal = SDResponseTotal.builder().responseOk(SDResponseOk.builder()
                .data(SDResponseDatum.builder().file("asd").build()).build()).build();
        SignDocument signDocument = signDocumentUseCase.buildSignature(acquisition, responseTotal, "asd");
        assertNotNull(signDocument);
    }

    @Test
    public void buildSignatureNull() {
        SDResponseTotal responseTotal = SDResponseTotal.builder().build();
        doNothing().when(signValidateUseCase).actionsErrors(any(Acquisition.class), any(SDResponseError.class));
        SignDocument signDocument = signDocumentUseCase.buildSignature(acquisition, responseTotal, "asd");
        assertNull(signDocument);
    }
}
