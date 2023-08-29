package co.com.bancolombia.common.validateinfogeneric;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorFieldObject {
    public List<ErrorField> retListSame;
    public List<ErrorField> retListOther;
}
