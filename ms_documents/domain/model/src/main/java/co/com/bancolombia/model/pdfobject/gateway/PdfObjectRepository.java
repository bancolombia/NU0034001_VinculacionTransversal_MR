package co.com.bancolombia.model.pdfobject.gateway;

import co.com.bancolombia.model.pdfobject.PdfObject;

import java.util.List;

public interface PdfObjectRepository {
	List<PdfObject> findByTypeAcquisition(String typeAcquisition);
}
