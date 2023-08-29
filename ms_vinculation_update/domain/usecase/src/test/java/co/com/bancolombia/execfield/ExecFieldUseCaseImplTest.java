package co.com.bancolombia.execfield;

import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.execfield.gateways.ExecFieldRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExecFieldUseCaseImplTest {

    @InjectMocks
    @Spy
    ExecFieldUseCaseImpl execFieldUseCase;

    @Mock
    ExecFieldRepository execFieldRepository;

    @Test
    public void insertExecFieldByMAcqAndChecklist() {
        this.execFieldUseCase.insertExecFieldByMAcqAndChecklist(any(MatrixAcquisition.class), any(CheckList.class));
        verify(this.execFieldUseCase, times(1)).insertExecFieldByMAcqAndChecklist(null,null);
    }

    @Test
    public void findByChecklist() {
        this.execFieldUseCase.findByChecklist(any(CheckList.class));
        verify(this.execFieldUseCase, times(1)).findByChecklist(null);
    }
}
