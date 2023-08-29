package co.com.bancolombia.model.asyncdigitalization.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.sqs.SqsMessage;

public interface AsyncDigitalizationRepository {
    AsyncDigitalization save (AsyncDigitalization asyncDigitalization);
    AsyncDigitalization findByAcquisition (Acquisition acquisition);
    AsyncDigitalization findBySqsMessage (SqsMessage sqsMessage);
}
