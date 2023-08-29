package co.com.bancolombia.usecase.rabbit.segmentcustomer;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignCurrencyInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.SegmentCustomerReply;
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
public class SegmentCustomerFinalUseCase {

    private final ConstructResponsesUseCase constructResponsesUseCase;
    private final TaxInformationRepository taxInformationRepository;
    private final TaxCountryRepository taxCountryRepository;
    private final ForeignInformationRepository foreignInformationRepository;
    private final ForeignInformationCurrencyRepository foreignInformationCurrencyRepository;

    public SegmentCustomerReply segmentCustomerReplyFinal(
            SegmentCustomerReply segmentCustomerReply, Acquisition acquisition){
        List<TaxCountry> taxCountry = taxCountryRepository.findAllByAcquisition(acquisition);
        List<TaxCountryInfoReply> taxCountryInfoReplies = new ArrayList<>();
        taxCountry.forEach(i -> taxCountryInfoReplies.add(constructResponsesUseCase.fromOriginal(i)));
        TaxInformation taxInformation = taxInformationRepository.findByAcquisition(acquisition);
        TaxInfoReply taxInfoReply = constructResponsesUseCase.fromOriginal(taxInformation, taxCountryInfoReplies);

        ForeignInformation foreignInformation = foreignInformationRepository.findByAcquisition(acquisition);
        List<ForeignInformationCurrency> foreignInformationCurrencies =
                foreignInformationCurrencyRepository.findByForeignInformation(foreignInformation);
        List<ForeignCurrencyInfoReply> foreignCurrencyInfoReplies = new ArrayList<>();
        foreignInformationCurrencies.forEach(i ->
                foreignCurrencyInfoReplies.add(constructResponsesUseCase.fromOriginal(i)));
        ForeignInfoReply foreignInfoReply = constructResponsesUseCase
                .fromOriginal(foreignInformation, foreignCurrencyInfoReplies);

        return segmentCustomerReply.toBuilder().taxInfo(taxInfoReply).foreignInfo(foreignInfoReply)
                .build();
    }
}