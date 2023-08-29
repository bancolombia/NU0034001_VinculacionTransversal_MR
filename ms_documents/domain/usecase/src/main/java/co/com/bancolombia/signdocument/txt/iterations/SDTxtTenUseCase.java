package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.INFO_ITER_TEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;

@RequiredArgsConstructor
public class SDTxtTenUseCase {

    private final SDTxtUtilUseCase genericUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, int iteration,
                                EconomicInfoReply economicInfo) throws IOException {
        requestTxt = requestTxt.toBuilder().dateRequest(economicInfo.getCreatedDate().toString()).build();
        genericUseCase.addIteration(bw, requestTxt, Numbers.TEN.getNumber(), iteration);
        genericUseCase.addLine(bw, INFO_ITER_TEN, EMPTY);
        genericUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
