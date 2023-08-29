package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.SegmentCustomerReply;
import co.com.bancolombia.usecase.rabbit.segmentcustomer.SegmentCustomerInitialUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_SEGMENT_CUSTOMER;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SegmentCustomerRabbitTest {

    @InjectMocks
    @Spy
    private GlobalServicesRabbit segmentCustomerRabbit;

    @Mock
    SegmentCustomerInitialUseCase segmentCustomerUseCase;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void servicesReply() {
        SegmentCustomerReply segmentCustomerReply = SegmentCustomerReply.builder().build();
        doReturn(segmentCustomerReply).when(segmentCustomerUseCase)
                .segmentCustomerReply(any(NpGlobalServicesQuery.class));
        NpGlobalServicesQuery query = NpGlobalServicesQuery.builder().operation(CODE_SEGMENT_CUSTOMER).build();
        Object o = segmentCustomerRabbit.servicesReply(query);
        assertNotNull(o);
    }
}