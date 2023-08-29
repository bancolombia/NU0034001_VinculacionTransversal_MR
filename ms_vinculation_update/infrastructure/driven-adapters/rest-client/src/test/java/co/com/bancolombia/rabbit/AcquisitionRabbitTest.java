package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateCountQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateCountReply;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_STATUS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class AcquisitionRabbitTest {

    @InjectMocks
    @Spy
    private AcquisitionRabbit acquisitionRabbit;

    @Mock
    private AcquisitionValidateUseCase acquisitionValidateUseCase;

    @Mock
    private AcquisitionOperationUseCase acquisitionOperationUseCase;

    @Mock
    private AcquisitionUseCase acquisitionUseCase;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void getAndValidateAcquisitionSuccessTest() {
        DocumentType documentType = DocumentType.builder().code("FS001").codeHomologation("CC")
                .codeOrderControlList("1").codeOrderExperian("1").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").name("Activo").build();
        Acquisition acquisition = Acquisition.builder()
                .id(uuid).documentType(documentType).stateAcquisition(stateAcquisition)
                .typeAcquisition(TypeAcquisition.builder().code("VTN001").build()).build();

        doReturn(Optional.of(acquisition)).when(acquisitionValidateUseCase).validateInfoSearchAndGet(
                anyString(), anyString(), anyString(), anyString());

        AcquisitionQuery query = AcquisitionQuery.builder()
                .acquisitionId(uuid.toString())
                .documentNumber("123").documentType("FS001").operation(CODE_VALIDATE_STATUS).build();

        AcquisitionReply reply = acquisitionRabbit.getAndValidateAcquisition(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getAndValidateAcquisitionEmptyAcquisitionTest() {
        doReturn(Optional.empty()).when(acquisitionValidateUseCase).validateInfoSearchAndGet(
                anyString(), anyString(), anyString(), anyString());

        AcquisitionQuery query = AcquisitionQuery.builder()
                .acquisitionId(uuid.toString())
                .documentNumber("123").documentType("FS001").operation(CODE_VALIDATE_STATUS).build();

        AcquisitionReply reply = acquisitionRabbit.getAndValidateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAndValidateAcquisitionCustomExceptionTest() {
        doThrow(CustomException.class).when(acquisitionValidateUseCase).validateInfoSearchAndGet(
                anyString(), anyString(), anyString(), anyString());

        AcquisitionQuery query = AcquisitionQuery.builder()
                .acquisitionId(uuid.toString())
                .documentNumber("123").documentType("FS001").operation(CODE_VALIDATE_STATUS).build();

        AcquisitionReply reply = acquisitionRabbit.getAndValidateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAndValidateAcquisitionNullQueryTest() {
        AcquisitionReply reply = acquisitionRabbit.getAndValidateAcquisition(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAndValidateAcquisitionNullDataTest() {
        AcquisitionQuery query = AcquisitionQuery.builder().build();
        AcquisitionReply reply = acquisitionRabbit.getAndValidateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAndValidateAcquisitionEmptyDataTest() {
        AcquisitionQuery query = AcquisitionQuery.builder()
                .acquisitionId("").documentNumber("").documentType("").operation("").build();

        AcquisitionReply reply = acquisitionRabbit.getAndValidateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAndValidateAcquisitionInvalidUUIDTest() {
        AcquisitionQuery query = AcquisitionQuery.builder()
                .acquisitionId("12345")
                .documentNumber("123").documentType("FS001").operation(CODE_VALIDATE_STATUS).build();

        AcquisitionReply reply = acquisitionRabbit.getAndValidateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateStateAcquisitionSuccessTest() {
        Acquisition acquisition = Acquisition.builder().build();

        doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));
        doNothing().when(acquisitionOperationUseCase).updateAcquisition(any(Acquisition.class), anyString());

        AcquisitionUpdateQuery query = AcquisitionUpdateQuery.builder()
                .acquisitionId(uuid.toString()).stateCode("1").build();

        EmptyReply reply = acquisitionRabbit.updateStateAcquisition(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void updateStateAcquisitionNullAcquisitionTest() {
        doReturn(null).when(acquisitionUseCase).findById(any(UUID.class));

        AcquisitionUpdateQuery query = AcquisitionUpdateQuery.builder()
                .acquisitionId(uuid.toString()).stateCode("1").build();

        EmptyReply reply = acquisitionRabbit.updateStateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateStateAcquisitionCustomExceptionTest() {
        doThrow(CustomException.class).when(acquisitionUseCase).findById(any(UUID.class));

        AcquisitionUpdateQuery query = AcquisitionUpdateQuery.builder()
                .acquisitionId(uuid.toString()).stateCode("1").build();

        EmptyReply reply = acquisitionRabbit.updateStateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateStateAcquisitionNullQueryTest() {
        EmptyReply reply = acquisitionRabbit.updateStateAcquisition(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateStateAcquisitionNullDataTest() {
        AcquisitionUpdateQuery query = AcquisitionUpdateQuery.builder().build();
        EmptyReply reply = acquisitionRabbit.updateStateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateStateAcquisitionEmptyDataTest() {
        AcquisitionUpdateQuery query = AcquisitionUpdateQuery.builder()
                .acquisitionId("").stateCode("").build();

        EmptyReply reply = acquisitionRabbit.updateStateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateStateAcquisitionInvalidUUIDTest() {
        AcquisitionUpdateQuery query = AcquisitionUpdateQuery.builder()
                .acquisitionId("12345").stateCode("1").build();

        EmptyReply reply = acquisitionRabbit.updateStateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateAcquisitionSuccessTest() {
        Acquisition acquisition = Acquisition.builder().build();
        doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));
        doReturn(acquisition).when(acquisitionOperationUseCase).save(any(Acquisition.class));
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        EmptyReply reply = acquisitionRabbit.updateAcquisition(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void updateAcquisitionNullAcquisitionTest() {
        doReturn(null).when(acquisitionUseCase).findById(any(UUID.class));
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        EmptyReply reply = acquisitionRabbit.updateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateAcquisitionCustomExceptionTest() {
        doThrow(CustomException.class).when(acquisitionUseCase).findById(any(UUID.class));
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        EmptyReply reply = acquisitionRabbit.updateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateAcquisitionNullQueryTest() {
        EmptyReply reply = acquisitionRabbit.updateAcquisition(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateAcquisitionNullDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().build();
        EmptyReply reply = acquisitionRabbit.updateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateAcquisitionEmptyDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("").build();
        EmptyReply reply = acquisitionRabbit.updateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateAcquisitionInvalidUUIDTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("12345").build();
        EmptyReply reply = acquisitionRabbit.updateAcquisition(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void countAcquisitionByStateSuccessTest(){
        List<UUID> acquisitionIdList = new ArrayList<>();
        acquisitionIdList.add(uuid);

        AcquisitionStateCountQuery query = AcquisitionStateCountQuery.builder()
                .acquisitionId(acquisitionIdList)
                .state("1")
                .build();

        AcquisitionStateCountReply reply = acquisitionRabbit.countAcquisitionByState(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void countAcquisitionByStateException(){
        AcquisitionStateCountQuery query = AcquisitionStateCountQuery.builder().build();

        AcquisitionStateCountReply reply = acquisitionRabbit.countAcquisitionByState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void countAcquisitionByStateNull(){
        AcquisitionStateCountQuery query = null;
        AcquisitionStateCountReply reply = acquisitionRabbit.countAcquisitionByState(query);
        assertFalse(reply.isValid());
    }

}
