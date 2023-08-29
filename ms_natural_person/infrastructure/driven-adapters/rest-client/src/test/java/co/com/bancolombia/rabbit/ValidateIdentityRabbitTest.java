package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class ValidateIdentityRabbitTest {

    @InjectMocks
    @Spy
    private ValidateIdentityRabbit validateIdentityRabbit;

    @Mock
    private PersonalInformationRepository personalInformationRepository;

    @Mock
    private ContactInformationRepository contactInformationRepository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void validateIdentitySuccessTest() {
        PersonalInformation personalInformation = PersonalInformation.builder().build();
        ContactInformation contactInformation = ContactInformation.builder().build();
        doReturn(personalInformation).when(personalInformationRepository).findByAcquisition(any(Acquisition.class));
        doReturn(contactInformation).when(contactInformationRepository).findByAcquisitionAndAddressType(
                any(Acquisition.class), anyString());
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        ValidateIdentityReply reply = validateIdentityRabbit.validateIdentity(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void validateIdentitySuccessNullTest() {
        doReturn(null).when(personalInformationRepository).findByAcquisition(any(Acquisition.class));
        doReturn(null).when(contactInformationRepository).findByAcquisitionAndAddressType(
                any(Acquisition.class), anyString());
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        ValidateIdentityReply reply = validateIdentityRabbit.validateIdentity(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void validateIdentityExceptionTest() {
        doThrow(CustomException.class).when(personalInformationRepository).findByAcquisition(any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        ValidateIdentityReply reply = validateIdentityRabbit.validateIdentity(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentityNullQueryTest() {
        ValidateIdentityReply reply = validateIdentityRabbit.validateIdentity(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentityNullDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().build();
        ValidateIdentityReply reply = validateIdentityRabbit.validateIdentity(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentityEmptyDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("").build();
        ValidateIdentityReply reply = validateIdentityRabbit.validateIdentity(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentityInvalidUUIDTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("12345").build();
        ValidateIdentityReply reply = validateIdentityRabbit.validateIdentity(query);
        assertFalse(reply.isValid());
    }

}
