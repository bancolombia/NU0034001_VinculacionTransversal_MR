package co.com.bancolombia.logfunctionalvnt.log.usescases.checklist;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;

import java.util.List;

public interface ILogFuncCheckListUseCase {

    /**
     * Returns the activities related to the acq.
     *
     * @param acquisition
     * @return List of activities.
     */
    public List<CheckList> getCheckListByAcquisition(Acquisition acquisition);


}
