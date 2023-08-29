package co.com.bancolombia.logfunctionalvnt.log.model.gateways;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;

public interface ILogFunctionalRepository {
    public LogFunctionalDTO save(LogFunctionalDTO logFunctionalDTO);
    public AcquisitionStateReply acquisitionStateReply(AcquisitionStateQuery acquisitionStateQuery);
}
