package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SDTxtFourUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtFourUseCase sdTxtFourUseCase;
    @Mock
    SDTxtUtilUseCase genericUseCase;

    private Date now;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        now = new Date();
    }

    @Test
    public void createIteration() throws IOException {
        File file = new File("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        SDRequestTxt sdRequestTxt = SDRequestTxt.builder().dateRequest("").build();
        doNothing().when(genericUseCase).addIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyString(), anyInt());
        doNothing().when(genericUseCase).addLine(any(BufferedWriter.class), anyString(), anyString());
        sdTxtFourUseCase.createIteration(bw, sdRequestTxt, 1, now);
        verify(sdTxtFourUseCase, times(1)).createIteration(bw, sdRequestTxt, 1, now);
    }
}
