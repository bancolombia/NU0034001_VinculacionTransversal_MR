package co.com.bancolombia.model.controllist.gateways;

import co.com.bancolombia.model.controllist.ControlListSave;

public interface ControlListSaveRepository {

    ControlListSave save(ControlListSave controlListSave);

    String findStateValidationCustomerControlList(String acquisitionId, String documentType, String documentNUmber);
}
