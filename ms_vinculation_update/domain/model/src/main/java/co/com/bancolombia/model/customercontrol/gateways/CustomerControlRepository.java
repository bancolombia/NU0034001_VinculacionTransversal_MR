package co.com.bancolombia.model.customercontrol.gateways;

import co.com.bancolombia.model.customercontrol.CustomerControl;

public interface CustomerControlRepository {
    CustomerControl save(CustomerControl customerControl);

    CustomerControl findByDocumentTypeAndDocumentNumber(String docType, String docNumber);
}
