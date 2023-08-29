package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.UtilContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.Map;

public interface GeneratePdfDocumentUseCase {
	void configureContentStream(UtilContentStream utilContentStream, PDDocument document, PDPage page);

	Map<String, String> generatePdf(AcquisitionPdf acquisitionPdf);
}
