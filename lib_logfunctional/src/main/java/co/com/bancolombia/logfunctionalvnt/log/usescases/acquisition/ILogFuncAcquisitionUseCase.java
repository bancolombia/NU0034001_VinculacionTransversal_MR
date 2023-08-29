package co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.logfunctionalvnt.log.jpa.LogFunctionalDataRepositoryAdapter;

import java.util.UUID;

public interface ILogFuncAcquisitionUseCase {
    public Acquisition findByIdWitOutState(UUID idAcquisition);
    public Acquisition findByIdAndDocumentTypeAndDocumentNumber(UUID id, String documentType, String documentNumber);
    public Acquisition findById(UUID idAcquisition);
    public AcquisitionStateReply acquisitionStateReply(AcquisitionStateQuery acquisitionStateQuery);

}
