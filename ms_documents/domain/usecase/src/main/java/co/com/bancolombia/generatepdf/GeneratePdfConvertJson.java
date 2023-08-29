package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import com.google.gson.JsonObject;

public interface GeneratePdfConvertJson {
    JsonObject getInfo(AcquisitionPdf acqPdf);
}
