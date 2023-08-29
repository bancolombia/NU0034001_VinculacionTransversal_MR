package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateTokenReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;

@RequiredArgsConstructor
public class SDTxtTwelveUseCase {

    private final SDTxtUtilUseCase genericUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, int iteration,
                                ValidateTokenReply validateTokenReply) throws IOException {
        requestTxt = requestTxt.toBuilder().dateRequest(validateTokenReply.getCreatedDate().toString()).build();
        genericUseCase.addIteration(bw, requestTxt, Numbers.TWELVE.getNumber(), iteration);
        genericUseCase.addLine(bw, REST_OK, EMPTY);
    }
}
