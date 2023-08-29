package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatetoken.ValidateToken;

public interface ValidateTokenUseCase {
    ValidateToken saveInfo(ValidateToken validateToken);
    ValidateToken findByAcquisition(Acquisition acquisition);
    ValidateToken findByAcquisitionLast(Acquisition acquisition);
    ValidateToken startProcessValidateToken(
            BasicAcquisitionRequest request, ValidateToken validateToken);

}
