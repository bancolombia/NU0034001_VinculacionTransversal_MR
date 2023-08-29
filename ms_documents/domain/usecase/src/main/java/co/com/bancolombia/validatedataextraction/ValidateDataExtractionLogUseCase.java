package co.com.bancolombia.validatedataextraction;

import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentApiResponseData;
import co.com.bancolombia.model.validatedataextraction.ValidateDataExtraction;

public interface ValidateDataExtractionLogUseCase {
	public void saveInfoLog(AsyncDigitalization asyncDigitalization);
	ValidateDataExtraction getObjectValid(AsyncDigitalization asyncDigitalization,
										  UploadDocumentApiResponseData objSuccess);
}
