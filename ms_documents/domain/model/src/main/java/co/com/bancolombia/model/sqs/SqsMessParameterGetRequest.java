package co.com.bancolombia.model.sqs;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SqsMessParameterGetRequest {
	private Acquisition acquisition;
	private List<ProcessDocumentFiles> processDocumentFiles;
	private String documentSubtype;
	private String messageId;
	private SqsMetaUploadDocument sqsMeta;
}
