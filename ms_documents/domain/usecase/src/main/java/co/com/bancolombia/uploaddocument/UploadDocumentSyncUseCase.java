package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;

import java.util.List;

public interface UploadDocumentSyncUseCase {
	boolean validateAsynchronousProcess(List<UploadDocumentFile> listGet);

	UploadDocumentWithLog asynchronousProcess(UploadDocumentParameter uploadDocumentParameter, String messageId);

	ProcessDocument getRequest(
			Acquisition acquisition, List<ProcessDocumentFiles> processDocumentFiles,
			String documentSubtype, String messageId);
}
