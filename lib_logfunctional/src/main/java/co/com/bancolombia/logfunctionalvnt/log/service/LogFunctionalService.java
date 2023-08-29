package co.com.bancolombia.logfunctionalvnt.log.service;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.util.ConstantLog;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;
import co.com.bancolombia.logfunctionalvnt.log.model.gateways.ILogFunctionalRepository;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class LogFunctionalService implements ILogFunctionalService {

    private static final String APP = "NU0034001_VinculacionTransversal_LogFunctional";
    private static final LoggerAdapter LOGGER_ADAPTER = new LoggerAdapter(
            APP, "LogInterceptor", LogFunctionalService.class.toString());

    @Autowired
    private ILogFunctionalRepository logFunctionalRepository;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    public LogFunctionalService(ILogFunctionalRepository logFunctionalRepository, CoreFunctionDate coreFunctionDate) {
        this.logFunctionalRepository = logFunctionalRepository;
        this.coreFunctionDate = coreFunctionDate;
    }

    public String recorrerLista(JsonArray body, List<String> fieldsCanNotSave) {

        List<String> lista = new ArrayList<>();
        body.forEach(item -> {
            lista.add(forEachObjecJson(item.toString(), fieldsCanNotSave));
        });

        return Arrays.toString(lista.toArray());
    }

    public String forEachObjecJson(String body, List<String> fieldsCanNotSave) {
        JsonParser parser = new JsonParser();
        JsonObject obj = null;

        try {
            JsonElement element = parser.parse(body);
            obj = element.getAsJsonObject();
        } catch (IllegalStateException ex) {
            return body;
        }

        JsonObject objNew = new JsonObject();

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
        if (fieldsCanNotSave == null)
            fieldsCanNotSave = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            if (!fieldsCanNotSave.contains(key)) {
                if (entry.getValue().isJsonPrimitive()) {
                    objNew.addProperty(key, entry.getValue().getAsString());
                } else if (entry.getValue().isJsonObject()) {
                    JsonElement el = parser.parse(forEachObjecJson(entry.getValue().toString(), fieldsCanNotSave));
                    objNew.add(key, el);
                } else {

                    if (entry.getValue() != null && entry.getValue().isJsonArray()) {
                        JsonElement el = parser
                                .parse(this.recorrerLista(entry.getValue().getAsJsonArray(), fieldsCanNotSave));
                        objNew.add(key, el);

                    } else {
                        objNew.add(key, null);
                    }

                }

            }

        }
        return objNew.toString();
    }

    @Override
    public boolean save(LogFunctionalDTO logFunctional, Map<String, List<String>> fieldsCanNotSave) {
        if(fieldsCanNotSave != null) {
            List<String> notSaveResponse = fieldsCanNotSave.get(ConstantLog.LOGFIELD_RESPONSE);
            List<String> notSaveRequest = fieldsCanNotSave.get(ConstantLog.LOGFIELD_REQUEST);

            List<String> notSaveAllField = fieldsCanNotSave.get(ConstantLog.LOGFIELD_ALL_FIELD);

            LogFunctionalDTO logSave = LogFunctionalDTO.builder()
                    .request(this.forEachObjecJson(logFunctional.getRequest(), notSaveRequest))
                    .response(this.forEachObjecJson(logFunctional.getResponse(), notSaveResponse))
                    .manangment(logFunctional.getManangment()).acquisitionId(logFunctional.getAcquisitionId())
                    .documentType(logFunctional.getDocumentType()).documentNumber(logFunctional.getDocumentNumber())
                    .operation(logFunctional.getOperation()).dateInitOperation(logFunctional.getDateInitOperation())
                    .dateFinalOperation(coreFunctionDate.getDatetime())
                    .stateOperation(logFunctional.getStateOperation())
                    .stateAcquisition(logFunctional.getStateAcquisition())
                    .requestReuse(logFunctional.getRequestReuse() != null ?
                            this.forEachObjecJson(logFunctional.getRequestReuse(), notSaveAllField) : null)
                    .requestDateReuse(logFunctional.getRequestDateReuse())
                    .responseReuse(logFunctional.getResponseReuse() != null ?
                            this.forEachObjecJson(logFunctional.getResponseReuse(), notSaveAllField) : null)
                    .responseDateReuse(logFunctional.getResponseDateReuse())
                    .consecutive(1)
                    .ipAddress(logFunctional.getIpAddress())
                    .createdDate(coreFunctionDate.getDatetime())
                    .createdBy(logFunctional.getCreatedBy())
                    .timeZone(coreFunctionDate.getZoneId().toString())
                    .build();

            logFunctionalRepository.save(logSave);
            LOGGER_ADAPTER.info("************GRABO CON DATOS");
        }else {

            logFunctionalRepository.save(null);
            LOGGER_ADAPTER.info("************GRABO SIN DATOS");
        }
        return false;
    }
    

}
