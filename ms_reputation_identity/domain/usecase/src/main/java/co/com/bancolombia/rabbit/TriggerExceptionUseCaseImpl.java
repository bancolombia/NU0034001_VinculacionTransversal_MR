package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TriggerExceptionUseCaseImpl implements TriggerExceptionUseCase {

    @Override
    public void trigger(Map<String, List<ErrorField>> stringListMap) {
        throw new ValidationException(stringListMap);
    }
}
