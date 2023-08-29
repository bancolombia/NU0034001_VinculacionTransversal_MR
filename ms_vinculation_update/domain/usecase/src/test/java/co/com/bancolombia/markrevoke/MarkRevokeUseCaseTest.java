package co.com.bancolombia.markrevoke;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.containeraction.ContainerAction;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.model.markrevoke.MarkRevoke;
import co.com.bancolombia.model.markrevoke.gateways.MarkRevokeRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class MarkRevokeUseCaseTest {

    @InjectMocks
    @Spy
    private MarkRevokeUseCaseImpl markRevokeUseCase;

    @Mock
    private MarkRevokeRepository markRevokeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveTest() {
        Mockito.doReturn(MarkRevoke.builder().build()).when(markRevokeRepository).save(any(MarkRevoke.class));
        MarkRevoke markRevoke = markRevokeUseCase.save(MarkRevoke.builder().build());
        assertNotNull(markRevoke);
    }

    @Test
    public void checkOperationTest() {
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String userTransaction = "AC001";
        Mockito.doReturn(null).when(this.markRevokeRepository).findByAcquisition(any(Acquisition.class));
        list = markRevokeUseCase.checkOperation(list,acquisition,userTransaction);
    }

    @Test
    public void checkOperationNotNullTest() {
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String userTransaction = "AC001";
        MarkRevoke revoke =  MarkRevoke.builder().id(UUID.randomUUID()).createdBy("Prueba").build();
        Mockito.doReturn(revoke).when(this.markRevokeRepository).findByAcquisition(any(Acquisition.class));
        list = markRevokeUseCase.checkOperation(list,acquisition,userTransaction);
    }

    @Test
    public void checkOperationTest1() {
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String userTransaction = "Prueba";
        MarkRevoke revoke = MarkRevoke.builder().id(UUID.randomUUID()).createdBy("Prueba").build();
        Mockito.doReturn(revoke).when(this.markRevokeRepository).findByAcquisition(any(Acquisition.class));
        list.add(ClauseAcquisitionCheckList.builder().id(UUID.randomUUID()).matrixTypeAcquisitionClause(
            MatrixTypeAcquisitionClause.builder().action(
                    ContainerAction.builder().code("AC001").build()).build()).build());

        list = markRevokeUseCase.checkOperation(list,acquisition,userTransaction);
    }
    @Test
    public void checkOperationTest2() {
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String userTransaction = "Prueba";
        MarkRevoke revoke = MarkRevoke.builder().id(UUID.randomUUID()).createdBy("Prueba").build();
        Mockito.doReturn(revoke).when(this.markRevokeRepository).findByAcquisition(any(Acquisition.class));
        list.add(ClauseAcquisitionCheckList.builder().id(UUID.randomUUID()).matrixTypeAcquisitionClause(
            MatrixTypeAcquisitionClause.builder().action(ContainerAction.builder().code("AC002").build()).build()).build());


        list = markRevokeUseCase.checkOperation(list,acquisition,userTransaction);

    }
    @Test
    public void checkOperationTest3() {
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String userTransaction = "Prueba";
        MarkRevoke revoke = MarkRevoke.builder().id(UUID.randomUUID()).createdBy("Prueba").build();
        Mockito.doReturn(revoke).when(this.markRevokeRepository).findByAcquisition(any(Acquisition.class));
        list.add(ClauseAcquisitionCheckList.builder().id(UUID.randomUUID()).matrixTypeAcquisitionClause(
            MatrixTypeAcquisitionClause.builder().action(
                    ContainerAction.builder().code("AC003").build()).build()).build());

        list = markRevokeUseCase.checkOperation(list,acquisition,userTransaction);
    }
    @Test
    public void checkOperationTest4() {
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String userTransaction = "Prueba";
        MarkRevoke revoke = MarkRevoke.builder().id(UUID.randomUUID()).createdBy("Prueba").build();
        Mockito.doReturn(revoke).when(this.markRevokeRepository).findByAcquisition(any(Acquisition.class));
        list.add(ClauseAcquisitionCheckList.builder().id(UUID.randomUUID()).matrixTypeAcquisitionClause(
            MatrixTypeAcquisitionClause.builder().action(
                    ContainerAction.builder().code("AC004").build()).build()).build());

        list = markRevokeUseCase.checkOperation(list,acquisition,userTransaction);
    }

    @Test
    public void checkOperationTest5() {
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String userTransaction = "Prueba";
        MarkRevoke revoke = MarkRevoke.builder().id(UUID.randomUUID()).createdBy("Prueba").build();
        Mockito.doReturn(revoke).when(markRevokeRepository).findByAcquisition(any(Acquisition.class));
        list.add(ClauseAcquisitionCheckList.builder().id(UUID.randomUUID()).matrixTypeAcquisitionClause(
            MatrixTypeAcquisitionClause.builder().action(
                    ContainerAction.builder().code("AC005").build()).build()).build());

        list = markRevokeUseCase.checkOperation(list,acquisition,userTransaction);
    }
}
