package co.com.bancolombia.signdocument.txt;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.IdentityValResultReply;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.rabbit.ReputationIdentityUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtClauseUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtFiveUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtFourUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtSixUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtThreeUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTwoUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SDTxtInitialUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtInitialUseCaseImpl sdTxtInitialUseCase;

    @Mock
    SDTxtFinalUseCase sdTxtFinalUseCase;
    @Mock
    SDTxtClauseUseCase sdTxtClauseUseCase;
    @Mock
    SDTxtTwoUseCase sdTxtTwoUseCase;
    @Mock
    SDTxtThreeUseCase sdTxtThreeUseCase;
    @Mock
    SDTxtFourUseCase sdTxtFourUseCase;
    @Mock
    SDTxtFiveUseCase sdTxtFiveUseCase;
    @Mock
    SDTxtSixUseCase sdTxtSixUseCase;
    @Mock
    ParametersUseCase parametersUseCase;
    @Mock
    ReputationIdentityUseCase reputationIdentityUseCase;

    private Date now;

    File file = new File("test.txt");
    BufferedWriter bw = null;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        now = new Date();
        bw = new BufferedWriter(new FileWriter(file));
    }

    @Test
    public void createTxt() throws IOException {
        SDRequestTxt requestTxt = SDRequestTxt.builder().documentType("a").documentNumber("a").acquisitionId("a")
                .build();
        SignDocumentReply sdReply = SignDocumentReply.builder().build();
        doNothing().when(sdTxtClauseUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyString(), anyInt());
        doNothing().when(sdTxtTwoUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                any(PersonalInfoReply.class));
        doNothing().when(sdTxtThreeUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                any(PersonalInfoReply.class));
        Parameters parameter = Parameters.builder().code("80").build();
        doReturn(Optional.of(parameter)).when(parametersUseCase).findByNameAndParent(anyString(), anyString());
        IdentityValResultReply identityValResultReply = IdentityValResultReply.builder()
                .validateIdentityScoreAccumulated("48.2").questionnaireCreatedDate(new Date()).build();
        doReturn(identityValResultReply).when(reputationIdentityUseCase).getIdentityValResultReply(any(UUID.class));
        doNothing().when(sdTxtFourUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyInt(), any(Date.class));
        doNothing().when(sdTxtFiveUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyInt(), any(BasicInfoReply.class));
        doNothing().when(sdTxtSixUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyInt(), any(ContactInfoReply.class));
        doNothing().when(sdTxtFinalUseCase).createTxt(any(BufferedWriter.class), any(SDRequestTxt.class),
                any(SignDocumentReply.class));
        Acquisition acquisition = Acquisition.builder().documentNumber("a").documentType(DocumentType.builder()
                .code("a").build()).id(UUID.randomUUID()).build();
        InputStream inputStream = sdTxtInitialUseCase.createTxt(acquisition, requestTxt, sdReply);
        assertNotNull(inputStream);
    }
}