package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

@Data
@Builder
public class UtilContentStream {
	private String key;
	private int page;
	private Color color;
	private int fontSize;
	private PDFont fontName;
	private int positionX;
	private int positionY;
	private String text;
}
