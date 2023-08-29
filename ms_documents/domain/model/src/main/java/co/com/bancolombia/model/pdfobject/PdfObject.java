package co.com.bancolombia.model.pdfobject;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PdfObject {
	private UUID uuid;
	private String key;
	private int page;
	private String color;
	private int fontSize;
	private String fontName;
	private int positionX;
	private int positionY;
	private String typeAcquisition;
}
