package co.com.bancolombia.commonsvnt.usecase.util;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exceptions {
    private final Map<String, List<ErrorField>> hashMapError = new HashMap<>();
    private final List<ErrorField> errorFields = new ArrayList<>();

    public ValidationException createValidationException(
            Map<String, String> hashMap, String name, String complement, String codeException){
        clearData();
        if (name!=null){
            createErrorField(name, complement);
        }else{
            hashMap.forEach(this::createErrorField);
        }
        hashMapError.put(codeException, errorFields);
        return new ValidationException(hashMapError);
    }

    public void createErrorField(String name, String complement){
        ErrorField errorField = ErrorField.builder().name(name).complement(complement).build();
        errorFields.add(errorField);
    }

    public void createException(
            Map<String, String> hashMap, String name, String complement, String codeException) {
        throw createValidationException(hashMap, name, complement, codeException);
    }

    public void clearData(){
        hashMapError.clear();
        errorFields.clear();
    }
}
