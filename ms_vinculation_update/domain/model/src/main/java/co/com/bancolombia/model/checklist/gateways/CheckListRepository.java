package co.com.bancolombia.model.checklist.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.step.Step;

import java.util.List;

public interface CheckListRepository {
    CheckList save(CheckList paramCheckList);

    List<CheckList> saveAll(List<CheckList> list);

    CheckList findByAcquisitionAndStep(Acquisition acquisition, Step step);

    List<CheckList> findByAcquisition(Acquisition acquisition);
}
