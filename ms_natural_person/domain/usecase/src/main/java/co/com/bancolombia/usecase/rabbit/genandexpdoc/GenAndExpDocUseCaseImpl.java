package co.com.bancolombia.usecase.rabbit.genandexpdoc;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.GenExposeReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.usecase.rabbit.util.ConstructResponsesUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
public class GenAndExpDocUseCaseImpl implements GenAndExpDocUseCase {

    private final ConstructResponsesUseCase constructReplyGenAndDoc;
    private final ContactInformationRepository contactInformationRepository;
    private final EconomicInformationRepository economicInformationRepository;
    private final GenAndExpDocUseCaseUtilTwo genAndExpDocUseCaseUtilTwo;


    @Override
    public GenExposeReply genAndExpDocUseCaseOneReply(NpGlobalServicesQuery query) {
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();

        List<ContactInformation> allByAcquisition = findAllContactsForAcquisition(acquisition);

        ContactInformation contactInfoRes =
                constructContactInformation(Constants.CO_ADDRESS_TYPE_RES, allByAcquisition);
        ContactInfoCompReply contactInfoResReply = constructReplyGenAndDoc.fromOriginal(contactInfoRes);

        ContactInformation contactInfoWork =
                constructContactInformation(Constants.CO_ADDRESS_TYPE_WORK, allByAcquisition);
        ContactInfoCompReply contactInfoWorkReply = constructReplyGenAndDoc.fromOriginal(contactInfoWork);

        EconomicInformation economicInformation = findEconomicInfoByAcquisition(acquisition);
        EconomicInfoReply economicInfoReply = constructReplyGenAndDoc.fromOriginal(economicInformation);

        return genAndExpDocUseCaseUtilTwo.genExposeTwoReply(acquisition, GenExposeReply.builder()
                .contactWorkInfo(contactInfoWorkReply).contactResInfo(contactInfoResReply)
                .economicInfo(economicInfoReply).build());


    }

    private List<ContactInformation> findAllContactsForAcquisition(Acquisition acquisition) {
        return contactInformationRepository.findAllByAcquisition(acquisition);
    }

    private EconomicInformation findEconomicInfoByAcquisition(Acquisition acquisition) {
        return economicInformationRepository.findByAcquisition(acquisition);
    }

    public ContactInformation constructContactInformation(
            String codeAddressType, List<ContactInformation> contactInformationList){
        Optional<ContactInformation> contactInformation = contactInformationList.stream()
                .filter(p -> p.getAddressType().equals(codeAddressType)).findFirst();
        return contactInformation.isPresent() ? contactInformation.get() : ContactInformation.builder().build();
    }
}

