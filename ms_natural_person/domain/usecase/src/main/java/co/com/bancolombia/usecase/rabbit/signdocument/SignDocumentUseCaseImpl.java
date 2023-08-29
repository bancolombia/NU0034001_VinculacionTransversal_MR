package co.com.bancolombia.usecase.rabbit.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateTokenReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.rabbit.util.ConstructResponsesUseCase;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class SignDocumentUseCaseImpl implements SignDocumentUseCase {

    private final ConstructResponsesUseCase consResponseUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;
    private final BasicInformationUseCase basicInfoUseCase;
    private final PersonalInformationRepository personalInfoRepository;
    private final EconomicInformationRepository economicInfoRepository;
    private final SignDocumentUseCaseUtil signDocumentUseCaseUtil;

    @Override
    public SignDocumentReply signDocumentReply(NpGlobalServicesQuery query) {
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();
        ValidateTokenReply validateTokenReply = consResponseUseCase.fromOriginal(validateTokenUseCase
                .findByAcquisitionLast(acquisition));
        Optional<BasicInformation> basicInformation = basicInfoUseCase.findByAcquisition(acquisition);
        BasicInformation basicInfo = basicInformation.orElseGet(() -> BasicInformation.builder().build());
        BasicInfoReply basicInfoReply = consResponseUseCase.fromOriginal(basicInfo);
        PersonalInfoReply personalInfoReply = consResponseUseCase.fromOriginal(personalInfoRepository
                .findByAcquisition(acquisition));
        ContactInfoReply contactInfoReply = signDocumentUseCaseUtil.getContactInfoReply(acquisition);
        EconomicInfoReply economicInfoReply = consResponseUseCase.fromOriginal(economicInfoRepository
                .findByAcquisition(acquisition));
        TaxInfoReply taxInfoReply = signDocumentUseCaseUtil.getTaxInfoReply(acquisition);
        ForeignInfoReply foreignInfoReply = signDocumentUseCaseUtil.getForeignInfoReply(acquisition);
        return SignDocumentReply.builder().validateToken(validateTokenReply).basicInfo(basicInfoReply)
                .valid(true).personalInfo(personalInfoReply).contactInfo(contactInfoReply)
                .economicInfo(economicInfoReply).taxInfo(taxInfoReply).foreignInfo(foreignInfoReply).build();
    }
}
