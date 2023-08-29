package co.com.bancolombia.usecase.rabbit.segmentcustomer;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SegmentCustomerInitialUseCaseTest {

    @InjectMocks
    @Spy
    private SegmentCustomerInitialUseCase segmentCustomerUseCase;

    @Mock
    ConstructResponsesUseCase constructResponsesUseCase;

    @Mock
    BasicInformationUseCase basicInformationUseCase;

    @Mock
    PersonalInformationRepository personalInformationRepository;

    @Mock
    ContactInformationRepository contactInformationRepository;

    @Mock
    EconomicInformationRepository economicInformationRepository;

    @Mock
    SegmentCustomerFinalUseCase segmentCustomerFinalUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void segmentCustomerReply(){
        BasicInformation basicInformation = BasicInformation.builder().build();
        doReturn(Optional.of(basicInformation)).when(basicInformationUseCase)
                .findByAcquisition(any(Acquisition.class));
        BasicInfoReply basicInfoReply = BasicInfoReply.builder().build();
        doReturn(basicInfoReply).when(constructResponsesUseCase).fromOriginal(any(BasicInformation.class));

        PersonalInformation personalInformation = PersonalInformation.builder().build();
        doReturn(personalInformation).when(personalInformationRepository).findByAcquisition(any(Acquisition.class));
        PersonalInfoReply personalInfoReply = PersonalInfoReply.builder().build();
        doReturn(personalInfoReply).when(constructResponsesUseCase).fromOriginal(any(PersonalInformation.class));


        ContactInformation contactInformation = ContactInformation.builder().companyName("asd").build();
        doReturn(contactInformation).when(contactInformationRepository)
                .findByAcquisitionAndAddressType(any(Acquisition.class), anyString());
        doReturn(Collections.singletonList(contactInformation)).when(contactInformationRepository)
                .findAllByAcquisition(any(Acquisition.class));
        ContactInfoCompReply contactInfoReply = ContactInfoCompReply.builder().build();
        doReturn(contactInfoReply).when(constructResponsesUseCase).fromOriginal(any(ContactInformation.class));

        EconomicInformation economicInformation = EconomicInformation.builder().build();
        doReturn(economicInformation).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));
        EconomicInfoReply economicInfoReply = EconomicInfoReply.builder().build();
        doReturn(economicInfoReply).when(constructResponsesUseCase).fromOriginal(any(EconomicInformation.class));

        doReturn(SegmentCustomerReply.builder().build()).when(segmentCustomerFinalUseCase)
                .segmentCustomerReplyFinal(any(SegmentCustomerReply.class), any(Acquisition.class));
        NpGlobalServicesQuery query = NpGlobalServicesQuery.builder()
                .acquisitionId(UUID.randomUUID().toString()).build();
        SegmentCustomerReply segmentCustomerReply = segmentCustomerUseCase.segmentCustomerReply(query);
        assertNotNull(segmentCustomerReply);
    }
}