package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
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
import java.math.BigDecimal;
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
public class SDTxtSevenUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtSevenUseCase sdTxtSevenUseCase;
    @Mock
    SDTxtUtilUseCase genericUseCase;

    private Date now;

    BufferedWriter bw = null;
    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        now = new Date();
        File file = new File("test.txt");
        bw = new BufferedWriter(new FileWriter(file));
    }

    @Test
    public void createIterationTest() throws IOException {
        EconomicInfoReply economicInfoReply = EconomicInfoReply.builder().createdDate(now)
                .profession("a").positionTrade("a").occupation("a").economicActivity("a")
                .ciiu("a").monthlyIncome("bigDecimal").otherMonthlyIncome("bigDecimal").totalAssets("bigDecimal")
                .totalLiabilities("bigDecimal").currency("a").detailOtherMonthlyIncome("a")
                .totalMonthlyExpenses("bigDecimal").annualSales("bigDecimal").closingDateSales(now)
                .patrimony("bigDecimal").employeesNumber("1").rut("a").build();
        doNothing().when(genericUseCase).addIteration(any(BufferedWriter.class), any(SDRequestTxt.class), anyString(),
                anyInt());
        doNothing().when(genericUseCase).addLine(any(BufferedWriter.class), anyString(), anyString());
        sdTxtSevenUseCase.createIteration(bw, SDRequestTxt.builder().build(), 1, economicInfoReply);
        verify(sdTxtSevenUseCase, times(1)).createIteration(bw, SDRequestTxt.builder().build(), 1, economicInfoReply);
    }
}