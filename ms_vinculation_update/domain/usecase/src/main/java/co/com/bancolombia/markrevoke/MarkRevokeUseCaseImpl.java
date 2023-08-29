package co.com.bancolombia.markrevoke;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.markrevoke.MarkRevoke;
import co.com.bancolombia.model.markrevoke.gateways.MarkRevokeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ACCEPT_CLAUSES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;

@RequiredArgsConstructor
public class MarkRevokeUseCaseImpl implements MarkRevokeUseCase {
    private final MarkRevokeRepository markRevokeRepository;

    private final String accept = Numbers.ONE.getNumber();
    private final String reject = Numbers.ZERO.getNumber();

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, CODE_ACCEPT_CLAUSES);

    @Override
    public boolean switchActions(String action, MarkRevoke revoke, boolean blAction) {
        switch (action) {
            case Constants.UNCHECK_REVOCATION:
                uncheckRevocation(revoke);
                blAction = true;
                break;
            case Constants.CHECK_AUTORIZATION:
                checkAuthorization(revoke);
                blAction = true;
                break;
            case Constants.CHECK_REVOCATION:
                checkRevocation(revoke);
                blAction = true;
                break;
            case Constants.UNCHECK_AUTORIZATION:
                uncheckAuthorization(revoke);
                blAction = true;
                break;
            default:
                adapter.warn("Actions not parameterized");
                break;
        }
        return blAction;
    }

    @Override
    public MarkRevoke save(MarkRevoke markRevoke) {
        return this.markRevokeRepository.save(markRevoke);
    }

    @Override
    public List<ClauseAcquisitionCheckList> checkOperation(
            List<ClauseAcquisitionCheckList> list, Acquisition acquisition, String userTransaction
    ) {
        MarkRevoke revoke = this.markRevokeRepository.findByAcquisition(acquisition);
        if (revoke == null) {
            revoke = MarkRevoke.builder().createdDate(new CoreFunctionDate().getDatetime()).createdBy(userTransaction)
                    .acquisition(acquisition).build();
        } else {
            revoke.setUpdatedBy(userTransaction);
            revoke.setUpdatedDate(new CoreFunctionDate().getDatetime());
        }
        boolean blAction = false;
        for (ClauseAcquisitionCheckList item : list) {
            blAction = switchActions(item.getMatrixTypeAcquisitionClause().getAction().getCode(), revoke, blAction);
        }
        if (blAction) {
            this.save(revoke);
        }
        return list;
    }

    @Override
    public void uncheckRevocation(MarkRevoke revoke) {
        revoke.setSharingInformation(reject);
        revoke.setTelesalesCommercialOffers(reject);
        revoke.setSendTextMessage(reject);
        revoke.setSendingEmails(reject);
    }

    @Override
    public void checkRevocation(MarkRevoke revoke) {
        revoke.setSharingInformation(accept);
        revoke.setTelesalesCommercialOffers(accept);
        revoke.setSendTextMessage(accept);
        revoke.setSendingEmails(accept);
    }

    @Override
    public void checkAuthorization(MarkRevoke revoke) {
        revoke.setVirtualCosts(accept);
        revoke.setVirtualExtracts(accept);
        revoke.setElectronicMediaExtracts(accept);
    }

    @Override
    public void uncheckAuthorization(MarkRevoke revoke) {
        revoke.setVirtualCosts(reject);
        revoke.setVirtualExtracts(reject);
        revoke.setElectronicMediaExtracts(reject);
    }
}
