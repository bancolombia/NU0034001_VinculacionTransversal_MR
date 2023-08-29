package co.com.bancolombia.model.uploaddocument.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.uploaddocument.DigitalizationIdentitySave;

public interface DigitalizationIdentityRepository {
    DigitalizationIdentitySave save(DigitalizationIdentitySave digitalizationIdentitySave);

    DigitalizationIdentitySave findByAcquisition(Acquisition acquisition);
}
