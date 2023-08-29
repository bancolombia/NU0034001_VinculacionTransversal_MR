package co.com.bancolombia.checklist;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.execfield.ExecFieldUseCase;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
import co.com.bancolombia.step.StepUseCase;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class CheckListValidationUseCaseTest {

    @InjectMocks
    @Spy
    private CheckListValidationUseCaseImpl checkListValidationUseCase;

    @Mock
    private StepUseCase stepUseCase;

    @Mock
    private AcquisitionUseCase acquisitionUseCase;

    @Mock
    private CheckListRepository checkListRepository;

    @Mock
    private ExecFieldUseCase execFieldUseCase;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void getExecFieldListByStepTest() {
        Mockito.doReturn(Acquisition.builder().build()).when(acquisitionUseCase).findAndValidateById(any(UUID.class));
        Mockito.doReturn(Optional.of(Step.builder().build())).when(stepUseCase).findByCode(anyString());
        Mockito.doReturn(CheckList.builder().build()).when(checkListRepository).findByAcquisitionAndStep(
                any(Acquisition.class), any(Step.class));
        Mockito.doReturn(new ArrayList<>()).when(execFieldUseCase).findByChecklist(any(CheckList.class));
        List<ExecField> execFields = checkListValidationUseCase.getExecFieldListByStep(UUID.randomUUID(), "");
        assertNotNull(execFields);
    }

    @Test(expected = CustomException.class)
    public void getExecFieldListByStepExceptionTest() {
        Mockito.doReturn(Acquisition.builder().build()).when(acquisitionUseCase).findAndValidateById(any(UUID.class));
        Mockito.doReturn(Optional.empty()).when(stepUseCase).findByCode(anyString());
        checkListValidationUseCase.getExecFieldListByStep(UUID.randomUUID(), "");
    }

    @Test
    public void getCheckListByAcquisitionAndStepTest() {
        Step step = Step.builder().build();
        CheckList checkList = CheckList.builder().build();
        Acquisition acquisition = Acquisition.builder().build();

        Mockito.doReturn(Optional.of(step)).when(stepUseCase).findByCode(anyString());
        Mockito.doReturn(checkList).when(checkListRepository)
                .findByAcquisitionAndStep(any(Acquisition.class), any(Step.class));

        CheckList checkListResponse = checkListValidationUseCase.getCheckListByAcquisitionAndStep(acquisition, "");
        assertNotNull(checkListResponse);
    }

    @Test(expected = CustomException.class)
    public void getCheckListByAcquisitionAndStepExceptionTest() {
        CheckList checkList = CheckList.builder().build();
        Acquisition acquisition = Acquisition.builder().build();

        Mockito.doReturn(Optional.empty()).when(stepUseCase).findByCode(anyString());
        Mockito.doReturn(checkList).when(checkListRepository)
                .findByAcquisitionAndStep(any(Acquisition.class), any(Step.class));

        checkListValidationUseCase.getCheckListByAcquisitionAndStep(acquisition, "");
    }
}
