package co.com.bancolombia.model.uploaddocument.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.uploaddocument.DigitalizationRutSave;

public interface DigitalizationRutRepository {
    DigitalizationRutSave save(DigitalizationRutSave digitalizationRutSave);

    DigitalizationRutSave findByAcquisition(Acquisition acquisition);
}
