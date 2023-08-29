package co.com.bancolombia.usecase.foreigninformationcurrency;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;

import java.util.List;

public interface ForeignInformationCurrencyUseCase {

    List<ForeignInformationCurrency> findByForeignInformation(ForeignInformation foreignInformation);

    List<ForeignInformationCurrency> mandatoryMergeForeignInfo(
        ForeignInformation fI, List<ForeignInformationCurrency> list,
        List<ExecFieldReply> mandatoryExecFList, boolean bValidExists);

    List<ForeignInformationCurrency> mergeForeignInformationCurrency(
            List<ForeignInformationCurrency> list, List<ForeignInformationCurrency> old);

    void mandatoryForeignInformationCurrency(
        List<ForeignInformationCurrency> list, List<ExecFieldReply> mandatoryExecFList, ForeignInformation fI);
}