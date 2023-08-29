package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.BIRTH_DATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.F_EXP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.F_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.P_SURNAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_GENDER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_SURNAME;

@RequiredArgsConstructor
public class SDTxtTwoUseCase {
    private final SDTxtUtilUseCase gUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, PersonalInfoReply persoInfo)
            throws IOException {
        requestTxt = requestTxt.toBuilder().dateRequest(persoInfo.getCreatedDate().toString()).build();
        gUseCase.addIteration(bw, requestTxt, Numbers.TWO.getNumber(), Numbers.TWO.getIntNumber());
        gUseCase.addLine(bw, F_NAME.concat(":"), persoInfo.getFirstName());
        gUseCase.addLine(bw, S_NAME.concat(":"), persoInfo.getSecondName());
        gUseCase.addLine(bw, P_SURNAME.concat(":"), persoInfo.getFirstSurname());
        gUseCase.addLine(bw, S_SURNAME.concat(":"), persoInfo.getSecondSurname());
        gUseCase.addLine(bw, F_EXP, persoInfo.getExpeditionDate().toString());
        gUseCase.addLine(bw, BIRTH_DATE, persoInfo.getBirthdate().toString());
        gUseCase.addLine(bw, S_GENDER, EMPTY);
        gUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
