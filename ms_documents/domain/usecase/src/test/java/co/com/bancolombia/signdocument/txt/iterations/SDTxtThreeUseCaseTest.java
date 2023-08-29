package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
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
public class SDTxtThreeUseCaseTest {

    @InjectMocks
    @Spy
    SDTxtThreeUseCase sdTxtThreeUseCase;
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
        PersonalInfoReply personalInfoReply = PersonalInfoReply.builder().firstName("a").secondName("a")
                .firstSurname("a").secondSurname("a").cellphone("a").email("a").expeditionCountry("a")
                .expeditionDepartment("a").expeditionPlace("a").createdDate(now).expeditionDate(now).birthdate(now)
                .build();
        doNothing().when(genericUseCase).addIteration(any(BufferedWriter.class), any(SDRequestTxt.class),
                anyString(), anyInt());
        doNothing().when(genericUseCase).addLine(any(BufferedWriter.class), anyString(), anyString());
        sdTxtThreeUseCase.createIteration(bw, SDRequestTxt.builder().build(), personalInfoReply);
        verify(sdTxtThreeUseCase, times(1)).createIteration(bw, SDRequestTxt.builder().build(), personalInfoReply);
    }
}
