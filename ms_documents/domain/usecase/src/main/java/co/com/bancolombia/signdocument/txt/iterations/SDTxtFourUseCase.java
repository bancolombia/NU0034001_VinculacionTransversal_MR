package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;


@RequiredArgsConstructor
public class SDTxtFourUseCase {

    private final SDTxtUtilUseCase gUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, int iteration, Date questionnaireCD)
            throws IOException {
        requestTxt = requestTxt.toBuilder().dateRequest(questionnaireCD.toString()).build();
        gUseCase.addIteration(bw, requestTxt, Numbers.FOUR.getNumber(), iteration);
        gUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
