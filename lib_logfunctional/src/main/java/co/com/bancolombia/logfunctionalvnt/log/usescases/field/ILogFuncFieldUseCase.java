package co.com.bancolombia.logfunctionalvnt.log.usescases.field;

import java.util.List;
import java.util.Map;

public interface ILogFuncFieldUseCase {
    public Map<String, List<String>> fieldsCanNotSaveByOperation(String codeOperation);
    public Map<String, List<String>> fieldsAllCanNotSaveByActive(boolean active);
}
