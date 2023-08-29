package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.UpdateCiiuQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.usecase.economicinformation.EconomicInformationUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.YES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class EconomicRabbitTest {

    @InjectMocks
    @Spy
    private EconomicRabbit economicRabbit;

    @Mock
    private EconomicInformationRepository economicInformationRepository;

    @Mock
    private EconomicInformationUseCase economicInformationUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void getRequiredRutSuccessTest() {
        EconomicInformation economic = EconomicInformation.builder().requiredRut(YES).ciiu("CIIU_00000").build();
        doReturn(economic).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        InfoRutReply reply = economicRabbit.getRequiredRut(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getRequiredRutNullEconomicTest() {
        doReturn(null).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        InfoRutReply reply = economicRabbit.getRequiredRut(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getRequiredRutCustomExceptionTest() {
        doThrow(CustomException.class).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        InfoRutReply reply = economicRabbit.getRequiredRut(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getRequiredRutNullQueryTest() {
        InfoRutReply reply = economicRabbit.getRequiredRut(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void getRequiredRutNullDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().build();
        InfoRutReply reply = economicRabbit.getRequiredRut(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getRequiredRutEmptyDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("").build();
        InfoRutReply reply = economicRabbit.getRequiredRut(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getRequiredRutInvalidUUIDTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("12345").build();
        InfoRutReply reply = economicRabbit.getRequiredRut(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateCiiuSuccessSameCiiuTest() {
        EconomicInformation economicFind = EconomicInformation.builder().ciiu("CIIU_00010").build();
        EconomicInformation economicSave = EconomicInformation.builder().build();

        doReturn(Optional.of(economicFind)).when(economicInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(economicSave).when(economicInformationUseCase).save(any(EconomicInformation.class));

        UpdateCiiuQuery query = UpdateCiiuQuery.builder()
                .acquisitionId(uuid.toString()).ciiu("CIIU_00010").usrMod("USUARIO").build();

        EmptyReply reply = economicRabbit.updateCiiu(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void updateCiiuSuccessDifferentCiiuTest() {
        EconomicInformation economicFind = EconomicInformation.builder().ciiu("CIIU_00010").build();
        EconomicInformation economicSave = EconomicInformation.builder().build();

        doReturn(Optional.of(economicFind)).when(economicInformationUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(economicSave).when(economicInformationUseCase).save(any(EconomicInformation.class));

        UpdateCiiuQuery query = UpdateCiiuQuery.builder()
                .acquisitionId(uuid.toString()).ciiu("CIIU_00090").usrMod("USUARIO").build();

        EmptyReply reply = economicRabbit.updateCiiu(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void updateCiiuNotFoundEconomicTest() {
        doReturn(Optional.empty()).when(economicInformationUseCase).findByAcquisition(any(Acquisition.class));

        UpdateCiiuQuery query = UpdateCiiuQuery.builder()
                .acquisitionId(uuid.toString()).ciiu("CIIU_00010").usrMod("USUARIO").build();

        EmptyReply reply = economicRabbit.updateCiiu(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateCiiuNullQueryTest() {
        EmptyReply reply = economicRabbit.updateCiiu(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateCiiuNullDataTest() {
        UpdateCiiuQuery query = UpdateCiiuQuery.builder().build();
        EmptyReply reply = economicRabbit.updateCiiu(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateCiiuEmptyDataTest() {
        UpdateCiiuQuery query = UpdateCiiuQuery.builder().acquisitionId("").ciiu("").usrMod("").build();
        EmptyReply reply = economicRabbit.updateCiiu(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateCiiuInvalidUUIDTest() {
        UpdateCiiuQuery query = UpdateCiiuQuery.builder()
                .acquisitionId("12345").ciiu("CIIU_00090").usrMod("USUARIO").build();

        EmptyReply reply = economicRabbit.updateCiiu(query);
        assertFalse(reply.isValid());
    }
}
