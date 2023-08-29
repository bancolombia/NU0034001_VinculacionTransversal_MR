package co.com.bancolombia.model.clauseacquisitionchecklist.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;

import java.util.List;

public interface ClauseAcquisitionCheckListRepository {
     List<ClauseAcquisitionCheckList> saveAll(List<ClauseAcquisitionCheckList> lists);
     List<ClauseAcquisitionCheckList> findByAcquisition(Acquisition acquisition);
}
