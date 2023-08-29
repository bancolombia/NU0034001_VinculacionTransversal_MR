package co.com.bancolombia.usecase.rabbit.genandexpdoc;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.GenExposeReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignCurrencyInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxCountryInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
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
public class GenAndExpDocUseCaseUtilThree {

    private final ConstructResponsesUseCase constructReplyGenAndDoc;
    private final TaxInformationRepository taxInformationRepository;
    private final TaxCountryRepository taxCountryRepository;
    private final ForeignInformationRepository foreignInformationRepository;
    private final ForeignInformationCurrencyRepository foreignInformationCurrencyRepository;

    public GenExposeReply genExposeThreeReply(Acquisition acquisition, GenExposeReply reply) {

        List<TaxCountry> taxCountry = taxCountryListByAcquisition(acquisition);
        List<TaxCountryInfoReply> taxCountryInfoReplies = taxCountryListToReplyList(taxCountry);

        TaxInformation taxInformation = taxInformationByAcquisition(acquisition);
        TaxInfoReply taxInfoReply =
                constructReplyGenAndDoc.fromOriginal(taxInformation, taxCountryInfoReplies);

        ForeignInformation foreignInformation = foreignInformationByAcquisition(acquisition);
        List<ForeignInformationCurrency> foreignInformationCurrencies =
                foreignInfoCurrencyListByForeignInfo(foreignInformation);

        List<ForeignCurrencyInfoReply> foreignCurrencyInfoReplies =
                foreignInfoCurrenciesToReplyList(foreignInformationCurrencies);
        ForeignInfoReply foreignInfoReply = constructReplyGenAndDoc
                .fromOriginal(foreignInformation, foreignCurrencyInfoReplies);

        return reply.toBuilder().taxInfo(taxInfoReply).foreignInfo(foreignInfoReply)
                .build();

    }

    private List<TaxCountry> taxCountryListByAcquisition(Acquisition acquisition) {
        return taxCountryRepository.findAllByAcquisition(acquisition);
    }

    private List<TaxCountryInfoReply> taxCountryListToReplyList(List<TaxCountry> taxCountry) {
        List<TaxCountryInfoReply> taxCountryInfoReplies = new ArrayList<>();
        taxCountry.forEach(i ->
                taxCountryInfoReplies.add(constructReplyGenAndDoc.fromOriginal(i)));
        return taxCountryInfoReplies;
    }

    private TaxInformation taxInformationByAcquisition(Acquisition acquisition) {
        return taxInformationRepository.findByAcquisition(acquisition);
    }

    private ForeignInformation foreignInformationByAcquisition(Acquisition acquisition) {
        return foreignInformationRepository.findByAcquisition(acquisition);
    }

    private List<ForeignInformationCurrency> foreignInfoCurrencyListByForeignInfo(
            ForeignInformation foreignInformation) {
        return foreignInformationCurrencyRepository.findByForeignInformation(foreignInformation);
    }

    private List<ForeignCurrencyInfoReply> foreignInfoCurrenciesToReplyList(
            List<ForeignInformationCurrency> foreignInformationCurrencies) {
        List<ForeignCurrencyInfoReply> foreignCurrencyInfoReplies = new ArrayList<>();
        foreignInformationCurrencies.forEach(i ->
                foreignCurrencyInfoReplies.add(constructReplyGenAndDoc.fromOriginal(i)));
        return  foreignCurrencyInfoReplies;
    }
}
