package co.com.bancolombia.logfunctionalvnt.log.usescases.checklist;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.step.Step;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LogFuncLogFuncCheckListUseCase implements ILogFuncCheckListUseCase {

    final Acquisition acquisition = Acquisition.builder()
            .id(UUID.randomUUID())
            .documentNumber("123456789")
            .build();

    final private Step step = Step.builder()
            .code("001")
            .name("STEP001")
            .build();
    final private StateStep state = StateStep.builder()
            .code("001")
            .name("STATESTEP001")
            .build();

    final private List<ExecField> execFieldList = Arrays.asList(ExecField.builder()
            .id(UUID.randomUUID())
            .code("001")
            .name("name")
            .mandatory(true)
            .upgradeable(true)
            .build());


    final CheckList checkList = CheckList.builder()
            .id(UUID.randomUUID())
            .acquisition(acquisition)
            .step(step)
            .state(state)
            .execFieldList(execFieldList)
            .build();

    final ExecField execField = ExecField.builder()
            .id(UUID.randomUUID())
            .code("001")
            .name("name")
            .mandatory(true)
            .upgradeable(true)
            .checkList(checkList)
            .build();


    @Override
    public List<CheckList> getCheckListByAcquisition(Acquisition acquisition) {
        return Arrays.asList(checkList);
    }


}
