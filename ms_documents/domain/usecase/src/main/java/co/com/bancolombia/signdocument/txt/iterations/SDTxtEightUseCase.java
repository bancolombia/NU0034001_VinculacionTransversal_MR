package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxCountryInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.CY_SOURCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.C_SOURCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.DEC_INCOME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.D_TAX_A_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.ID_TAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.O_ASSET_COME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.R_VAT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SEPARATOR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SOCIAL_SEC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.W_AGENT;

@RequiredArgsConstructor
public class SDTxtEightUseCase {

    private final SDTxtUtilUseCase gUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, int iteration, TaxInfoReply taxInfo)
            throws IOException {
        List<TaxCountryInfoReply> taxCountry = taxInfo.getTaxCountryInfoList();
        requestTxt = requestTxt.toBuilder().dateRequest(taxInfo.getCreatedDate().toString()).build();
        gUseCase.addIteration(bw, requestTxt, Numbers.EIGHT.getNumber(), iteration);
        gUseCase.addLine(bw, DEC_INCOME, taxInfo.getDeclaringIncome());
        gUseCase.addLine(bw, W_AGENT, taxInfo.getWithHoldingAgent());
        gUseCase.addLine(bw, R_VAT, taxInfo.getVatRegime());
        gUseCase.addLine(bw, O_ASSET_COME, taxInfo.getOriginAssetComeFrom());
        gUseCase.addLine(bw, C_SOURCE, taxInfo.getSourceCountryResource());
        gUseCase.addLine(bw, CY_SOURCE, taxInfo.getSourceCityResource());
        gUseCase.addLine(bw, SOCIAL_SEC, taxInfo.getSocialSecurityPayment());
        gUseCase.addLine(bw, D_TAX_A_COUNTRY, taxInfo.getDeclareTaxInAnotherCountry());
        for (TaxCountryInfoReply item : taxCountry) {
            gUseCase.addLine(bw, ID_TAX, item.getCountry().concat("-").concat(item.getTaxId()));
            if (taxCountry.size() > 1) {
                gUseCase.addLine(bw, SEPARATOR, EMPTY);
            }
        }
        gUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
