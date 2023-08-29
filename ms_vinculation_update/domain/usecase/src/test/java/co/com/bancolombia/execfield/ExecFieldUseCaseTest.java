package co.com.bancolombia.execfield;

import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.execfield.gateways.ExecFieldRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ExecFieldUseCaseTest {

    @InjectMocks
    @Spy
    private ExecFieldUseCaseImpl execFieldUseCase;

    @Mock
    private ExecFieldRepository execFieldRepository;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void insertExecFieldByMAcqAndChecklistTest() {
        MatrixAcquisition matrixAcquisition = MatrixAcquisition.builder().build();
        CheckList checkList = CheckList.builder().build();
        execFieldUseCase.insertExecFieldByMAcqAndChecklist(matrixAcquisition, checkList);

        verify(execFieldRepository, times(1)).insertExecFieldByMAcqAndChecklist(
                any(MatrixAcquisition.class), any(CheckList.class));
    }

    @Test
    public void findByChecklistTest() {
        CheckList checkList = CheckList.builder().build();
        execFieldUseCase.findByChecklist(checkList);

        verify(execFieldRepository, times(1)).findByChecklist(any(CheckList.class));
    }
}