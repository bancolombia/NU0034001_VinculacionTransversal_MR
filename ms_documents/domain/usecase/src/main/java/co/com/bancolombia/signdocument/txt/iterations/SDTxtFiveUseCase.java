package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENDER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.C_BIRTH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.DEPENDENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.D_BIRTH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.EDU_LEVEL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.HOUSE_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.INFO_ITER_FIVE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.PEP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.P_BIRTH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.STRATUM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_CIVIL_STATUS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_CONTRACT_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_NATIONALITY;


@RequiredArgsConstructor
public class SDTxtFiveUseCase {

    private final SDTxtUtilUseCase gUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, int iteration, BasicInfoReply basicInfo)
            throws IOException {
        requestTxt = requestTxt.toBuilder().dateRequest(basicInfo.getCreatedDate().toString()).build();
        gUseCase.addIteration(bw, requestTxt, Numbers.FIVE.getNumber(), iteration);
        gUseCase.addLine(bw, P_BIRTH, basicInfo.getCountry());
        gUseCase.addLine(bw, D_BIRTH, basicInfo.getBirthDepartment());
        gUseCase.addLine(bw, C_BIRTH, basicInfo.getBirthCity());
        gUseCase.addLine(bw, S_CIVIL_STATUS, basicInfo.getCivilStatus());
        gUseCase.addLine(bw, S_NATIONALITY, basicInfo.getNationality());
        gUseCase.addLine(bw, DEPENDENTS, basicInfo.getDependants());
        gUseCase.addLine(bw, EDU_LEVEL, basicInfo.getEducationLevel());
        gUseCase.addLine(bw, STRATUM, basicInfo.getSocialStratum());
        gUseCase.addLine(bw, HOUSE_TYPE, basicInfo.getHousingType());
        gUseCase.addLine(bw, S_CONTRACT_TYPE, basicInfo.getContractType());
        gUseCase.addLine(bw, PEP, basicInfo.getPep());
        gUseCase.addLine(bw, INFO_ITER_FIVE, EMPTY);
        gUseCase.addLine(bw, GENDER.concat(":"), basicInfo.getGender());
        gUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
