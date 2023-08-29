package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.model.clause.Clause;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ClauseQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ClauseReply;
import co.com.bancolombia.model.clause.gateways.ClauseRepository;
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
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ClauseRabbitTest {
    @InjectMocks
    @Spy
    private ClauseRabbit clauseRabbit;

    @Mock
    private ClauseRepository clauseRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getClauseSuccessTest() {
        Clause clause = Clause.builder().name("dgfdfg").createdDate(new Date()).build();
        doReturn(Optional.of(clause)).when(clauseRepository).findByCode(any(String.class));
        ClauseQuery query = ClauseQuery.builder().clauseCode("CLAUSE001").build();
        ClauseReply reply = clauseRabbit.getClause(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getClauseNullQueryTest() {
        ClauseReply reply = clauseRabbit.getClause(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void getClauseEmptyTest() {
        Clause clause = Clause.builder().name("dgfdfg").createdDate(new Date()).build();
        doReturn(Optional.of(clause)).when(clauseRepository).findByCode(any(String.class));
        ClauseQuery query = ClauseQuery.builder().clauseCode("").build();
        ClauseReply reply = clauseRabbit.getClause(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getClauseNullDataTest() {
        Clause clause = Clause.builder().name("dgfdfg").createdDate(new Date()).build();
        doReturn(Optional.of(clause)).when(clauseRepository).findByCode(any(String.class));
        ClauseQuery query = ClauseQuery.builder().build();
        ClauseReply reply = clauseRabbit.getClause(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getClauseNullClauseTest() {
        doReturn(Optional.empty()).when(clauseRepository).findByCode(any(String.class));
        ClauseQuery query = ClauseQuery.builder().clauseCode("1234").build();
        ClauseReply reply = clauseRabbit.getClause(query);
        assertFalse(reply.isValid());
    }

}
