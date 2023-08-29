package co.com.bancolombia.model.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UploadDocumentParameter {
	private Acquisition acquisition;
	private List<ProcessDocumentFiles> processDocumentFiles;
	private String documentSubtype;
	private String usrTransaction;
	private	List<UploadDocumentFile> listGet;
	private SqsMetaUploadDocument meta;
	private String messageId;
	private SqsMessage sqsMessage;
}
