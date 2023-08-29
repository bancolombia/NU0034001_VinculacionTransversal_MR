package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.BIRTH_DATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.C_EXP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.D_EXP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.F_EXP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.F_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.INFO_ITER_THREE_1;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.INFO_ITER_THREE_2;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.P_EXP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.P_SURNAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_CELLPHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_EMAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_SURNAME;

@RequiredArgsConstructor
public class SDTxtThreeUseCase {

    private final SDTxtUtilUseCase gUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, PersonalInfoReply persoInfo)
            throws IOException {
        requestTxt = requestTxt.toBuilder().dateRequest(persoInfo.getCreatedDate().toString()).build();
        gUseCase.addIteration(bw, requestTxt, Numbers.THREE.getNumber(), Numbers.THREE.getIntNumber());
        gUseCase.addLine(bw, S_EMAIL, persoInfo.getEmail());
        gUseCase.addLine(bw, S_CELLPHONE, persoInfo.getCellphone());
        gUseCase.addLine(bw, P_EXP, persoInfo.getExpeditionCountry());
        gUseCase.addLine(bw, D_EXP, persoInfo.getExpeditionDepartment());
        gUseCase.addLine(bw, C_EXP, persoInfo.getExpeditionPlace());
        gUseCase.addLine(bw, INFO_ITER_THREE_1, EMPTY);
        gUseCase.addLine(bw, F_NAME.concat(":"), persoInfo.getFirstName());
        gUseCase.addLine(bw, S_NAME.concat(":"), persoInfo.getSecondName());
        gUseCase.addLine(bw, P_SURNAME.concat(":"), persoInfo.getFirstSurname());
        gUseCase.addLine(bw, S_SURNAME.concat(":"), persoInfo.getSecondSurname());
        gUseCase.addLine(bw, F_EXP, persoInfo.getExpeditionDate().toString());
        gUseCase.addLine(bw, BIRTH_DATE, persoInfo.getBirthdate().toString());
        gUseCase.addLine(bw, INFO_ITER_THREE_2, EMPTY);
        gUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
