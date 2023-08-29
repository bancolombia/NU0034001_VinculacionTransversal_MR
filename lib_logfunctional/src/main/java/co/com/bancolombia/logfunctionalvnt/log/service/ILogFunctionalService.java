package co.com.bancolombia.logfunctionalvnt.log.service;

import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;

import java.util.List;
import java.util.Map;


public interface ILogFunctionalService {
    public boolean save(LogFunctionalDTO logFunctional, Map<String, List<String>> fieldsCanNotSave);
}
