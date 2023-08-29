package co.com.bancolombia.validatedataextraction;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.validatedataextraction.ValidateDataExtraction;

import java.util.Optional;


public interface ValidateDataExtractionUseCase {
    ValidateDataExtraction getAnswer(Acquisition acquisition, String documentalTypeCode, String documentalSubTypeCode,
                                     SqsMessageParamAllObject sqsMessageParamAllObject);
    void validateBusinessException(ProcessDocumentKofaxError processDocumentKofaxError, Acquisition acquisition,
                                   SqsMessageParamAllObject sqsMessageParamAllObject);
    Optional<SqsMessage> findValidMessage(Acquisition acq, String documentalTypeCode, String documentalSubTypeCode);
}
