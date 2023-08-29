package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.BlockCustomerQuery;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.customercontrol.CustomerControlUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@RequiredArgsConstructor
public class CustomerControlRabbitTest {

    @InjectMocks
    @Spy
    private CustomerControlRabbit customerControlRabbit;

    @Mock
    private CustomerControlUseCase customerControlUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void blockCustomerSuccessTest() {
        doNothing().when(customerControlUseCase).blockCustomer(anyString(), anyString(), any(Date.class), anyString());

        BlockCustomerQuery query = BlockCustomerQuery.builder()
                .documentNumber("123").documentType("FS001")
                .unlockDate(new Date()).operation(Constants.CODE_GEN_EXP_DOCS).build();

        EmptyReply reply = customerControlRabbit.blockCustomer(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void blockCustomerNullQueryTest() {
        EmptyReply reply = customerControlRabbit.blockCustomer(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void blockCustomerNullDataTest() {
        BlockCustomerQuery query = BlockCustomerQuery.builder().build();
        EmptyReply reply = customerControlRabbit.blockCustomer(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void blockCustomerEmptyDataTest() {
        BlockCustomerQuery query = BlockCustomerQuery.builder()
                .documentNumber("").documentType("").unlockDate(new Date()).operation("").build();

        EmptyReply reply = customerControlRabbit.blockCustomer(query);
        assertFalse(reply.isValid());
    }
}
