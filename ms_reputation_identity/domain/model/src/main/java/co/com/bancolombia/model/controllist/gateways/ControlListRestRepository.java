package co.com.bancolombia.model.controllist.gateways;

import co.com.bancolombia.model.controllist.ControlListRequest;
import co.com.bancolombia.model.controllist.ControlListResponse;

import java.util.Date;

public interface ControlListRestRepository {

    ControlListResponse getUserInfoFromControlList(ControlListRequest controlListRequest, String messageId,
                                                   Date dateRequestApi);
}
