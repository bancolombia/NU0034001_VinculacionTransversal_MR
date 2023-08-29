package co.com.bancolombia.asyncdigitalization;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.asyncdigitalization.gateways.AsyncDigitalizationRepository;
import co.com.bancolombia.model.sqs.SqsMessage;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AsyncDigitalizationUseCaseImpl implements AsyncDigitalizationUseCase{

    private final AsyncDigitalizationRepository repository;

    @Override
    public AsyncDigitalization save(AsyncDigitalization asyncDigitalization) {
        return repository.save(asyncDigitalization);
    }

    @Override
    public Optional<AsyncDigitalization> findByAcquisition(Acquisition acquisition) {
        return Optional.ofNullable(repository.findByAcquisition(acquisition));
    }

    @Override
    public Optional<AsyncDigitalization> findBySqsMessage(SqsMessage sqsMessage) {
        return Optional.ofNullable(repository.findBySqsMessage(sqsMessage));
    }
}
