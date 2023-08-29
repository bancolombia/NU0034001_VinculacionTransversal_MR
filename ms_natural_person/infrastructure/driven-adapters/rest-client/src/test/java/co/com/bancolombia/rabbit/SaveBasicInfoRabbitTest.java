package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.SaveBasicInfoQuery;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SaveBasicInfoRabbitTest {

    @InjectMocks
    @Spy
    private SaveBasicInfoRabbit saveBasicInfoRabbit;

    @Mock
    BasicInformationUseCase basicInformationUseCase;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveResult() {
        BasicInformation basicInformation = BasicInformation.builder().build();
        doReturn(Optional.of(basicInformation)).when(basicInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doReturn(basicInformation).when(basicInformationUseCase).save(any(BasicInformation.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        SaveBasicInfoQuery query = SaveBasicInfoQuery.builder()
                .acquisitionId(UUID.randomUUID().toString()).gender("asd").usrMod("asd").build();
        EmptyReply reply = saveBasicInfoRabbit.saveResult(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void saveResult2() {
        BasicInformation basicInformation = BasicInformation.builder().build();
        doReturn(Optional.empty()).when(basicInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doReturn(null).when(basicInformationUseCase).save(any(BasicInformation.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        SaveBasicInfoQuery query = SaveBasicInfoQuery.builder()
                .acquisitionId(UUID.randomUUID().toString()).gender("asd").usrMod("asd").build();
        EmptyReply reply = saveBasicInfoRabbit.saveResult(query);
        assertFalse(reply.isValid());
    }
}