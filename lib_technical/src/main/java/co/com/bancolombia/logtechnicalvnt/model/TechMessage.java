package co.com.bancolombia.logtechnicalvnt.model;

import java.util.List;

public interface TechMessage<T> {
	
	/**
     * Identifier of the global transaction, is a main attribute to logger output.
     *
     * @param transactionId
     */
    void setTransactionId(String transactionId);

    /**
     * Application Unique identifier, is a main attribute to logger output.
     *
     * @param actionName
     */
    public void setActionName(String actionName);

    /**
     * Name of the service in execution, is a main attribute to logger output.
     *
     * @param serviceName
     */
    public void setServiceName(String serviceName);

    /**
     * For example java class name, is a main attribute to logger output.
     *
     * @param componentName
     */
    public void setComponentName(String componentName);

    /**
     * Value allows to the developer to have a correlation between different applications, is a main attribute
     * to logger output.
     *
     * @param tagList
     */
    public void setTagList(List<String> tagList);

    /**
     * Customized info, is a main attribute to logger output.
     *
     * @param message
     */
    public void setMessage(T message);

    public void setAppName(String name);

    public boolean hasAppName();

}
