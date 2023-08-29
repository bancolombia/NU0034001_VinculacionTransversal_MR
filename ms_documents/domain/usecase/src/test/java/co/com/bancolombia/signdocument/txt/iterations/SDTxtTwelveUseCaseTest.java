package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateTokenReply;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
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
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SDTxtTwelveUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtTwelveUseCase sdTxtTwelveUseCase;
    @Mock
    SDTxtUtilUseCase genericUseCase;

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
    public void test2() throws IOException {
        ValidateTokenReply validateTokenReply = ValidateTokenReply.builder().createdDate(now).build();
        doNothing().when(genericUseCase).addIteration(any(BufferedWriter.class), any(SDRequestTxt.class), anyString(),
                anyInt());
        doNothing().when(genericUseCase).addLine(any(BufferedWriter.class), anyString(), anyString());
        sdTxtTwelveUseCase.createIteration(bw, SDRequestTxt.builder().build(), 1, validateTokenReply);
        verify(sdTxtTwelveUseCase, times(1)).createIteration(bw, SDRequestTxt.builder().build(), 1, validateTokenReply);
    }
}