package co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.logfunctionalvnt.log.jpa.LogFunctionalDataRepositoryAdapter;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class LogFuncAcquisitionUseCase implements ILogFuncAcquisitionUseCase {

    private LogFunctionalDataRepositoryAdapter logFunctionalDataRepositoryAdapter;

    @Override
    public AcquisitionStateReply acquisitionStateReply(AcquisitionStateQuery acquisitionStateQuery){
        return this.logFunctionalDataRepositoryAdapter.acquisitionStateReply(acquisitionStateQuery);
    }



    @Override
    public Acquisition findByIdWitOutState(UUID idAcquisition) {

        return Acquisition.builder()
                .id(idAcquisition)
                .documentNumber("123456789")
                .stateAcquisition(StateAcquisition.builder()
                        .name("ACTIVO")
                        .build())
                .build();
    }

	@Override
	public Acquisition findByIdAndDocumentTypeAndDocumentNumber(UUID id, String documentType, String documentNumber) {
        return Acquisition.builder()
                .id(id)
                .documentNumber("123456789")
                .stateAcquisition(StateAcquisition.builder()
                        .name("ACTIVO")
                        .build())
                .build();
	}

	@Override
	public Acquisition findById(UUID idAcquisition) {
        return Acquisition.builder()
                .id(idAcquisition)
                .documentNumber("123456789")
                .stateAcquisition(StateAcquisition.builder()
                        .name("ACTIVO")
                        .build())
                .build();
	}
}
