package co.com.bancolombia.generatepdf;

import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPOSE_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.util.constants.Constants.ERROR_CONVERTING_PDFA1B;

@RequiredArgsConstructor
public class GenPdfA1BImpl implements GenPdfA1B {

	private LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, OPER_EXPOSE_DOCS, "GenPdfA1B");

	@Override
	public PDDocument convertPdfA1B(PDDocument documentFinal) {
		XMPMetadata xmp = XMPMetadata.createXMPMetadata();

		try {
			DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
			dc.setTitle("");

			PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
			id.setPart(1);
			id.setConformance("B");

			XmpSerializer serializer = new XmpSerializer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(xmp, baos, true);

			PDMetadata metadata = new PDMetadata(documentFinal);
			metadata.importXMPMetadata(baos.toByteArray());
			documentFinal.getDocumentCatalog().setMetadata(metadata);
		} catch (BadFieldValueException | IOException | TransformerException e) {
			adapter.error(ERROR_CONVERTING_PDFA1B);
			return null;
		}

		return documentFinal;
	}
}
