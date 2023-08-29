package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.SavePersonalInfoQuery;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class PersonalRabbitTest {

    @InjectMocks
    @Spy
    private PersonalRabbit personalRabbit;

    @Mock
    private PersonalInformationUseCase personalInformationUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void saveResultUpdateOk(){
        SavePersonalInfoQuery query = SavePersonalInfoQuery.builder().acquisitionId(uuid).usrMod("USER").build();
        Optional<PersonalInformation>    pInformationUpdate = Optional.ofNullable(PersonalInformation.builder().build());
        PersonalInformation personalInformation = PersonalInformation.builder().build();

        doReturn(pInformationUpdate).when(personalInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(personalInformation).when(personalInformationUseCase).save(any(PersonalInformation.class));

        EmptyReply reply = personalRabbit.saveResult(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void saveResultNewOk(){
        SavePersonalInfoQuery query = SavePersonalInfoQuery.builder().acquisitionId(uuid).usrMod("USER").build();
        Optional<PersonalInformation>    pInformationUpdate = Optional.empty();
        PersonalInformation personalInformation = PersonalInformation.builder().build();

        doReturn(pInformationUpdate).when(personalInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(personalInformation).when(personalInformationUseCase).save(any(PersonalInformation.class));

        EmptyReply reply = personalRabbit.saveResult(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void saveResultError(){
        SavePersonalInfoQuery query = SavePersonalInfoQuery.builder().acquisitionId(uuid).usrMod("USER").build();
        Optional<PersonalInformation>    pInformationUpdate = Optional.empty();

        doReturn(pInformationUpdate).when(personalInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(null).when(personalInformationUseCase).save(any(PersonalInformation.class));

        EmptyReply reply = personalRabbit.saveResult(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void saveResultNoMandatoryFields(){
        SavePersonalInfoQuery query = SavePersonalInfoQuery.builder().build();
        Optional<PersonalInformation>    pInformationUpdate = Optional.empty();

        doReturn(pInformationUpdate).when(personalInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(null).when(personalInformationUseCase).save(any(PersonalInformation.class));

        EmptyReply reply = personalRabbit.saveResult(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void saveResultEmptyQuery(){
        Optional<PersonalInformation>    pInformationUpdate = Optional.empty();

        doReturn(pInformationUpdate).when(personalInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(null).when(personalInformationUseCase).save(any(PersonalInformation.class));

        EmptyReply reply = personalRabbit.saveResult(null);
        assertFalse(reply.isValid());
    }
}
