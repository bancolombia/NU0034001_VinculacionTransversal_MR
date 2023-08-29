package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignCurrencyInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.AVERAGE_AMOUNT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.C_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.F_CURRENCY_T;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.F_CURRENCY_TT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.PRODUCT_N;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SEPARATOR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_CITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_DEPARTMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_PRODUCT_TYPE;

@RequiredArgsConstructor
public class SDTxtNineUseCase {

    private final SDTxtUtilUseCase gUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, int iteration,
                                ForeignInfoReply foreignInfo) throws IOException {
        List<ForeignCurrencyInfoReply> foreignCurrencyInfo = foreignInfo.getForeignCurrencyInfo();
        requestTxt = requestTxt.toBuilder().dateRequest(foreignInfo.getCreatedDate().toString()).build();
        gUseCase.addIteration(bw, requestTxt, Numbers.NINE.getNumber(), iteration);
        for (ForeignCurrencyInfoReply item : foreignCurrencyInfo) {
            gUseCase.addLine(bw, F_CURRENCY_T, foreignInfo.getForeignCurrencyTransaction());
            gUseCase.addLine(bw, F_CURRENCY_TT, item.getForeignCurrencyTransactionType());
            gUseCase.addLine(bw, C_NAME, item.getNameEntity());
            gUseCase.addLine(bw, S_PRODUCT_TYPE, item.getProductType());
            gUseCase.addLine(bw, PRODUCT_N, item.getProductNumber());
            gUseCase.addLine(bw, AVERAGE_AMOUNT, item.getAverageMonthlyAmount());
            gUseCase.addLine(bw, S_CURRENCY, item.getCurrency());
            gUseCase.addLine(bw, S_COUNTRY, item.getCountry());
            gUseCase.addLine(bw, S_DEPARTMENT, item.getDepartment());
            gUseCase.addLine(bw, S_CITY, item.getCity());
            if (foreignCurrencyInfo.size() > 1) {
                gUseCase.addLine(bw, SEPARATOR, EMPTY);
            }
        }
        gUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
