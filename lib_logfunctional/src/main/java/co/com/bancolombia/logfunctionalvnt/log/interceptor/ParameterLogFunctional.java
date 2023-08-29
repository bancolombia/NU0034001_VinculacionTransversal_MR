package co.com.bancolombia.logfunctionalvnt.log.interceptor;

import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;
import co.com.bancolombia.logfunctionalvnt.log.model.LogObjectAttribute;
import lombok.Builder;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Data
@Builder
public class ParameterLogFunctional {
    LogFunctionalDTO logFunctionalDTO;
    String newResponse;
    ILogRegister logRegister;
    HttpServletRequest request;
    LogObjectAttribute logObjectAttribute;
    Map<String, String> optionals;
    String stateOperation;

    String stateAcquisition;

    LogFunctionalReuse logFunctionalReuse;
}
