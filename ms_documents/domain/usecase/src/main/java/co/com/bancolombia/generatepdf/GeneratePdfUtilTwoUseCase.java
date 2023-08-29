package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;

import java.util.Optional;

public interface GeneratePdfUtilTwoUseCase {
    Optional<AcquisitionPdf> validateAndFindInfo(Acquisition acquisition);
}
