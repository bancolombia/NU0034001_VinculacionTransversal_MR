package co.com.bancolombia.signdocument.txt;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.ParameterReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.IdentityValResultReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.rabbit.ReputationIdentityUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtClauseUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtFiveUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtFourUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtSixUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtThreeUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTwoUseCase;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.CLAUSE1;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.TRACE_FILE;

@RequiredArgsConstructor
public class SDTxtInitialUseCaseImpl implements SDTxtInitialUseCase {

    private final SDTxtFinalUseCase sdTxtFinalUseCase;
    private final SDTxtClauseUseCase sdTxtClauseUseCase;
    private final SDTxtTwoUseCase sdTxtTwoUseCase;
    private final SDTxtThreeUseCase sdTxtThreeUseCase;
    private final SDTxtFourUseCase sdTxtFourUseCase;
    private final SDTxtFiveUseCase sdTxtFiveUseCase;
    private final SDTxtSixUseCase sdTxtSixUseCase;
    private final ReputationIdentityUseCase reputationIdentityUseCase;

    @Override
    public InputStream createTxt(Acquisition acquisition, SDRequestTxt requestTxt, SignDocumentReply sdReply)
            throws IOException {
        File file = new File(TRACE_FILE);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        requestTxt = requestTxt.toBuilder().documentType(acquisition.getDocumentType().getCode())
                .documentNumber(acquisition.getDocumentNumber()).acquisitionId(acquisition.getId().toString()).build();
        sdTxtClauseUseCase.createIteration(bw, requestTxt, CLAUSE1, Numbers.ONE.getIntNumber());
        sdTxtTwoUseCase.createIteration(bw, requestTxt, sdReply.getPersonalInfo());
        sdTxtThreeUseCase.createIteration(bw, requestTxt, sdReply.getPersonalInfo());
        ParameterReply parameter = reputationIdentityUseCase.getParameterByNameAndParent(THRESHOLD_MAX,
                PARENT_VALIDATE_IDENTITY);
        int iteration = Numbers.FOUR.getIntNumber();
        IdentityValResultReply ivResult = reputationIdentityUseCase.getIdentityValResultReply(acquisition.getId());
        if (ivResult.isValid() && (new BigDecimal(ivResult.getValidateIdentityScoreAccumulated())
                .compareTo(new BigDecimal(Integer.parseInt(parameter.getCode()))) < 0)) {
            sdTxtFourUseCase.createIteration(bw, requestTxt, iteration, ivResult.getQuestionnaireCreatedDate());
            iteration++;
        }
        sdTxtFiveUseCase.createIteration(bw, requestTxt, iteration, sdReply.getBasicInfo());
        iteration++;
        sdTxtSixUseCase.createIteration(bw, requestTxt, iteration, sdReply.getContactInfo());
        iteration++;
        requestTxt = requestTxt.toBuilder().iteration(iteration).build();
        sdTxtFinalUseCase.createTxt(bw, requestTxt, sdReply);
        bw.close();
        return new FileInputStream(file);
    }
}
