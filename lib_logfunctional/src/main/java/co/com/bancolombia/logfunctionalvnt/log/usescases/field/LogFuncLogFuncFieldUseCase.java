package co.com.bancolombia.logfunctionalvnt.log.usescases.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogFuncLogFuncFieldUseCase implements ILogFuncFieldUseCase {
    @Override
    public Map<String, List<String>> fieldsCanNotSaveByOperation(String codeOperation) {
        return new HashMap<>();
    }

	@Override
	public Map<String, List<String>> fieldsAllCanNotSaveByActive(boolean active) {
		return new HashMap<>();
	}
}
