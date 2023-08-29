package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFNormalQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFSearchQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.model.dependentfield.gateways.DependentFieldRepository;
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
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_ACQUISITION;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class DependentFieldRabbitTest {

    @InjectMocks
    @Spy
    private DependentFieldRabbit dependentFieldRabbit;

    @Mock
    private DependentFieldRepository dependentFieldRepository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void dependentFieldNormalTest() {
        doReturn(new ArrayList<>()).when(dependentFieldRepository)
                .findByTypeAcquisitionAndCurrentOperationAndCurrentFieldInAndActive(
                        anyString(), anyString(), any(List.class), anyBoolean());

        DependentFNormalQuery query = DependentFNormalQuery.builder()
                .typeAcquisition("VT001").currentOperation(CODE_START_ACQUISITION)
                .currentFields(Collections.singletonList("")).active(true).build();

        DependentFieldReply reply = dependentFieldRabbit.dependentFieldNormal(query);
        assertNotNull(reply.getDependentFields());
    }

    @Test
    public void dependentFieldSearchTest() {
        doReturn(new ArrayList<>()).when(dependentFieldRepository)
                .searchDependentValueFieldAndActive(
                        any(UUID.class), anyString(), anyString(), anyString(), anyBoolean());

        DependentFSearchQuery query = DependentFSearchQuery.builder()
                .idAcq(uuid).table("TABLE").searchField("FIELD").searchValue("VALUE").active(true).build();

        DependentFieldReply reply = dependentFieldRabbit.dependentFieldSearch(query);
        assertNotNull(reply.getObjectList());
    }
}
