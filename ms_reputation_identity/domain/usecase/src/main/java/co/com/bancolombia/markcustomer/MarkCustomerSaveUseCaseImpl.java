package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.markcustomer.RegisterControlListResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListSave;
import co.com.bancolombia.model.markcustomer.gateways.RegisterControlListRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MarkCustomerSaveUseCaseImpl implements MarkCustomerSaveUseCase {

    private final RegisterControlListRepository repository;
    private final CoreFunctionDate coreFD;

    @Override
    public void saveInfo(RegisterControlListResponse response, Acquisition acquisition,
                         BasicAcquisitionRequest request) {
        repository.save(this.transSave(response, acquisition, request));
    }

    @Override
    public RegisterControlListSave transSave(RegisterControlListResponse response, Acquisition acquisition,
                                             BasicAcquisitionRequest ba) {
        return RegisterControlListSave.builder()
                .acquisitionId(acquisition.getId().toString())
                .createdBy(ba.getUserTransaction())
                .createdDate(coreFD.getDatetime())
                .messageId(response.getMeta().getMessageId())
                .requestDate(response.getMeta().getRequestDate()).build();
    }
}
