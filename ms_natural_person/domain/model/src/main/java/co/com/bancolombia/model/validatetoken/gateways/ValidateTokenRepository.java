package co.com.bancolombia.model.validatetoken.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.validatetoken.ValidateToken;

public interface ValidateTokenRepository {
    ValidateToken save (ValidateToken validateToken);
    ValidateToken findByAcquisition(Acquisition acquisition);
    ValidateToken findByAcquisitionLast(Acquisition acquisition);
}
