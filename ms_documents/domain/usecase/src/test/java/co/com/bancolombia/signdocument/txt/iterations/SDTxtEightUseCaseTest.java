package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxCountryInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
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
public class SDTxtEightUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtEightUseCase sdTxtEightUseCase;

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
        List<TaxCountryInfoReply> taxCountryInfoReplyList = new ArrayList<>();
        TaxCountryInfoReply taxCountryInfoReply = TaxCountryInfoReply.builder().country("").taxId("").build();
        taxCountryInfoReplyList.add(taxCountryInfoReply);
        TaxInfoReply taxInfoReply = TaxInfoReply.builder().createdDate(new Date()).declaringIncome("a")
                .withHoldingAgent("a").vatRegime("a").sourceCountryResource("a").sourceCityResource("a")
                .socialSecurityPayment("a").declareTaxInAnotherCountry("a").taxCountryInfoList(taxCountryInfoReplyList)
                .build();
        doNothing().when(genericUseCase).addIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyString(), anyInt());
        doNothing().when(genericUseCase).addLine(any(BufferedWriter.class), anyString(), anyString());
        sdTxtEightUseCase.createIteration(bw, SDRequestTxt.builder().build(), 1, taxInfoReply);
        verify(sdTxtEightUseCase, times(1)).createIteration(bw, SDRequestTxt.builder().build(), 1, taxInfoReply);
    }
}