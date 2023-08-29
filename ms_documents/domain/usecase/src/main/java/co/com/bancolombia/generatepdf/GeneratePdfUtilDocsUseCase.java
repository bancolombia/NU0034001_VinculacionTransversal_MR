package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.UtilContentStream;

import java.util.List;

public interface GeneratePdfUtilDocsUseCase {
	String findUrlTemplate(String typeAcquisition);

	List<UtilContentStream> getCoordinates(String typeAcquisition);
}
