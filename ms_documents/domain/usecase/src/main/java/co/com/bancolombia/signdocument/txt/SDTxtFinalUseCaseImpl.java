package co.com.bancolombia.signdocument.txt;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtClauseUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtEightUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtNineUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtSevenUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTenUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTwelveUseCase;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.CLAUSE2;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.OCCUPATION_RUT;

@RequiredArgsConstructor
public class SDTxtFinalUseCaseImpl implements SDTxtFinalUseCase {

    private final SDTxtClauseUseCase sdTxtClauseUseCase;
    private final SDTxtSevenUseCase sdTxtSevenUseCase;
    private final SDTxtEightUseCase sdTxtEightUseCase;
    private final SDTxtNineUseCase sdTxtNineUseCase;
    private final SDTxtTenUseCase sdTxtTenUseCase;
    private final SDTxtTwelveUseCase sdTxtTwelveUseCase;

    @Override
    public void createTxt(BufferedWriter bw, SDRequestTxt requestTxt, SignDocumentReply sdReply) throws IOException {
        EconomicInfoReply economicInfo = sdReply.getEconomicInfo();
        int iteration = requestTxt.getIteration();
        sdTxtSevenUseCase.createIteration(bw, requestTxt, iteration, economicInfo);
        iteration++;
        sdTxtEightUseCase.createIteration(bw, requestTxt, iteration, sdReply.getTaxInfo());
        iteration++;
        sdTxtNineUseCase.createIteration(bw, requestTxt, iteration, sdReply.getForeignInfo());
        iteration++;
        if (Arrays.asList(OCCUPATION_RUT).contains(economicInfo.getOccupation())) {
            sdTxtTenUseCase.createIteration(bw, requestTxt, iteration, economicInfo);
            iteration++;
        }
        sdTxtClauseUseCase.createIteration(bw, requestTxt, CLAUSE2, iteration);
        iteration++;
        sdTxtTwelveUseCase.createIteration(bw, requestTxt, iteration, sdReply.getValidateToken());
        bw.close();
    }
}
