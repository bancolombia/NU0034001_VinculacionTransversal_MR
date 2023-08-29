package co.com.bancolombia.asyncdigitalization;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.sqs.SqsMessage;

import java.util.Optional;

public interface AsyncDigitalizationUseCase {
    AsyncDigitalization save (AsyncDigitalization asyncDigitalization);

    Optional<AsyncDigitalization> findByAcquisition (Acquisition acquisition);

    Optional<AsyncDigitalization> findBySqsMessage (SqsMessage sqsMessage);
}
