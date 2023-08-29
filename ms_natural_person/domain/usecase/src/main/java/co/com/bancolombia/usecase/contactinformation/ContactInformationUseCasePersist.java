package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import lombok.Data;

import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_RES;

@Data
public class ContactInformationUseCasePersist {
	
	private final ContactInformationRepository contactInformationRepository;
	private final ContactIValidationUseCase cValidationUseCase;

	public List<ContactInformation> findAllByAcquisition(Acquisition acquisition){
	    return contactInformationRepository.findAllByAcquisition(acquisition);
    }

    public ContactInformationUseCasePersist(ContactInformationRepository contactInformationRepository,
	        ContactIValidationUseCase cValidationUseCase) {
		this.contactInformationRepository = contactInformationRepository;
		this.cValidationUseCase = cValidationUseCase;
	}

	public ContactInformation save(ContactInformation contactInformation) {
        ContactInformation information = this.contactInformationRepository
                .findByAcquisitionAndAddressType(contactInformation.getAcquisition(), CO_ADDRESS_TYPE_RES);
        cValidationUseCase.maxRepetitionEmailCellphone(contactInformation);
        if (information != null) {
            information = information.merge(contactInformation).toBuilder()
                    .updatedBy(contactInformation.getCreatedBy())
                    .updatedDate(contactInformation.getCreatedDate()).build();
        } else {
            information = contactInformation;
        }
        return this.contactInformationRepository.save(information);
    }

    public List<ContactInformation> save(List<ContactInformation> contactInformationList) {
        return this.contactInformationRepository.saveAll(contactInformationList);
    }

    public ContactInformation findByAcquisitionAndAddressType(Acquisition acquisition, String addressType){
	    return contactInformationRepository.findByAcquisitionAndAddressType(acquisition, addressType);
    }
}
