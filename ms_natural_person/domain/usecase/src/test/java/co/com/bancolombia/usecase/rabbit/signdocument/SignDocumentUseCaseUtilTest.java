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
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

public class SignDocumentUseCaseUtilTest {

    @InjectMocks
    @Spy
    private SignDocumentUseCaseUtil signDocumentUseCase;

    @Mock
    private ContactInformationRepository contactInformationRepository;
    @Mock
    private TaxInformationRepository taxInformationRepository;
    @Mock
    private TaxCountryRepository taxCountryRepository;
    @Mock
    private ForeignInformationRepository foreignInformationRepository;
    @Mock
    private ForeignInformationCurrencyRepository foreignInformationCurrencyRepository;
    @Mock
    private ConstructResponsesUseCase constructResponsesUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getContactInformationTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        ContactInformation contactInformation = ContactInformation.builder().companyName("asd").build();
        doReturn(contactInformation).when(contactInformationRepository)
                .findByAcquisitionAndAddressType(any(Acquisition.class), anyString());
        doReturn(Collections.singletonList(contactInformation)).when(contactInformationRepository)
                .findAllByAcquisition(any(Acquisition.class));
        ContactInfoCompReply contactInfoCompReply = ContactInfoCompReply.builder().build();
        doReturn(contactInfoCompReply).when(constructResponsesUseCase).fromOriginal(any(ContactInformation.class));
        ContactInfoReply contactInfoReply = signDocumentUseCase.getContactInfoReply(acquisition);
        doReturn(contactInfoReply).when(signDocumentUseCase).getContactInfoReply(any(Acquisition.class));
        assertNotNull(contactInfoReply);
    }

    @Test
    public void getTaxInfoReply(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        TaxInformation taxInformation = TaxInformation.builder().build();
        doReturn(taxInformation).when(taxInformationRepository).findByAcquisition(any(Acquisition.class));
        TaxCountry taxCountry = TaxCountry.builder().build();
        doReturn(Collections.singletonList(taxCountry)).when(taxCountryRepository).findAllByAcquisition(any(Acquisition.class));
        TaxCountryInfoReply taxCountryInfoReply = TaxCountryInfoReply.builder().build();
        doReturn(taxCountryInfoReply).when(constructResponsesUseCase).fromOriginal(any(TaxCountry.class));
        TaxInfoReply taxInfoReply = TaxInfoReply.builder().build();
        doReturn(taxInfoReply).when(constructResponsesUseCase).fromOriginal(any(TaxInformation.class), any(List.class));
        doReturn(taxInfoReply).when(constructResponsesUseCase).fromOriginal(any(TaxInformation.class), any(List.class));
        TaxInfoReply taxInfoReply1 = signDocumentUseCase.getTaxInfoReply(acquisition);
        doReturn(taxInfoReply1).when(signDocumentUseCase).getTaxInfoReply(any(Acquisition.class));
        assertNotNull(taxInfoReply1);
    }

    @Test
    public void getForeignInfoReplyTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
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
        doReturn(foreignInfoReply).when(constructResponsesUseCase).fromOriginal(any(ForeignInformation.class),
                any(List.class));
        ForeignInfoReply foreignInfoReply1 = signDocumentUseCase.getForeignInfoReply(acquisition);
        doReturn(foreignInfoReply1).when(signDocumentUseCase).getForeignInfoReply(any(Acquisition.class));
        assertNotNull(foreignInfoReply1);
    }
}
