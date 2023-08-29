package co.com.bancolombia.commonsvnt.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Map<String,List<ErrorField>> errorFieldsCodes;
}
