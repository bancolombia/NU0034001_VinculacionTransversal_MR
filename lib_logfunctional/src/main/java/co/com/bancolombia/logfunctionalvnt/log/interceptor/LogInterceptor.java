package co.com.bancolombia.logfunctionalvnt.log.interceptor;

import co.com.bancolombia.commonsvnt.util.ConstantLog;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalException;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;
import co.com.bancolombia.logfunctionalvnt.log.model.LogObjectAttribute;
import co.com.bancolombia.logfunctionalvnt.log.service.ILogFunctionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component
@EnableWebMvc
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private ILogFunctionalService logFunctionalService;

    @Autowired
    private LogInterceptorUtil logInterceptorUtil;



    public Object recoveryValueRequest(HttpServletRequest request, String atribute) {
        Object result = request.getSession()
                .getAttribute(atribute);
        request.getSession().removeAttribute(atribute);
        return result;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws LogFunctionalException {


        if (response instanceof ContentCachingResponseWrapper) {
            HandlerMethod method = (HandlerMethod) handler;
            ILogRegister logRegister = this.getLogRegister(method);
            if (logRegister != null) {
                LogObjectAttribute logObjectAttribute = (LogObjectAttribute)
                        recoveryValueRequest(request, ConstantLog.LOGFIELD_OBJECT_ATTRIBUTE);
                if (logObjectAttribute != null) {
                    Map<String, List<String>> fieldsCanNotSave = logObjectAttribute.getFieldNotSve();
                    LogFunctionalDTO logFunctionalDTO = this.logInterceptorUtil.getLog((ContentCachingRequestWrapper)
                            request, (ContentCachingResponseWrapper) response);


                    String stateAcquisition = (String)
                            recoveryValueRequest(request, ConstantLog.LOGFIELD_STATE_ACQUISITION);
                    String stateOperation = (String)
                            recoveryValueRequest(request, ConstantLog.LOGFIELD_STATE_OPERATION);
                    Map<String, String> optionals = this.logInterceptorUtil
                            .getRequest((ContentCachingRequestWrapper) request);
                    LogFunctionalReuse logFunctionalReuse = (LogFunctionalReuse)
                            recoveryValueRequest(request, ConstantLog.LOGFIELD_REUSE_INFO);
                    String newResponse = LogInterceptorUtil.GSON.toJson(
                            this.logInterceptorUtil.getDataResponse(request, logFunctionalDTO));
                    LogFunctionalDTO logFunctionalFinal = this.logInterceptorUtil.getLogFunctionalDTO(
                            ParameterLogFunctional.builder()
                                    .logFunctionalDTO(logFunctionalDTO).newResponse(newResponse)
                                    .logRegister(logRegister).request(request)
                                    .logObjectAttribute(logObjectAttribute)
                                    .optionals(optionals).stateOperation(stateOperation)
                                    .stateAcquisition(stateAcquisition)
                                    .logFunctionalReuse(logFunctionalReuse)
                                    .build());
                    this.logFunctionalService.save(logFunctionalFinal, fieldsCanNotSave);
                } else {
                    LogInterceptorUtil.LOGGER_ADAPTER.info("logObjectAttribute IS null");
                }
            }

        } else {
            LogInterceptorUtil.LOGGER_ADAPTER.info("Respuesta No controlada en " + request.getRequestURI());
        }

    }

    public ILogRegister getLogRegister(HandlerMethod method) {
        return (method.hasMethodAnnotation(ILogRegister.class)) ? method.getMethodAnnotation(ILogRegister.class) : null;
    }



}
