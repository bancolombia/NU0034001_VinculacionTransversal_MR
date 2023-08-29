package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.KofaxRutInformation;

public interface UploadDocumentRutSaveUseCase {
	boolean validateAndSaveInformation(KofaxRutInformation kofaxRutInformation, Acquisition acquisition,
			String userTransaction, SqsMessageParamAllObject sqsMessageParamAllObject);
}
