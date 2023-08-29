package co.com.bancolombia.usecase.rabbit.vinculationupdate;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;

import java.util.List;
import java.util.Map;

public interface TriggerExceptionUseCase {
    void trigger(Map<String, List<ErrorField>> stringListMap);
}