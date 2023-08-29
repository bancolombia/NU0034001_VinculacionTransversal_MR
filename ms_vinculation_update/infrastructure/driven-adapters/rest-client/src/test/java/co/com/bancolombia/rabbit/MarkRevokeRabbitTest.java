package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.MarkRevokeReply;
import co.com.bancolombia.model.markrevoke.MarkRevoke;
import co.com.bancolombia.model.markrevoke.gateways.MarkRevokeRepository;
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
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class MarkRevokeRabbitTest {

    @InjectMocks
    @Spy
    private MarkRevokeRabbit markRevokeRabbit;

    @Mock
    private MarkRevokeRepository markRevokeRepository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void getMarkRevokeSuccessTest() {
        MarkRevoke markRevoke = MarkRevoke.builder().build();
        doReturn(markRevoke).when(markRevokeRepository).findByAcquisition(any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        MarkRevokeReply reply = markRevokeRabbit.getMarkRevoke(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getMarkRevokeNullMarkTest() {
        doReturn(null).when(markRevokeRepository).findByAcquisition(any(Acquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        MarkRevokeReply reply = markRevokeRabbit.getMarkRevoke(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getMarkRevokeNullQueryTest() {
        MarkRevokeReply reply = markRevokeRabbit.getMarkRevoke(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void getMarkRevokeNullDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().build();
        MarkRevokeReply reply = markRevokeRabbit.getMarkRevoke(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getMarkRevokeEmptyDataTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("").build();
        MarkRevokeReply reply = markRevokeRabbit.getMarkRevoke(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAndValidateAcquisitionInvalidUUIDTest() {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("12345").build();
        MarkRevokeReply reply = markRevokeRabbit.getMarkRevoke(query);
        assertFalse(reply.isValid());
    }
}
