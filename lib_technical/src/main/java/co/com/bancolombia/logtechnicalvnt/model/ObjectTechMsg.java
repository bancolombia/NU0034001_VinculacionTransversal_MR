package co.com.bancolombia.logtechnicalvnt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ObjectTechMsg<T> implements TechMessage<T>{
	private String appName;
    private String transactionId;
    private String actionName;
    private String serviceName;
    private String componentName;
    private List<String> tagList;
    private T message;

    public ObjectTechMsg(String transactionId, String actionName, String serviceName, String componentName,
                         List<String> tagList, T message) {
        this.transactionId = transactionId;
        this.actionName = actionName;
        this.serviceName = serviceName;
        this.componentName = componentName;
        this.tagList = tagList;
        this.message = message;
    }


    @Override
    public boolean hasAppName() {
        return this.appName != null;
    }
}
