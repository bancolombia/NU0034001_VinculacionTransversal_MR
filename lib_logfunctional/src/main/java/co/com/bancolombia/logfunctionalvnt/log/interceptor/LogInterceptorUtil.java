package co.com.bancolombia.logfunctionalvnt.log.interceptor;

import co.com.bancolombia.commonsvnt.util.ConstantLog;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class LogInterceptorUtil {

    private static final String APP = "NU0034001_VinculacionTransversal_LogFunctional";
    public static final LoggerAdapter LOGGER_ADAPTER = new LoggerAdapter(
            APP, "LogInterceptor", LogInterceptorUtil.class.toString());

    public static final String LOGFIELD_DATEREQUEST = "DATEREQUEST";
    public static final String LOGFIELD_DATERESPONSE = "DATERESPONSE";
    public static final String[] HEADERS_IP = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};
    public static final Gson GSON = new Gson();
    private static final JsonObject JSON_OBJECT_NULL = new JsonObject();

    public LogFunctionalDTO getLogFunctionalDTO(ParameterLogFunctional paLoFo) {
        return LogFunctionalDTO.builder()
                .request(paLoFo.getLogFunctionalDTO().getRequest())
                .response(paLoFo.getNewResponse())
                .manangment(paLoFo.getLogRegister().api())
                .ipAddress(this.getIpAddress(paLoFo.getRequest()))
                .acquisitionId(this.convertString(paLoFo.getLogObjectAttribute().getAcquisitionId(),
                        paLoFo.getOptionals(), ConstantLog.LOGFIELD_ACQUISITION_ID))
                .documentNumber(this.convertString(paLoFo.getLogObjectAttribute().getDocumentNumber(),
                        paLoFo.getOptionals(), ConstantLog.LOGFIELD_DOCUMENT_NUMBER))
                .documentType(this.convertString(paLoFo.getLogObjectAttribute().getDocumentType(),
                        paLoFo.getOptionals(), ConstantLog.LOGFIELD_DOCUMENT_TYPE))
                .operation(paLoFo.getLogRegister().operation())
                .dateInitOperation(paLoFo.getLogObjectAttribute().getDateInit())
                .stateOperation(paLoFo.getStateOperation()).stateAcquisition(paLoFo.getStateAcquisition())
                .requestReuse(this.getReuseStr(paLoFo.getLogFunctionalReuse(), ConstantLog.LOGFIELD_REQUEST))
                .requestDateReuse(this.getReuseDate(paLoFo.getLogFunctionalReuse(),
                        LogInterceptorUtil.LOGFIELD_DATEREQUEST))
                .responseReuse(this.getReuseStr(paLoFo.getLogFunctionalReuse(), ConstantLog.LOGFIELD_RESPONSE))
                .responseDateReuse(this.getReuseDate(paLoFo.getLogFunctionalReuse(),
                        LogInterceptorUtil.LOGFIELD_DATERESPONSE))
                .createdBy(paLoFo.getLogObjectAttribute().getCreatedBy())
                .build();
    }


    public DataResponse getDataResponse(HttpServletRequest request, LogFunctionalDTO logFunctionalDTO) {
        String camposAdicionalesResponse = (String) request.getSession()
                .getAttribute(ConstantLog.LOGFIELD_OTHER_FIELD_DATA);
        request.getSession().removeAttribute(ConstantLog.LOGFIELD_OTHER_FIELD_DATA);
        return DataResponse.builder()
                .response(logFunctionalDTO.getResponse() != null ?
                        new JsonParser().parse(logFunctionalDTO.getResponse()).getAsJsonObject() :
                        JSON_OBJECT_NULL)
                .otherDataResponse(camposAdicionalesResponse != null ?
                        new JsonParser().parse(camposAdicionalesResponse).getAsJsonObject() :
                        JSON_OBJECT_NULL)
                .build();
    }

    public String getReuseStr(LogFunctionalReuse logFunctionalReuse, String wichReuse) {

        String result = GSON.toJson(JSON_OBJECT_NULL);
        if (logFunctionalReuse != null) {
            switch (wichReuse) {
                case ConstantLog.LOGFIELD_REQUEST:
                    result = this.transformJsonString(logFunctionalReuse.getRequestReuse());
                    break;
                case ConstantLog.LOGFIELD_RESPONSE:
                    result = this.transformJsonString(logFunctionalReuse.getResponseReuse());
                    break;
                default:
                    result = GSON.toJson(JSON_OBJECT_NULL);
            }


        }

        return result;
    }

    public LogFunctionalDTO getLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        String bodyRequest = this.getAttribute(request, ConstantLog.LOGFIELD_REQUEST);
        String bodyResponse = this.getAttribute(request, ConstantLog.LOGFIELD_RESPONSE);

        String bodyRequestSave = (bodyRequest == null) ? new String(request.getContentAsByteArray()) : bodyRequest;
        String bodyResponseSave = (bodyResponse == null) ? new String(response.getContentAsByteArray()) : bodyRequest;

        return LogFunctionalDTO.builder().request(bodyRequestSave).response(bodyResponseSave).build();

    }

    public String getAttribute(ContentCachingRequestWrapper request, String field) {
        Object attribute = request.getAttribute(field);
        return (attribute != null) ? attribute.toString() : null;
    }

    public String transformJsonString(String cadena) {
        try {
            JsonObject jsonObjectNull = new JsonObject();
            Gson gson = new Gson();
            return cadena != null ? gson.toJson(new JsonParser().parse(cadena).getAsJsonObject()) :
					gson.toJson(jsonObjectNull);

        } catch (IllegalStateException e) {
            return "";
        }

    }


    public List<String> setItems(Object var) {
        List<String> result = new ArrayList<>();
        if (var instanceof List) {
            for (int i = 0; i < ((List<?>) var).size(); i++) {
                Object item = ((List<?>) var).get(i);
                if (item instanceof String) {
                    result.add((String) item);
                }
            }
        }
        return result;
    }


    public String getOptionals(Map<String, String> optionals, String keyOptional) {
        return (optionals.get(keyOptional)) != null
                ? optionals.get(keyOptional)
                : null;
    }

    public String convertString(String value, Map<String, String> optionals, String keyOptional) {
        return (value != null)
                ? value
                : this.getOptionals(optionals, keyOptional);
    }


    public Date getReuseDate(LogFunctionalReuse logFunctionalReuse, String wichReuse) {
        Date result = null;
        if (logFunctionalReuse != null) {
            switch (wichReuse) {
                case LOGFIELD_DATEREQUEST:
                    result = logFunctionalReuse.getDateRequest();
                    break;
                case LOGFIELD_DATERESPONSE:
                    result = logFunctionalReuse.getDateResponse();
                    break;
                default:
                    result = null;
            }
        }
        return result;
    }

    public String getIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_IP) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public Map<String, String> getRequest(ContentCachingRequestWrapper request) {
        String bodyRequest = this.getAttribute(request, ConstantLog.LOGFIELD_REQUEST);
        String bodyRequestSave = (bodyRequest == null) ? new String(request.getContentAsByteArray()) : bodyRequest;

        JsonParser parser = new JsonParser();

        Map<String, String> opRes = new HashMap<>();

        try {

            JsonElement jsonElement = parser.parse(bodyRequestSave);
            JsonObject rootObject = jsonElement.getAsJsonObject();

            JsonObject childObject = rootObject.getAsJsonObject("data");

            opRes.put(ConstantLog.LOGFIELD_DOCUMENT_NUMBER,
                    childObject.get(ConstantLog.LOGFIELD_DOCUMENT_NUMBER) != null ?
                            childObject.get(ConstantLog.LOGFIELD_DOCUMENT_NUMBER).getAsString() : null);
            opRes.put(ConstantLog.LOGFIELD_DOCUMENT_TYPE,
                    childObject.get(ConstantLog.LOGFIELD_DOCUMENT_TYPE) != null ?
                            childObject.get(ConstantLog.LOGFIELD_DOCUMENT_TYPE).getAsString() : null);
            opRes.put(ConstantLog.LOGFIELD_ACQUISITION_ID,
                    childObject.get(ConstantLog.LOGFIELD_ACQUISITION_ID) != null ?
                            childObject.get(ConstantLog.LOGFIELD_ACQUISITION_ID).getAsString() : null);

        } catch (IllegalStateException ex) {
            LogInterceptorUtil.LOGGER_ADAPTER.info("Error convirtiendo el JSON");
        }
        return opRes;

    }

    public void nada() {
    	System.err.println("nada");
    }

}
