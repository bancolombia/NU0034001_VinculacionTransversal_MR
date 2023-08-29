package co.com.bancolombia.markrevoke;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.model.markrevoke.MarkRevoke;

import java.util.List;

public interface MarkRevokeUseCase {

    public MarkRevoke save(MarkRevoke markRevoke);

    public List<ClauseAcquisitionCheckList> checkOperation(
            List<ClauseAcquisitionCheckList> list, Acquisition acquisition, String userTransaction
    );

    public void uncheckRevocation(MarkRevoke revoke);

    public void checkRevocation(MarkRevoke revoke);

    public void checkAuthorization(MarkRevoke revoke);

    public void uncheckAuthorization(MarkRevoke revoke);

    public boolean switchActions(String action, MarkRevoke revoke,boolean blAction);
}
