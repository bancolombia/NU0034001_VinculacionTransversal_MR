package co.com.bancolombia.controllist;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.controllist.ControlList;
import co.com.bancolombia.model.controllist.ControlListResponse;

public interface ControlListTransformUseCase {

    ControlList transformInfoControlList(AcquisitionReply acquisition,
                                         ControlListResponse controlListResponse);
}
