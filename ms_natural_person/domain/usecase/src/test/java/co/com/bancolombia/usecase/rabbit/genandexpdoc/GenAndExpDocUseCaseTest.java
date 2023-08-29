package co.com.bancolombia.usecase.rabbit.genandexpdoc;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.usecase.rabbit.util.ConstructResponsesUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_GEN_EXP_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_RES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_WORK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class GenAndExpDocUseCaseTest {

    @InjectMocks
    @Spy
    private GenAndExpDocUseCaseImpl genAndExpDocUseCase;

    @Mock
    ConstructResponsesUseCase constructReplyGenAndDoc;
    @Mock
    ContactInformationRepository contactInformationRepository;
    @Mock
    EconomicInformationRepository economicInformationRepository;
    @Mock
    GenAndExpDocUseCaseUtilTwo genAndExpDocUseCaseUtilTwo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void genAndExpDocUseCaseReplyTypeTest() {
        NpGlobalServicesQuery query = NpGlobalServicesQuery.builder().acquisitionId(UUID.randomUUID().toString()).operation(CODE_GEN_EXP_DOCS).build();

        List<ContactInformation> allByAcquisition = new ArrayList<>();

        doReturn(allByAcquisition).when(contactInformationRepository).findAllByAcquisition((any(Acquisition.class)));
        doReturn(ContactInfoCompReply.builder().addressType(CO_ADDRESS_TYPE_WORK).build()).when(constructReplyGenAndDoc).fromOriginal(any(ContactInformation.class));

        doReturn(EconomicInformation.builder().build()).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));
        doReturn(EconomicInfoReply.builder().build()).when(constructReplyGenAndDoc).fromOriginal(any(EconomicInformation.class));

        genAndExpDocUseCase.genAndExpDocUseCaseOneReply(query);

        verify(this.genAndExpDocUseCase, times(1)).genAndExpDocUseCaseOneReply(query);
    }

    @Test
    public void genAndExpDocUseCaseReplyResTest() {

        List<ContactInformation> allByAcquisition = new ArrayList<>();
        allByAcquisition.add(ContactInformation.builder().addressType(CO_ADDRESS_TYPE_RES).build());

        doReturn(allByAcquisition).when(contactInformationRepository).findAllByAcquisition((any(Acquisition.class)));
                doReturn(ContactInfoCompReply.builder().addressType(CO_ADDRESS_TYPE_RES).build()).when(constructReplyGenAndDoc).fromOriginal(any(ContactInformation.class));

        doReturn(EconomicInformation.builder().build()).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));
        doReturn(EconomicInfoReply.builder().build()).when(constructReplyGenAndDoc).fromOriginal(any(EconomicInformation.class));

        genAndExpDocUseCase.constructContactInformation(CO_ADDRESS_TYPE_RES, allByAcquisition);

        verify(this.genAndExpDocUseCase, times(1)).constructContactInformation(CO_ADDRESS_TYPE_RES, allByAcquisition);
    }




}