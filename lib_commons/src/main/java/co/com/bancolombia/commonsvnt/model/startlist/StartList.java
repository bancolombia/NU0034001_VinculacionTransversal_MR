package co.com.bancolombia.commonsvnt.model.startlist;

import co.com.bancolombia.commonsvnt.model.activity.Activity;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class StartList  {
    private String acquisitionId;
    private String documentType;
    private String documentNumber;
    private String registrationDate;
    private String codeStateAcquisition;
    private String nameStateAcquisition;
    private List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists;
    private List<Activity> activitiesList;
}
