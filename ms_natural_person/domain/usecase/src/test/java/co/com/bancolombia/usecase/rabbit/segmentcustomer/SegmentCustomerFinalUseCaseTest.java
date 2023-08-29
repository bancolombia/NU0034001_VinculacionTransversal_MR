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
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SegmentCustomerFinalUseCaseTest {

    @InjectMocks
    @Spy
    SegmentCustomerFinalUseCase segmentCustomerFinalUseCase;

    @Mock
    ConstructResponsesUseCase constructResponsesUseCase;
    @Mock
    TaxInformationRepository taxInformationRepository;
    @Mock
    TaxCountryRepository taxCountryRepository;
    @Mock
    ForeignInformationRepository foreignInformationRepository;
    @Mock
    ForeignInformationCurrencyRepository foreignInformationCurrencyRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void segmentCustomerReplyFinal(){
        TaxInformation taxInformation = TaxInformation.builder().build();
        doReturn(taxInformation).when(taxInformationRepository).findByAcquisition(any(Acquisition.class));
        TaxCountry taxCountry = TaxCountry.builder().build();
        doReturn(Collections.singletonList(taxCountry)).when(taxCountryRepository).findAllByAcquisition(any(Acquisition.class));
        TaxCountryInfoReply taxCountryInfoReply = TaxCountryInfoReply.builder().build();
        doReturn(taxCountryInfoReply).when(constructResponsesUseCase).fromOriginal(any(TaxCountry.class));
        TaxInfoReply taxInfoReply = TaxInfoReply.builder().build();
        doReturn(taxInfoReply).when(constructResponsesUseCase).fromOriginal(any(TaxInformation.class), any(List.class));

        ForeignInformation foreignInformation = ForeignInformation.builder().build();
        doReturn(foreignInformation).when(foreignInformationRepository).findByAcquisition(any(Acquisition.class));
        ForeignInformationCurrency foreignInformationCurrency = ForeignInformationCurrency.builder().build();
        ForeignInformationCurrency foreignInformationCurrency2 = ForeignInformationCurrency.builder().build();
        doReturn(ImmutableList.of(foreignInformationCurrency, foreignInformationCurrency2)).when(foreignInformationCurrencyRepository)
                .findByForeignInformation(any(ForeignInformation.class));
        ForeignCurrencyInfoReply foreignCurrencyInfoReply = ForeignCurrencyInfoReply.builder().build();
        doReturn(foreignCurrencyInfoReply).when(constructResponsesUseCase)
                .fromOriginal(any(ForeignInformationCurrency.class));
        ForeignInfoReply foreignInfoReply = ForeignInfoReply.builder().build();
        doReturn(foreignInfoReply).when(constructResponsesUseCase)
                .fromOriginal(any(ForeignInformation.class), any(List.class));

        SegmentCustomerReply segmentCustomerReply = segmentCustomerFinalUseCase
                .segmentCustomerReplyFinal(SegmentCustomerReply.builder().build(), Acquisition.builder().build());
        assertNotNull(segmentCustomerReply);
    }
}