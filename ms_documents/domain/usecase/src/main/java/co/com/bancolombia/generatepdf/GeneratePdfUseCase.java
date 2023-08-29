package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.generatepdf.GeneratePdf;

public interface GeneratePdfUseCase {
	GeneratePdf findByAcquisition(Acquisition acquisition);

	GeneratePdf generatePdf(Acquisition acquisition, String createdBy);
}
