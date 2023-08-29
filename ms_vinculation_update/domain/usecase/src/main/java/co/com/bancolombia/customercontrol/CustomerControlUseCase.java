package co.com.bancolombia.customercontrol;

import co.com.bancolombia.model.customercontrol.CustomerControl;

import java.util.Date;

public interface CustomerControlUseCase {
    public void blockCustomer(String docType, String docNumber, Date unlockDate, String operation);
    public String unblockCustomer(CustomerControl customerControl);
    public CustomerControl findByDocumentTypeAndDocumentNumber(String docType, String docNumber);
    public CustomerControl save(CustomerControl customerControl);
}
