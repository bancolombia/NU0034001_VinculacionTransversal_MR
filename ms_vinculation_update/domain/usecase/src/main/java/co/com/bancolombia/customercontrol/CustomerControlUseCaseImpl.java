package co.com.bancolombia.customercontrol;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.customercontrol.CustomerControl;
import co.com.bancolombia.model.customercontrol.gateways.CustomerControlRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class CustomerControlUseCaseImpl implements CustomerControlUseCase {

    private final CustomerControlRepository repository;

    @Override
    public void blockCustomer(String docType, String docNumber, Date unlockDate, String operation) {
        Date now = new CoreFunctionDate().getDatetime();
        CustomerControl customerControl = CustomerControl.builder()
                .documentType(docType).documentNumber(docNumber).operation(operation)
                .unlockDate(unlockDate).registrationDate(now).createdDate(now)
                .state(Numbers.ONE.getNumber()).build();
        save(customerControl);
    }

    @Override
    public String unblockCustomer(CustomerControl customerControl) {
        String timeBlock = null;
        CoreFunctionDate coreFunctionDate = new CoreFunctionDate();
        if (customerControl.getState().equals(Numbers.ONE.getNumber())) {
            timeBlock = coreFunctionDate
                    .compareDifferenceTime(customerControl.getUnlockDate(), null, false, true);
            if (timeBlock == null) {
                customerControl.setState(Numbers.TWO.getNumber());
                customerControl.setUpdatedDate(coreFunctionDate.getDatetime());
                save(customerControl);
            }
        }
        return timeBlock;
    }

    @Override
    public CustomerControl findByDocumentTypeAndDocumentNumber(String docType, String docNumber) {
        return repository.findByDocumentTypeAndDocumentNumber(docType, docNumber);
    }

    @Override
    public CustomerControl save(CustomerControl customerControl) {
        repository.save(customerControl);
        return customerControl;
    }
}