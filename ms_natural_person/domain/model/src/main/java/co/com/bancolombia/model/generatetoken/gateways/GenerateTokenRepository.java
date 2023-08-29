package co.com.bancolombia.model.generatetoken.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.generatetoken.GenerateToken;

public interface GenerateTokenRepository {

    GenerateToken findByAcquisition(Acquisition acq);
    GenerateToken save(GenerateToken generateToken);
    GenerateToken findTopByAcquisitionOrderByCreatedDateDesc(Acquisition acquisition);
}
