package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SDTxtFiveUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtFiveUseCase sdTxtFiveUseCase;
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
        BasicInfoReply basicInfoReply = BasicInfoReply.builder().createdDate(now).country("a").birthDepartment("a")
                .birthCity("a").civilStatus("a").nationality("a").dependants("1").educationLevel("a")
                .socialStratum("a").housingType("a").contractType("a").pep("a").gender("a").build();
        doNothing().when(genericUseCase).addIteration(any(BufferedWriter.class), any(SDRequestTxt.class), anyString(),
                anyInt());
        doNothing().when(genericUseCase).addLine(any(BufferedWriter.class), anyString(), anyString());
        sdTxtFiveUseCase.createIteration(bw, SDRequestTxt.builder().build(), 1, basicInfoReply);
        verify(sdTxtFiveUseCase, times(1)).createIteration(bw, SDRequestTxt.builder().build(), 1, basicInfoReply);
    }
}
