package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ClauseReply;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.rabbit.VinculationUpdateTwoUseCaseImpl;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.CLAUSE1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SDTxtClauseUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtClauseUseCase sdTxtClauseUseCase;
    @Mock
    SDTxtUtilUseCase genericUseCase;
    @Mock
    VinculationUpdateTwoUseCaseImpl vinculationUpdateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createIteration() throws IOException {
        File file = new File("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        SDRequestTxt sdRequestTxt = SDRequestTxt.builder().dateRequest("").build();
        ClauseReply clauseReply = ClauseReply.builder().valid(true).name("asd").createdDate(new Date()).build();
        doReturn(clauseReply).when(vinculationUpdateUseCase).getClause(anyString());
        doNothing().when(genericUseCase).addIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyString(), anyInt());
        doNothing().when(genericUseCase).addLine(any(BufferedWriter.class), anyString(), anyString());
        sdTxtClauseUseCase.createIteration(bw, sdRequestTxt, CLAUSE1, 1);
        verify(sdTxtClauseUseCase, times(1)).createIteration(bw, sdRequestTxt, CLAUSE1,1);
    }
}
