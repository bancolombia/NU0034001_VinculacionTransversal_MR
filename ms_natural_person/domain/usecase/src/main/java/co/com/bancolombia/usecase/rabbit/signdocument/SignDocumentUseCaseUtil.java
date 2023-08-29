package co.com.bancolombia.usecase.rabbit.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignCurrencyInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxCountryInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformation.gateways.ForeignInformationRepository;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import co.com.bancolombia.model.foreigninformationcurrency.gateways.ForeignInformationCurrencyRepository;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.model.taxcountry.gateways.TaxCountryRepository;
import co.com.bancolombia.taxinformation.TaxInformation;
import co.com.bancolombia.taxinformation.gateways.TaxInformationRepository;
import co.com.bancolombia.usecase.rabbit.util.ConstructResponsesUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SignDocumentUseCaseUtil {

    private final ContactInformationRepository contactInfoRepository;
    private final TaxInformationRepository taxInfoRepository;
    private final TaxCountryRepository taxCountryRepository;
    private final ForeignInformationRepository foreignInfoRepository;
    private final ForeignInformationCurrencyRepository foreignInfoCurrencyRepository;
    private final ConstructResponsesUseCase consResponseUseCase;

    public ContactInfoReply getContactInfoReply(Acquisition acquisition) {
        List<ContactInformation> allContactInformation = contactInfoRepository.findAllByAcquisition(acquisition);
        List<ContactInfoCompReply> contactInfoReplies = new ArrayList<>();
        allContactInformation.forEach(ci -> contactInfoReplies.add(consResponseUseCase.fromOriginal(ci)));
        return ContactInfoReply.builder().createdDate(allContactInformation.get(0).getCreatedDate())
                .contactInfoCompList(contactInfoReplies).build();
    }

    public TaxInfoReply getTaxInfoReply(Acquisition acquisition) {
        List<TaxCountry> taxCountry = taxCountryRepository.findAllByAcquisition(acquisition);
        List<TaxCountryInfoReply> taxCountryInfoReplies = new ArrayList<>();
        taxCountry.forEach(tc -> taxCountryInfoReplies.add(consResponseUseCase.fromOriginal(tc)));
        TaxInformation taxInformation = taxInfoRepository.findByAcquisition(acquisition);
        return consResponseUseCase.fromOriginal(taxInformation, taxCountryInfoReplies);
    }

    public ForeignInfoReply getForeignInfoReply(Acquisition acquisition) {
        ForeignInformation foreignInfo = foreignInfoRepository.findByAcquisition(acquisition);
        List<ForeignInformationCurrency> foreignInfoCurrencies = foreignInfoCurrencyRepository
                .findByForeignInformation(foreignInfo);
        List<ForeignCurrencyInfoReply> foreignCurrencyInfoReplies = new ArrayList<>();
        foreignInfoCurrencies.forEach(i -> foreignCurrencyInfoReplies.add(consResponseUseCase.fromOriginal(i)));
        return consResponseUseCase.fromOriginal(foreignInfo, foreignCurrencyInfoReplies);
    }
}
