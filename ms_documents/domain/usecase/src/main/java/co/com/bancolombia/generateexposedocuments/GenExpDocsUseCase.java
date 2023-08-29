package co.com.bancolombia.generateexposedocuments;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.exposedocuments.ExposeDocs;
import co.com.bancolombia.model.generatepdf.GeneratePdf;

public interface GenExpDocsUseCase {
    GeneratePdf getPdf(Acquisition acquisition);

    ExposeDocs urlFinal(Acquisition acquisition, GeneratePdf generatePdf);
}
