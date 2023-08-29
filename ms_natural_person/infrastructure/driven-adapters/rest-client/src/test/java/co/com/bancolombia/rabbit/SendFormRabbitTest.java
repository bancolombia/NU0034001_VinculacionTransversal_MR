package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SendFormReply;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCasePersist;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SendFormRabbitTest {

    @InjectMocks
    @Spy
    private SendFormRabbit sendFormRabbit;

    @Mock
    private PersonalInformationUseCase personalInformationUseCase;

    @Mock
    private ContactInformationUseCasePersist contactInformationUseCasePersist;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void sendFormSuccessTest() {
        Optional<PersonalInformation> personalInformation = Optional.ofNullable(PersonalInformation.builder().build());
        doReturn(personalInformation).when(personalInformationUseCase).findByAcquisition(any(Acquisition.class));

        List<ContactInformation> contactInformation = new ArrayList<>();
        contactInformation.add(ContactInformation.builder().build());
        doReturn(contactInformation).when(contactInformationUseCasePersist).findAllByAcquisition(any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        SendFormReply reply = sendFormRabbit.sendFormReply(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void sendFormNullPersonalTest() {
        doReturn(Optional.empty()).when(personalInformationUseCase).findByAcquisition(any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        SendFormReply reply = sendFormRabbit.sendFormReply(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void sendFormEmptyContactTest() {
        Optional<PersonalInformation> personalInformation = Optional.ofNullable(PersonalInformation.builder().build());
        doReturn(personalInformation).when(personalInformationUseCase).findByAcquisition(any(Acquisition.class));

        doReturn(Collections.emptyList()).when(contactInformationUseCasePersist).findAllByAcquisition(
                any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        SendFormReply reply = sendFormRabbit.sendFormReply(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void sendFormNullQueryTest() {
        SendFormReply reply = sendFormRabbit.sendFormReply(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void sendFormNullDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().build();
        SendFormReply reply = sendFormRabbit.sendFormReply(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void sendFormEmptyDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("").build();
        SendFormReply reply = sendFormRabbit.sendFormReply(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void sendFormInvalidUUIDTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("12345").build();
        SendFormReply reply = sendFormRabbit.sendFormReply(query);
        assertFalse(reply.isValid());
    }
}

