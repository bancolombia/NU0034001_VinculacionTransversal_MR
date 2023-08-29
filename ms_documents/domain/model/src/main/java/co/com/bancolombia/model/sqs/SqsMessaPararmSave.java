package co.com.bancolombia.model.sqs;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqsMessaPararmSave {
	private Acquisition acquisition;
	private SqsMessObjUploadDoc obj;
	private SqsMessage sqsMessage;
	private UploadDocumentWithLog uploadDocumentWithLog;
	private boolean saveAsync;
}
