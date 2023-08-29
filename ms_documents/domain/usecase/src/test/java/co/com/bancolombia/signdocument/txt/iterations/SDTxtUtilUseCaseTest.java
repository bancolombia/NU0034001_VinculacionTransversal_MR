package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.signdocument.TxtConstruction;
import co.com.bancolombia.model.signdocument.gateways.TxtConstructionRepository;
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
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SDTxtUtilUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtUtilUseCase sdTxtUtilUseCase;

    @Mock
    TxtConstructionRepository txtConstructionRepository;

    BufferedWriter bw = null;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        File file = new File("test.txt");
        bw = new BufferedWriter(new FileWriter(file));
    }

    @Test
    public void findByAllIteration() {
        List<TxtConstruction> list = new ArrayList<>();
        doReturn(list).when(txtConstructionRepository).findByAllIteration(anyString());
        List<TxtConstruction> txtConstructions = sdTxtUtilUseCase.findByAllIteration("1");
        assertNotNull(txtConstructions);
    }

    @Test
    public void addIteration() throws IOException {
        SDRequestTxt requestTxt = SDRequestTxt.builder().messageId("a").acquisitionId("a").documentNumber("a")
                .documentType("a").ip("a").dateRequest("a").build();
        TxtConstruction construction = TxtConstruction.builder().iterationOrder("2").value("asd").build();
        List<TxtConstruction> list = new ArrayList<>();
        list.add(construction);
        doReturn(list).when(sdTxtUtilUseCase).findByAllIteration(anyString());
        sdTxtUtilUseCase.addIteration(bw, requestTxt, "1", 1);
        verify(sdTxtUtilUseCase, times(1)).addIteration(bw, requestTxt, "1", 1);
    }

    @Test
    public void addIteration2() throws IOException {
        SDRequestTxt requestTxt = SDRequestTxt.builder().messageId("a").acquisitionId("a").documentNumber("a")
                .documentType("a").ip("a").dateRequest("a").build();
        TxtConstruction construction = TxtConstruction.builder().iterationOrder("1").value("asd").build();
        List<TxtConstruction> list = new ArrayList<>();
        list.add(construction);
        doReturn(list).when(sdTxtUtilUseCase).findByAllIteration(anyString());
        sdTxtUtilUseCase.addIteration(bw, requestTxt, "1", 1);
        verify(sdTxtUtilUseCase, times(1)).addIteration(bw, requestTxt, "1", 1);    }

    @Test
    public void addLine() throws IOException {
        sdTxtUtilUseCase.addLine(bw, "asd", "");
        verify(sdTxtUtilUseCase, times(1)).addLine(bw, "asd", "");
    }

    @Test
    public void addLine1() throws IOException {
        sdTxtUtilUseCase.addLine(bw, "asd", "a");
        verify(sdTxtUtilUseCase, times(1)).addLine(bw, "asd", "a");
    }
}
