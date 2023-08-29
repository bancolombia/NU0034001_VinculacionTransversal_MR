package co.com.bancolombia.model.markrevoke.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.markrevoke.MarkRevoke;

public interface MarkRevokeRepository {

    MarkRevoke save(MarkRevoke basicInformation);

    MarkRevoke findByAcquisition(Acquisition acquisition);
}
