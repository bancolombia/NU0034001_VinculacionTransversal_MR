package co.com.bancolombia.signdocument.txt;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateTokenReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtClauseUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtEightUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtNineUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtSevenUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTenUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTwelveUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SDTxtFinalUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtFinalUseCaseImpl sdTxtFinalUseCase;

    @Mock
    SDTxtClauseUseCase sdTxtClauseUseCase;
    @Mock
    SDTxtSevenUseCase sdTxtSevenUseCase;
    @Mock
    SDTxtEightUseCase sdTxtEightUseCase;
    @Mock
    SDTxtNineUseCase sdTxtNineUseCase;
    @Mock
    SDTxtTenUseCase sdTxtTenUseCase;
    @Mock
    SDTxtTwelveUseCase sdTxtTwelveUseCase;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createTxt() throws IOException {
        File file = new File("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        SDRequestTxt requestTxt = SDRequestTxt.builder().iteration(1).build();
        EconomicInfoReply economicInfoReply = EconomicInfoReply.builder().occupation("").build();
        SignDocumentReply sdReply = SignDocumentReply.builder().economicInfo(economicInfoReply).build();
        doNothing().when(sdTxtSevenUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyInt(), any(EconomicInfoReply.class));
        doNothing().when(sdTxtEightUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyInt(), any(TaxInfoReply.class));
        doNothing().when(sdTxtNineUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyInt(), any(ForeignInfoReply.class));
        doNothing().when(sdTxtTenUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyInt(), any(EconomicInfoReply.class));
        doNothing().when(sdTxtClauseUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyString(), anyInt());
        doNothing().when(sdTxtTwelveUseCase).createIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyInt(), any(ValidateTokenReply.class));
        sdTxtFinalUseCase.createTxt(bw, requestTxt, sdReply);
        verify(sdTxtFinalUseCase, times(1)).createTxt(bw, requestTxt, sdReply);
    }
}