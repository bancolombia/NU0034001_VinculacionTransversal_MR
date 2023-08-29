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
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.rabbit.util.ConstructResponsesUseCase;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SignDocumentUseCaseTest {

    @InjectMocks
    @Spy
    private SignDocumentUseCaseImpl signDocumentUseCase;

    @Mock
    private ValidateTokenUseCase validateTokenUseCase;
    @Mock
    private BasicInformationUseCase basicInformationUseCase;
    @Mock
    private PersonalInformationRepository personalInformationRepository;
    @Mock
    private EconomicInformationRepository economicInformationRepository;
    @Mock
    private ConstructResponsesUseCase constructResponsesUseCase;
    @Mock
    private SignDocumentUseCaseUtil signDocumentUseCaseUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void signDocumentReplyTest() {

        ValidateToken validateToken = ValidateToken.builder().tokenCode("12312").build();
        doReturn(validateToken).when(validateTokenUseCase).findByAcquisition(any(Acquisition.class));
        ValidateTokenReply validateTokenReply = ValidateTokenReply.builder().tokenCode("38492").build();
        doReturn(validateTokenReply).when(constructResponsesUseCase).fromOriginal(any(ValidateToken.class));

        BasicInformation basicInformation = BasicInformation.builder().build();
        doReturn(Optional.of(basicInformation)).when(basicInformationUseCase).findByAcquisition(any(Acquisition.class));
        BasicInfoReply basicInfoReply = BasicInfoReply.builder().build();
        doReturn(basicInfoReply).when(constructResponsesUseCase).fromOriginal(any(BasicInformation.class));

        PersonalInformation personalInformation = PersonalInformation.builder().build();
        doReturn(personalInformation).when(personalInformationRepository).findByAcquisition(any(Acquisition.class));
        PersonalInfoReply personalInfoReply = PersonalInfoReply.builder().build();
        doReturn(personalInfoReply).when(constructResponsesUseCase).fromOriginal(any(PersonalInformation.class));

        ContactInfoReply contactInfoReply = ContactInfoReply.builder().build();
        doReturn(contactInfoReply).when(signDocumentUseCaseUtil).getContactInfoReply(any(Acquisition.class));

        EconomicInformation economicInformation = EconomicInformation.builder().build();
        doReturn(economicInformation).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));
        EconomicInfoReply economicInfoReply = EconomicInfoReply.builder().build();
        doReturn(economicInfoReply).when(constructResponsesUseCase).fromOriginal(any(EconomicInformation.class));

        TaxInfoReply taxInfoReply = TaxInfoReply.builder().build();
        doReturn(taxInfoReply).when(signDocumentUseCaseUtil).getTaxInfoReply(any(Acquisition.class));

        ForeignInfoReply foreignInfoReply = ForeignInfoReply.builder().build();
        doReturn(foreignInfoReply).when(signDocumentUseCaseUtil).getForeignInfoReply(any(Acquisition.class));

        NpGlobalServicesQuery query = NpGlobalServicesQuery.builder().acquisitionId(UUID.randomUUID().toString()).build();
        SignDocumentReply signDocumentReply = signDocumentUseCase.signDocumentReply(query);
        doReturn(signDocumentReply).when(signDocumentUseCase).signDocumentReply(any(NpGlobalServicesQuery.class));

        assertNotNull(signDocumentReply);
    }
}

