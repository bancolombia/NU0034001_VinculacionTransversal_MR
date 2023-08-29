package co.com.bancolombia.foreigninformation;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequest;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformation.ForeignInformationOperation;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@NaturalPersonController
public class CreateModelForeignOperation {

    @Autowired
    private CoreFunctionString coreFunctionString;
    @Autowired
    private CoreFunctionDate coreFunctionDate;

    public ForeignInformationOperation creteModelForeignOperation(
            ForeignCurrencyInfoRequest body, Acquisition acquisition) {
        String foreignCurrencyTransactions = body.getData().getForeignCurrencyTransactions();
        ForeignInformation fInfo = ForeignInformation.builder().acquisition(acquisition)
                .createdDate(coreFunctionDate.getDatetime()).createdBy(body.getMeta().getUsrMod())
                .foreignCurrencyTransaction(foreignCurrencyTransactions).build();
        List<ForeignInformationCurrency> fInfoCList = new ArrayList<>();
        if (body.getData().getForeignCurrencyInfoRequestDataList() != null) {
            body.getData().getForeignCurrencyInfoRequestDataList().forEach(e -> fInfoCList.add(
                    ForeignInformationCurrency.builder()
                            .createdDate(coreFunctionDate.getDatetime())
                            .foreignCurrencyTransactionType(e.getForeignCurrencyTransactionType())
                            .productType(e.getProductType()).foreignInformation(fInfo).which(e.getWhich())
                            .department(e.getDepartment()).country(e.getCountry()).nameEntity(e.getNameEntity())
                            .productNumber(e.getProductNumber()).currency(e.getCurrency()).city(e.getCity())
                            .averageMonthlyAmount(coreFunctionString.stringToDecimal(e.getAverageMonthlyAmount()))
                            .acquisition(acquisition).createdBy(body.getMeta().getUsrMod()).build()));
        }
        return ForeignInformationOperation.builder().list(fInfoCList).foreignInformation(fInfo).build();
    }
}