package co.com.bancolombia.usecase.rabbit.segmentcustomer;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.SegmentCustomerReply;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.rabbit.util.ConstructResponsesUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_WORK;

@RequiredArgsConstructor
public class SegmentCustomerInitialUseCase {

    private final ConstructResponsesUseCase constructResponsesUseCase;
    private final BasicInformationUseCase basicInformationUseCase;
    private final PersonalInformationRepository personalInformationRepository;
    private final ContactInformationRepository contactInformationRepository;
    private final EconomicInformationRepository economicInformationRepository;
    private final SegmentCustomerFinalUseCase segmentCustomerFinalUseCase;

    private Acquisition acquisition;

    public SegmentCustomerReply segmentCustomerReply(NpGlobalServicesQuery query){
        this.acquisition = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();
        Optional<BasicInformation> basicInformation = basicInformationUseCase.findByAcquisition(acquisition);
        assert basicInformation.isPresent();
        BasicInfoReply basicInfoReply = constructResponsesUseCase.fromOriginal(basicInformation.get());

        PersonalInformation personalInformation = personalInformationRepository.findByAcquisition(acquisition);
        PersonalInfoReply personalInfoReply = constructResponsesUseCase.fromOriginal(personalInformation);

        ContactInformation contactInformation = contactInformationRepository
                .findByAcquisitionAndAddressType(acquisition, CO_ADDRESS_TYPE_WORK);
        List<ContactInformation> allContactInformation = contactInformationRepository
                .findAllByAcquisition(acquisition);
        List<ContactInfoCompReply> contactInfoReplies = new ArrayList<>();
        allContactInformation.forEach(i -> contactInfoReplies.add(constructResponsesUseCase.fromOriginal(i)));
        ContactInfoReply contactInfoReply = ContactInfoReply.builder()
                .companyName(contactInformation.getCompanyName()).contactInfoCompList(contactInfoReplies).build();

        EconomicInformation economicInformation = economicInformationRepository.findByAcquisition(acquisition);
        EconomicInfoReply economicInfoReply = constructResponsesUseCase.fromOriginal(economicInformation);

        return segmentCustomerFinalUseCase.segmentCustomerReplyFinal(
                SegmentCustomerReply.builder().basicInfo(basicInfoReply)
                .valid(true).personalInfo(personalInfoReply).contactInfo(contactInfoReply)
                .economicInfo(economicInfoReply).build(), acquisition);
    }
}