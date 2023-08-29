package co.com.bancolombia.clauseacquisitionchecklist;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.acquisition.BasicAcquisitionRequest;

import java.util.List;

public interface ClauseAcquisitionChecklistUseCase {

    public void startCheckList(TypeAcquisition typeAcquisition, Acquisition acquisition);

    public void saveAll(List<ClauseAcquisitionCheckList> list);

    public List<ClauseAcquisitionCheckList> findByAcquisition(Acquisition acquisition);

    public Acquisition acceptClause(BasicAcquisitionRequest request, String acceptClauses, String clauseCode,
                                    String operation);

    public Acquisition validateAcquisition(BasicAcquisitionRequest request, String operation);

    public void markOperationClause(Acquisition acq);

    public List<ClauseAcquisitionCheckList> validatePreviousAcceptClause(List<ClauseAcquisitionCheckList> lists,
                                                                         String clauseCode);
}
