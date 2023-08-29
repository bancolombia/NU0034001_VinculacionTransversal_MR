package co.com.bancolombia.restclient.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.query.ControlListSaveQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.ControlListSaveReply;
import co.com.bancolombia.controllist.ControlListUseCase;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class ControlListSaveRabbitTest {
    @InjectMocks
    @Spy
    private ControlListSaveRabbit controlListSaveRabbit;

    @Mock
    private ControlListUseCase controlListUseCase;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void findStateValidationCustomerControlListSuccessTest() {
        ControlListSaveQuery query =
                ControlListSaveQuery.builder()
                        .acquisitionId(uuid.toString())
                        .documentType("CC")
                        .documentNumber("0123456")
                        .build();
        doReturn("6").when(controlListUseCase).findStateValidationCustomerControlList(anyString(), anyString(), anyString());
        ControlListSaveReply reply = controlListSaveRabbit.findStateValidationCustomerControlList(query);

        assertTrue(reply.isValid());
    }

    @Test
    public void findStateValidationCustomerControlListFalseTest() {
        ControlListSaveQuery query =
                ControlListSaveQuery.builder()
                        .acquisitionId(null)
                        .documentType(null)
                        .documentNumber(null)
                        .build();
        ControlListSaveReply reply = controlListSaveRabbit.findStateValidationCustomerControlList(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void findStateValidationCustomerControlListNullTest() {
        ControlListSaveReply reply = controlListSaveRabbit.findStateValidationCustomerControlList(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void findStateValidationCustomerControlListCustomExceptionTest() {
        doThrow(CustomException.class).when(controlListUseCase).findStateValidationCustomerControlList(anyString(), anyString(), anyString());
        ControlListSaveReply reply = controlListSaveRabbit.findStateValidationCustomerControlList(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void findStateValidationCustomerControlListValidationExceptionTest() {
        doThrow(ValidationException.class).when(controlListUseCase).findStateValidationCustomerControlList(anyString(), anyString(), anyString());
        ControlListSaveReply reply = controlListSaveRabbit.findStateValidationCustomerControlList(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void findStateValidationCustomerControlListErrorAcquisitionIdFormatTest() {
        ControlListSaveQuery query =
                ControlListSaveQuery.builder()
                        .acquisitionId("99999999999")
                        .documentType("CC")
                        .documentNumber("0123456")
                        .build();

        doThrow(CustomException.class).when(controlListUseCase).findStateValidationCustomerControlList(anyString(), anyString(), anyString());
        ControlListSaveReply reply = controlListSaveRabbit.findStateValidationCustomerControlList(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void findStateValidationCustomerControlListNullStateTest() {
        ControlListSaveQuery query =
                ControlListSaveQuery.builder()
                        .acquisitionId(uuid.toString())
                        .documentType("CC")
                        .documentNumber("0123456")
                        .build();
        doReturn(null).when(controlListUseCase).findStateValidationCustomerControlList(anyString(), anyString(), anyString());
        ControlListSaveReply reply = controlListSaveRabbit.findStateValidationCustomerControlList(query);

        assertFalse(reply.isValid());
    }
}