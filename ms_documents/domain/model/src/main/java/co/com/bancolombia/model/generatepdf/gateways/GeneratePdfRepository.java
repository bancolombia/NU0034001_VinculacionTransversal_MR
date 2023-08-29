package co.com.bancolombia.model.generatepdf.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.generatepdf.GeneratePdf;

public interface GeneratePdfRepository {
    GeneratePdf save(GeneratePdf generatePdf);

    GeneratePdf findTopByAcquisitionOrderByCreatedDateDesc(Acquisition acq);
}