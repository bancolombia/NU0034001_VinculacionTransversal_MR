package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.GeneratePdfResponseData;

public interface GeneratePdfProcessUseCase {
    GeneratePdfResponseData generatePdf(AcquisitionPdf acquisitionPdf);
}
