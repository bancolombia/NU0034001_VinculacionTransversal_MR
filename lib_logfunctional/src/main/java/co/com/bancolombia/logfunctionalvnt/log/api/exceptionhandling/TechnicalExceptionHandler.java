package co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling;

import co.com.bancolombia.commonsvnt.api.i18n.Language;
import co.com.bancolombia.commonsvnt.api.model.util.Error;
import co.com.bancolombia.commonsvnt.api.model.util.ErrorItem;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.commonsvnt.common.exception.ValidateListException;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_RUN_TIME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_TITLE_RUN_TIME;

public class TechnicalExceptionHandler extends ConstructionDataLogFunctional {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleGeneralException(RuntimeException ex, WebRequest request) {
        this.constructionDataLogFunctional(request);
        HttpHeaders headers = new HttpHeaders();
        List<ErrorItem> errorItems = Stream
                .of(new ErrorItem(ERROR_TITLE_RUN_TIME, ERROR_CODE_RUN_TIME, ex.getMessage()))
                .collect(Collectors.toList());
        MetaRequest meta = (MetaRequest) request.getAttribute(META, RequestAttributes.SCOPE_REQUEST);
        LoggerAdapter adapter = new LoggerAdapter(MY_APP, meta.getService(), meta.getOperation());
        MetaResponse metaResponse = MetaResponse.fromMeta(meta);
        Error error = new Error(errorItems, metaResponse);
        adapter.fatal(ex.getMessage(), ex);
        return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidateListException.class)
    public ResponseEntity<Object> handleValidateListException(ValidateListException exList, WebRequest request) {
        this.constructionDataLogFunctional(request);
        final Locale locale = Language.SPANISH_LOCALE;
        HttpHeaders headers = new HttpHeaders();
        List<ErrorItem> errorItems = new ArrayList<>();
        exList.getListException().forEach(ex -> ex.getConstraintViolations().forEach(error -> {
            String code = ex.getCode();
            String title = messageSource.getMessage(code + ".title", null, locale);
            String detail = messageSource.getMessage(code + ".message",
                    new Object[]{error.getPropertyPath(), ex.getComplement(), ex.getNameList()}, locale);
            ErrorItem newErrorItem = new ErrorItem(title, code, detail);
            if (errorItems.stream().noneMatch(e -> e.equals(newErrorItem))) {
                errorItems.add(newErrorItem);
            }
        }));
        MetaRequest meta = (MetaRequest) request.getAttribute(META, RequestAttributes.SCOPE_REQUEST);
        MetaResponse metaResponse = MetaResponse.fromMeta(meta);
        Error error = new Error(errorItems, metaResponse);
        return handleExceptionInternal(exList, error, headers, HttpStatus.BAD_REQUEST, request);
    }

}