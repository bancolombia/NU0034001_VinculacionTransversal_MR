package co.com.bancolombia.clauseacquisitionchecklist;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clause.Clause;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.markrevoke.MarkRevokeUseCase;
import co.com.bancolombia.matrixtypeacquisitionclause.MatrixTypeAcquisitionClauseUseCase;
import co.com.bancolombia.model.acquisition.BasicAcquisitionRequest;
import co.com.bancolombia.model.clauseacquisitionchecklist.gateways.ClauseAcquisitionCheckListRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITHOUT_LINK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ClauseAcquisitionChecklistUseCaseTest {

    @InjectMocks
    @Spy
    private ClauseAcquisitionChecklistUseCaseImp useCase;

    @Mock
    private ClauseAcquisitionCheckListRepository repository;

    @Mock
    private MatrixTypeAcquisitionClauseUseCase clauseUseCase;

    @Mock
    private AcquisitionValidateUseCase acquisitionValidateUseCase;

    @Mock
    private AcquisitionOperationUseCase acquisitionOperationUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private CheckListUseCase checkListUseCase;

    @Mock
    private MarkRevokeUseCase markRevokeUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveAllTest() {
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        list.add(ClauseAcquisitionCheckList.builder().id(any(UUID.class)).build());
        useCase.saveAll(list);
        verify(repository, times(1)).saveAll(any(List.class));
    }

    @Test
    public void findByAcquisitionTest() {
        Acquisition acquisition = Acquisition.builder().build();
        List<ClauseAcquisitionCheckList> checkLists = useCase.findByAcquisition(acquisition);
        assertNotNull(checkLists);
    }

    @Test(expected = ValidationException.class)
    public void validateAcquisitionTestFailed() {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        BasicAcquisitionRequest request = BasicAcquisitionRequest.builder().build();
        Acquisition acqOpt = Acquisition.builder().build();
        error.put(ERROR_CODE_WITHOUT_LINK, Collections.singletonList(ErrorField.builder().build()));
        Mockito.doReturn(Optional.of(acqOpt)).when(acquisitionValidateUseCase).validateInfoSearchAndGet(
                any(String.class), any(String.class), any(String.class), anyString());
        Acquisition res = useCase.validateAcquisition(request, "");
        assertNotNull(res);
    }

    @Test
    public void validateAcquisitionTest() {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        BasicAcquisitionRequest request =
                BasicAcquisitionRequest.builder().idAcq("FS001").documentType("FS001").documentNumber("123456").build();
        Acquisition acqOpt = Acquisition.builder().build();
        error.put(ERROR_CODE_WITHOUT_LINK, Collections.singletonList(ErrorField.builder().build()));
        Mockito.doReturn(Optional.of(acqOpt)).when(acquisitionValidateUseCase).validateInfoSearchAndGet(
                request.getIdAcq(), request.getDocumentType(), request.getDocumentNumber(), "");
        Acquisition res = useCase.validateAcquisition(request, "");
        assertNotNull(res);
    }

    @Test
    public void startCheckListTest() {
        TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        Acquisition acquisition = Acquisition.builder().build();
        MatrixTypeAcquisitionClause clause = MatrixTypeAcquisitionClause.builder()
                .typeAcquisition(typeAcquisition).build();
        List<MatrixTypeAcquisitionClause> list = Collections
                .singletonList(MatrixTypeAcquisitionClause.builder().build());
        List<ClauseAcquisitionCheckList> checkLists = new ArrayList<>();

        Mockito.doReturn(list).when(clauseUseCase).findByTypeAcquisitionAndActive(clause);
        useCase.startCheckList(typeAcquisition, acquisition);
        Mockito.doNothing().when(useCase).saveAll(checkLists);

        verify(useCase, times(1)).saveAll(any(List.class));
    }

    @Test
    public void markOperationClauseCompleteTest() {
        Acquisition acq = Acquisition.builder().build();
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        list.add(ClauseAcquisitionCheckList.builder().id(UUID.randomUUID()).dateAcceptClause(new Date()).build());
        Mockito.doReturn(list).when(useCase).findByAcquisition(any(Acquisition.class));
        Mockito.doNothing().when(checkListUseCase).markOperation(any(UUID.class), any(String.class), anyString());

        useCase.markOperationClause(acq);
        verify(useCase, times(1)).markOperationClause(acq);
    }

    @Test
    public void markOperationClausePartialCompletedTest() {
        Acquisition acq = Acquisition.builder().build();
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        list.add(ClauseAcquisitionCheckList.builder().id(UUID.randomUUID()).dateAcceptClause(null).build());
        Mockito.doReturn(list).when(useCase).findByAcquisition(any(Acquisition.class));
        Mockito.doNothing().when(checkListUseCase).markOperation(any(UUID.class), any(String.class), anyString());

        useCase.markOperationClause(acq);
        Mockito.verify(useCase, times(1)).markOperationClause(acq);
    }

    @Test
    public void validatePreviousAcceptClauseTest() {
        String clauseCode = "CLAUSE001";
        List<ClauseAcquisitionCheckList> checkLists = new ArrayList<>();
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        Clause clau = Clause.builder().code("CLAUSE001").build();
        MatrixTypeAcquisitionClause acquisitionClause = MatrixTypeAcquisitionClause.builder().clause(clau).build();
        List<ClauseAcquisitionCheckList> lists = new ArrayList<>();
        lists.add(ClauseAcquisitionCheckList.builder().matrixTypeAcquisitionClause(acquisitionClause).build());

        checkLists = useCase.validatePreviousAcceptClause(lists, clauseCode);
        assertEquals(checkLists, lists);
    }

    @Test(expected = ValidationException.class)
    public void validatePreviousAcceptClauseTestFail() {
        String clauseCode = "CLAUSE002";
        List<ClauseAcquisitionCheckList> checkLists = new ArrayList<>();
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        Clause clau = Clause.builder().code("CLAUSE001").build();
        MatrixTypeAcquisitionClause acquisitionClause = MatrixTypeAcquisitionClause.builder().clause(clau).build();
        List<ClauseAcquisitionCheckList> lists = new ArrayList<>();
        lists.add(ClauseAcquisitionCheckList.builder().matrixTypeAcquisitionClause(acquisitionClause).build());

        checkLists = useCase.validatePreviousAcceptClause(lists, clauseCode);
    }

    @Test(expected = ValidationException.class)
    public void validatePreviousAcceptClauseTestFail1() {
        String clauseCode = "CLAUSE001";
        List<ClauseAcquisitionCheckList> checkLists = new ArrayList<>();
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        Clause clau = Clause.builder().code("CLAUSE001").build();
        MatrixTypeAcquisitionClause acquisitionClause = MatrixTypeAcquisitionClause.builder().clause(clau).build();
        List<ClauseAcquisitionCheckList> lists = new ArrayList<>();
        lists.add(ClauseAcquisitionCheckList.builder().matrixTypeAcquisitionClause(acquisitionClause).dateAcceptClause(new CoreFunctionDate().getDatetime()).build());

        checkLists = useCase.validatePreviousAcceptClause(lists, clauseCode);
    }

    @Test
    public void acceptClause() {
        BasicAcquisitionRequest request = BasicAcquisitionRequest.builder()
                .documentType("FS001").documentNumber("10101010").idAcq(UUID.randomUUID().toString()).build();
        String acceptClauses = "1";
        String clauseCode = "CLAUSE001";
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        List<ClauseAcquisitionCheckList> checkLists = new ArrayList<>();
        Clause clau = Clause.builder().code("CLAUSE001").build();
        MatrixTypeAcquisitionClause acquisitionClause = MatrixTypeAcquisitionClause.builder().clause(clau).build();
        checkLists.add(ClauseAcquisitionCheckList.builder().matrixTypeAcquisitionClause(acquisitionClause).build());

        Acquisition acq = Acquisition.builder().build();
        Mockito.doReturn(acq).when(useCase).validateAcquisition(request, "");
        Mockito.doReturn(list).when(useCase).findByAcquisition(acq);
        Mockito.doReturn(checkLists).when(useCase).validatePreviousAcceptClause(list, clauseCode);

        Mockito.doNothing().when(useCase).saveAll(checkLists);
        Mockito.doReturn(checkLists).when(this.markRevokeUseCase).checkOperation(checkLists, acq, "USER");
        Mockito.doNothing().when(this.useCase).markOperationClause(acq);

        Acquisition acqResponse = useCase.acceptClause(request, acceptClauses, clauseCode, "");
        assertNotNull(acqResponse);
    }

    @Test(expected = ValidationException.class)
    public void acceptClauseFail() {
        BasicAcquisitionRequest request = BasicAcquisitionRequest.builder().documentType("FS001").documentNumber(
                "10101010").idAcq(UUID.randomUUID().toString()).build();
        String acceptClauses = "0";
        String clauseCode = "CLAUSE001";
        List<ClauseAcquisitionCheckList> list = new ArrayList<>();
        List<ClauseAcquisitionCheckList> checkLists = new ArrayList<>();
        Clause clau = Clause.builder().code("CLAUSE001").build();
        MatrixTypeAcquisitionClause acquisitionClause = MatrixTypeAcquisitionClause.builder().clause(clau).build();
        checkLists.add(ClauseAcquisitionCheckList.builder().matrixTypeAcquisitionClause(acquisitionClause).build());

        Acquisition acq = Acquisition.builder().build();
        Mockito.doReturn(acq).when(this.useCase).validateAcquisition(request, "");
        Mockito.doReturn(acq).when(this.useCase).validateAcquisition(request, "");
        Mockito.doReturn(list).when(this.useCase).findByAcquisition(acq);
        Mockito.doReturn(checkLists).when(this.useCase).validatePreviousAcceptClause(list, clauseCode);

        Mockito.doNothing().when(this.useCase).saveAll(checkLists);
        Mockito.doReturn(checkLists).when(this.markRevokeUseCase).checkOperation(checkLists, acq, "USER");
        Mockito.doNothing().when(this.useCase).markOperationClause(acq);

        useCase.acceptClause(request, acceptClauses, clauseCode, "");
    }
}
