package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.ACT_ECONOMIC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.CIUU;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.C_D_ANNUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.D_O_INCOME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.M_INCOME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.OT_INCOME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.P_TRADE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_ANNUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_OCCUPATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_PROFESSION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.TE_MONTHLY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.T_ASSETS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.T_LIABILITIES;

@RequiredArgsConstructor
public class SDTxtSevenUseCase {

    private final SDTxtUtilUseCase gUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, int iteration,
                                EconomicInfoReply economicInfo) throws IOException {
        requestTxt = requestTxt.toBuilder().dateRequest(economicInfo.getCreatedDate().toString()).build();
        gUseCase.addIteration(bw, requestTxt, Numbers.SEVEN.getNumber(), iteration);
        gUseCase.addLine(bw, S_PROFESSION, economicInfo.getProfession());
        gUseCase.addLine(bw, P_TRADE, economicInfo.getPositionTrade());
        gUseCase.addLine(bw, S_OCCUPATION, economicInfo.getOccupation());
        gUseCase.addLine(bw, ACT_ECONOMIC, economicInfo.getEconomicActivity());
        gUseCase.addLine(bw, CIUU, economicInfo.getCiiu());
        gUseCase.addLine(bw, M_INCOME, economicInfo.getMonthlyIncome());
        gUseCase.addLine(bw, OT_INCOME, economicInfo.getOtherMonthlyIncome());
        gUseCase.addLine(bw, T_ASSETS, economicInfo.getTotalAssets());
        gUseCase.addLine(bw, T_LIABILITIES, economicInfo.getTotalLiabilities());
        gUseCase.addLine(bw, S_CURRENCY, economicInfo.getCurrency());
        gUseCase.addLine(bw, D_O_INCOME, economicInfo.getDetailOtherMonthlyIncome());
        gUseCase.addLine(bw, TE_MONTHLY, economicInfo.getTotalMonthlyExpenses());
        gUseCase.addLine(bw, S_ANNUAL, economicInfo.getAnnualSales());
        gUseCase.addLine(bw, C_D_ANNUAL, economicInfo.getClosingDateSales().toString());
        gUseCase.addLine(bw, ConstantsTwo.T_PATRIMONY, economicInfo.getPatrimony());
        gUseCase.addLine(bw, ConstantsTwo.EMP_NUMBER, economicInfo.getEmployeesNumber());
        gUseCase.addLine(bw, ConstantsTwo.S_RUT, economicInfo.getRut());
        gUseCase.addLine(bw, ConstantsTwo.REST_OK, Constants.EMPTY);
        bw.newLine();
    }
}
