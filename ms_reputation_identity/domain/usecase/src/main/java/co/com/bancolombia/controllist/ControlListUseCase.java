package co.com.bancolombia.controllist;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.controllist.ControlList;

public interface ControlListUseCase {

    ControlList startProcessControlList(AcquisitionReply acquisitionReply, BasicAcquisitionRequest baRequest);

    String findStateValidationCustomerControlList(String acquisitionId, String documentType, String documentNUmber);
}
