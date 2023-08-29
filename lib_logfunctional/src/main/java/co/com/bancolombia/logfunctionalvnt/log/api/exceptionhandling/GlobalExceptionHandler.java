package co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling;


import co.com.bancolombia.commonsvnt.api.i18n.Language;
import co.com.bancolombia.commonsvnt.api.model.util.Error;
import co.com.bancolombia.commonsvnt.api.model.util.ErrorItem;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.commonsvnt.api.model.util.Request;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANDATORY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANDATORY_OUT;

@ControllerAdvice
public class GlobalExceptionHandler extends TechnicalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        this.constructionDataLogFunctional(request);
        Request requestObject = (Request) ex.getBindingResult().getTarget();
        List<ErrorItem> errorItems = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Object rejectedValue = error.getRejectedValue();
            String code = ERROR_CODE_MANDATORY;
            List<ErrorField> errorFields = Collections
                    .singletonList(ErrorField.builder().name(error.getField()).build());
            if (rejectedValue == null) {
                code = ERROR_CODE_MANDATORY_OUT;
            }
            this.translateValidationException(code, errorFields);
            ErrorItem newErrorItem = new ErrorItem(errorFields.get(0).getTitle(), code,
                    errorFields.get(0).getMessage());
            if (errorItems.stream().noneMatch(e -> e.equals(newErrorItem))) {
                errorItems.add(newErrorItem);
            }
        }

        MetaResponse metaResponse = MetaResponse.fromMeta(requestObject != null ? requestObject.getMeta() : null);
        Error error = new Error(errorItems, metaResponse);
        return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleTechnicalException(ValidationException ex, WebRequest request) {
        this.constructionDataLogFunctional(request);

        HttpHeaders headers = new HttpHeaders();
        List<ErrorItem> errorItems = new ArrayList<>();
        ex.getErrorFieldsCodes().forEach((k, v) -> {
            this.translateValidationException(k, v);
            v.forEach(errorField -> errorItems.add(new ErrorItem(errorField.getTitle(), k, errorField.getMessage())));
        });

        MetaRequest meta = (MetaRequest) request.getAttribute(META, RequestAttributes.SCOPE_REQUEST);
        MetaResponse metaResponse = MetaResponse.fromMeta(meta);
        Error error = new Error(errorItems, metaResponse);
        return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    public void translateValidationException(final String code, final List<ErrorField> errorFields) {
        final Locale locale = Language.SPANISH_LOCALE;
        if (errorFields != null) {
            errorFields.forEach(errorItem -> {
                if (errorItem.getComplement() == null) {
                    errorItem.setComplement(EMPTY);
                }
                List<String> otherArray = Arrays.asList(errorItem.getComplement().split(","));
                Object[] obj = new Object[] { errorItem.getName(), errorItem.getComplement(), errorItem.getNameList(),
                        new ArrayList<>(otherArray) };
                ArrayList<Object> newObj = new ArrayList<>(Arrays.asList(obj));
                newObj.addAll(new ArrayList<>(otherArray));
                String fieldMessage = messageSource.getMessage(code + ".message", newObj.toArray(), locale);
                String fieldTitle = messageSource.getMessage(code + ".title", null, locale);
                errorItem.setMessage(fieldMessage);
                errorItem.setTitle(fieldTitle);
            });
        }
    }

}