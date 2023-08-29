package co.com.bancolombia.model.uploaddocument;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@Validated
@AllArgsConstructor
public class UploadDocumentWithLog {
	private InfoReuseCommon infoReuseCommon;
	private UploadDocumentTotal uploadDocumentTotal;
	private ProcessDocumentKofaxTotal processDocumentKofaxTotal;
}
