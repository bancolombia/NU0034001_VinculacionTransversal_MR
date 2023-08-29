package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignCurrencyInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SDTxtNineUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtNineUseCase sdTxtNineUseCase;

    @Mock
    SDTxtUtilUseCase genericUseCase;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createIterationTest() throws IOException {
        File file = new File("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        SDRequestTxt sdRequestTxt = SDRequestTxt.builder().dateRequest("").build();
        ForeignCurrencyInfoReply foreignCurrencyInfoReply = ForeignCurrencyInfoReply.builder()
                .foreignCurrencyTransactionType("a").nameEntity("a").productType("a").productNumber("a")
                .averageMonthlyAmount("2").currency("a").country("a").department("a").city("a").build();
        List<ForeignCurrencyInfoReply> list = new ArrayList<>();
        list.add(foreignCurrencyInfoReply);
        ForeignInfoReply foreignInfoReply = ForeignInfoReply.builder().foreignCurrencyInfo(list)
                .createdDate(new Date()).build();
        doNothing().when(genericUseCase).addIteration(any(BufferedWriter.class), any(SDRequestTxt.class), anyString(),
                anyInt());
        doNothing().when(genericUseCase).addLine(any(BufferedWriter.class), anyString(), anyString());
        sdTxtNineUseCase.createIteration(bw, sdRequestTxt, 1, foreignInfoReply);
        verify(sdTxtNineUseCase, times(1)).createIteration(bw, sdRequestTxt, 1, foreignInfoReply);
    }
}