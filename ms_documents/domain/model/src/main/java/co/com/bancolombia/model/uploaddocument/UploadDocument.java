package co.com.bancolombia.model.uploaddocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UploadDocument {
	private String processID;
	private String dateProcess;
	private String codeResponseProcess;
	private String responseProcess;
	private List<ProcessedDocument> processedDocument;
}
