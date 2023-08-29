package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;

import java.util.List;

public interface UploadDocumentValidateUseCase {
	List<ProcessDocumentFiles> getProcessDocumentFiles(String documentSubtype,
			List<UploadDocumentFile> listGet, SqsMessageParamAllObject sqsMessageParamAllObject);

	boolean validateRutDataExtraction(Acquisition acquisition, SqsMessageParamAllObject sqsMessageParamAllObject);
}
