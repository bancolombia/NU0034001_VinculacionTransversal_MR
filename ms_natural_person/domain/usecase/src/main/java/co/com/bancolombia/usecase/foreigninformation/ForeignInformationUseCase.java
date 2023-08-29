package co.com.bancolombia.usecase.foreigninformation;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformation.ForeignInformationOperation;

import java.util.List;

public interface ForeignInformationUseCase {
    ForeignInformation findByAcquisition(Acquisition acquisition);

    ForeignInformationOperation startProcessForeignInformation(ForeignInformationOperation operation);

    void mergeForeignInformation(ForeignInformation oldFI, ForeignInformation newFI);

    void mandatoryForeignInformation(ForeignInformation info, List<ExecFieldReply> mandatoryExecFList);
}