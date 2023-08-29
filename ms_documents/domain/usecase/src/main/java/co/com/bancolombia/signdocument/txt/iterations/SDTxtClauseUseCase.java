package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ClauseReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.rabbit.VinculationUpdateTwoUseCase;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATA_ADMINISTRATION_POLICY_URL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TITLE_POLICY_URL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.CLAUSE1;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.CODE_CLAUSE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.DESC_CLAUSE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;

@RequiredArgsConstructor
public class SDTxtClauseUseCase {

    private final SDTxtUtilUseCase genericUseCase;
    private final VinculationUpdateTwoUseCase vinculationUpdateUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, String clauseCode, int iteration)
            throws IOException {
        ClauseReply clauseReply = vinculationUpdateUseCase.getClause(clauseCode);
        if (clauseReply.isValid()) {
            String iterationBD = clauseCode.equals(CLAUSE1) ? Numbers.ONE.getNumber() : Numbers.ELEVEN.getNumber();
            requestTxt = requestTxt.toBuilder().dateRequest(clauseReply.getCreatedDate().toString()).build();
            genericUseCase.addIteration(bw, requestTxt, iterationBD, iteration);
            genericUseCase.addLine(bw, CODE_CLAUSE, clauseCode);
            genericUseCase.addLine(bw, DESC_CLAUSE, clauseReply.getName());
            genericUseCase.addLine(bw, TITLE_POLICY_URL, DATA_ADMINISTRATION_POLICY_URL);
        }
        genericUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
