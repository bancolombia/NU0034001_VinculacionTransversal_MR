package co.com.bancolombia.model.markcustomer.gateways;

import co.com.bancolombia.model.markcustomer.MarkCustomerResponseWithLog;
import co.com.bancolombia.model.markcustomer.RegisterControlListRequest;

import java.util.Date;

public interface RegisterControlListRestRepository {

    MarkCustomerResponseWithLog getRegisterControl(RegisterControlListRequest request, String messageId,
                                                          Date dateRequestApi);
}
