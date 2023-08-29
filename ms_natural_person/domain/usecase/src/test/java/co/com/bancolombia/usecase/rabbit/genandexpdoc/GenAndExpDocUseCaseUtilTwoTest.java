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
import junit.framework.TestCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class GenAndExpDocUseCaseUtilTwoTest extends TestCase {

    @InjectMocks
    @Spy
    private GenAndExpDocUseCaseUtilTwo genAndExpDocUseCaseUtilTwo;

    @Mock
    private ConstructResponsesUseCase constructReplyGenAndDoc;
    @Mock
    private BasicInformationUseCase basicInformationUseCase;
    @Mock
    private PersonalInformationRepository personalInformationRepository;
    @Mock
    private GenAndExpDocUseCaseUtilThree genAndExpDocUseCaseUtilThree;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenExposeTwoReply() {
        GenExposeReply genExposeReply = GenExposeReply.builder().build();

        Mockito.doReturn(Optional.of(BasicInformation.builder().build())).when(basicInformationUseCase).findByAcquisition(any(Acquisition.class));
        Mockito.doReturn(BasicInfoReply.builder().build()).when(constructReplyGenAndDoc).fromOriginal(any(BasicInformation.class));

        Mockito.doReturn(PersonalInformation.builder().build()).when(personalInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doReturn(PersonalInfoReply.builder().build()).when(constructReplyGenAndDoc).fromOriginal(any(PersonalInformation.class));

        Mockito.doReturn(GenExposeReply.builder().build()).when(genAndExpDocUseCaseUtilThree).genExposeThreeReply(any(Acquisition.class), any(GenExposeReply.class));

        genAndExpDocUseCaseUtilTwo.genExposeTwoReply(Acquisition.builder().build(), genExposeReply);
        verify(this.genAndExpDocUseCaseUtilTwo, times(1)).genExposeTwoReply(Acquisition.builder().build(), genExposeReply);
    }

    @Test
    public void testFindAcquisitionBasicInfo() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        Mockito.doReturn(Optional.empty()).when(basicInformationUseCase).findByAcquisition(any(Acquisition.class));
        genAndExpDocUseCaseUtilTwo.findAcquisitionBasicInfo(acquisition);
        verify(this.genAndExpDocUseCaseUtilTwo, times(1)).findAcquisitionBasicInfo(acquisition);
    }
}