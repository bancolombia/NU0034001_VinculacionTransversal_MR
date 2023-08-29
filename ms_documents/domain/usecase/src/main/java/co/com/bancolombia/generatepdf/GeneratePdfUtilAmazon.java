package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.util.Map;

public interface GeneratePdfUtilAmazon {
    Map<String, String> saveInAmazon(
            AcquisitionPdf acquisitionPdf, PDDocument document, PDPage page) throws IOException;
}
