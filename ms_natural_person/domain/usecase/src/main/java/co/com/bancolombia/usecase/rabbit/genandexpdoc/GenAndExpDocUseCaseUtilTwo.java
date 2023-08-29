package co.com.bancolombia.usecase.rabbit.genandexpdoc;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.GenExposeReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.rabbit.util.ConstructResponsesUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GenAndExpDocUseCaseUtilTwo {

    private final ConstructResponsesUseCase constructReplyGenAndDoc;
    private final BasicInformationUseCase basicInformationUseCase;
    private final PersonalInformationRepository personalInformationRepository;
    private final GenAndExpDocUseCaseUtilThree genAndExpDocUseCaseUtilThree;

    public GenExposeReply genExposeTwoReply(Acquisition acquisition, GenExposeReply reply) {

        BasicInformation basicInformation = findAcquisitionBasicInfo(acquisition);
        BasicInfoReply basicInfoReply = constructReplyGenAndDoc.fromOriginal(basicInformation);

        PersonalInformation personalInformation = findAcquisitionPersonalInfo(acquisition);
        PersonalInfoReply personalInfoReply = constructReplyGenAndDoc.fromOriginal(personalInformation);

        return genAndExpDocUseCaseUtilThree.genExposeThreeReply(acquisition, reply.toBuilder()
                .basicInfo(basicInfoReply).personalInfo(personalInfoReply).build());
    }

    public BasicInformation findAcquisitionBasicInfo(Acquisition acquisition) {
        Optional<BasicInformation> basicInformation = basicInformationUseCase.findByAcquisition(acquisition);
        return basicInformation.isPresent() ? basicInformation.get() : BasicInformation.builder().build();
    }

    private PersonalInformation findAcquisitionPersonalInfo(Acquisition acquisition) {
        return personalInformationRepository.findByAcquisition(acquisition);
    }

}
