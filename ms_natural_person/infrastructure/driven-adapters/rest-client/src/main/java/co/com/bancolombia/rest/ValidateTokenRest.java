package co.com.bancolombia.rest;

import co.com.bancolombia.common.interfaces.TpcClientGeneric;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.commons.secretsmodel.OnPremiseCredential;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponse;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponseError;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponseWithLog;
import co.com.bancolombia.model.validatetoken.gateways.ValidateTokenRestRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.BANK_HEADER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_ID_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_SECRET_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_INTERNAL_SERVER_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PIN_EXPIRED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_TOKEN_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STATUS_INTERNAL_SERVER_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_TOKEN_URI;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CALL_TO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PIN_EXPIRED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_TIMEOUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TIMEOUT;
import static co.com.bancolombia.commonsvnt.util.ConstantLog.LOGFIELD_REUSE_INFO;

@Component
public class ValidateTokenRest implements ValidateTokenRestRepository {
    private WebClient webClient;
    private OnPremiseCredential onPremiseValidateToken;
    private TpcClientGeneric tpcClientGeneric;

    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_VALIDATION, OPER_TOKEN_OPERATION);

    @Value("${baseUrl.onPremise}")
    private String BASE_URL_VALIDATE_TOKEN;

    @Autowired
    private WebRequest webRequest;

    public ValidateTokenRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremiseValidateToken = onPremiseI;

        this.tpcClientGeneric = clientGeneric;

        this.webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, BANK_HEADER)
                .defaultHeader(HttpHeaders.ACCEPT, BANK_HEADER)
                .defaultHeader(CLIENT_ID_KEY, this.onPremiseValidateToken.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, this.onPremiseValidateToken.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient()))).build();
    }

    @Override
    public ValidateTokenResponseWithLog getUserInfoFromValidateToken(ValidateTokenRequest validateTokenRequest,
                                                                     String messageId, Date dateRequest) {

        Mono<ClientResponse> response = webClient.post().uri(BASE_URL_VALIDATE_TOKEN + VALIDATE_TOKEN_URI)
                .body(BodyInserters.fromObject(validateTokenRequest)).header(MESSAGE_ID, messageId)
                .exchange();

        Object validateTokenResponse;

        try {
            validateTokenResponse = response.flatMap(clientResponse -> {
                switch (clientResponse.statusCode()) {
                    case OK:
                        return clientResponse.bodyToMono(ValidateTokenResponse.class);
                    case SERVICE_UNAVAILABLE:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        return Mono.error(new ValidationException(createError(ERROR_CODE_TIMEOUT,
                                CALL_TO + SPACE + VALIDATE_TOKEN_URI + SPACE + ERROR + SPACE +
                                        clientResponse.statusCode())));
                    case GATEWAY_TIMEOUT:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        return Mono.error(new ValidationException(createError(ERROR_CODE_TIMEOUT,
                                CALL_TO + SPACE + VALIDATE_TOKEN_URI + SPACE + TIMEOUT + SPACE +
                                        clientResponse.statusCode())));
                    case INTERNAL_SERVER_ERROR:
                        return clientResponse.bodyToMono(ValidateTokenResponseError.class);
                    default:
                        if (clientResponse.statusCode().is4xxClientError() ||
                                clientResponse.statusCode().is5xxServerError()) {
                            adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                            return Mono.error(new RuntimeException(CALL_TO + SPACE + VALIDATE_TOKEN_URI + SPACE + ERROR
                                    + SPACE + clientResponse.statusCode()));
                        } else {
                            adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                            return Mono.error(new RuntimeException());
                        }
                }
            }).block();
        } catch (CustomException e) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            ErrorField errorField = ErrorField.builder().name(OPER_TOKEN_OPERATION).complement(e.toString()).build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_TIMEOUT, eFieldList);
            adapter.error(ERROR+ERROR_CODE_TIMEOUT+MIDDLE_SCREEN+eFieldList);
            throw new ValidationException(error);
        }

        if(validateTokenResponse.getClass().equals(ValidateTokenResponseError.class)){

            ValidateTokenResponseError validateTokenResponseError = (ValidateTokenResponseError) validateTokenResponse;

            if (CODE_INTERNAL_SERVER_ERROR.equals(validateTokenResponseError.getErrors().get(0).getCode())) {
                adapter.error(MESSAGE_CODE.concat(CODE).concat(validateTokenResponseError.getErrors().get(0).getCode()+
                        validateTokenResponseError.getErrors().get(0).getDetail()));
                throw new ValidationException(createError(ERROR_CODE_TIMEOUT, CALL_TO + SPACE + VALIDATE_TOKEN_URI
                        + SPACE + TIMEOUT + SPACE + CODE + validateTokenResponseError.getErrors().get(0).getStatus()
                        + SPACE + validateTokenResponseError.getErrors().get(0).getDetail()));
            }

            if (CODE_PIN_EXPIRED.equals(validateTokenResponseError.getErrors().get(0).getCode())) {
                adapter.error(MESSAGE_CODE.concat(CODE).concat(validateTokenResponseError.getErrors().get(0).getCode()+
                        validateTokenResponseError.getErrors().get(0).getDetail()));
                throw new ValidationException(createError(ERROR_CODE_PIN_EXPIRED,
                        CALL_TO + SPACE + VALIDATE_TOKEN_URI + SPACE + ERROR + SPACE
                                + validateTokenResponseError.getErrors().get(0).getDetail()));
            }

            if (STATUS_INTERNAL_SERVER_ERROR.equals(validateTokenResponseError.getErrors().get(0).getStatus())) {
                ValidateTokenResponse validateTokenResponse1 = ValidateTokenResponse.builder()
                        .errors(validateTokenResponseError.getErrors())
                        .meta(validateTokenResponseError.getMeta())
                        .build();
                adapter.error(MESSAGE_CODE.concat(CODE).concat(validateTokenResponseError.getErrors().get(0).getStatus()
                        +validateTokenResponseError.getErrors().get(0).getDetail()));
                return ValidateTokenResponseWithLog.builder().validateTokenResponse(validateTokenResponse1).build();
            } else {
                adapter.error(ERROR_CODE_TIMEOUT+CALL_TO + SPACE + VALIDATE_TOKEN_URI
                        + SPACE + TIMEOUT + SPACE + CODE +validateTokenResponseError.getErrors().get(0).getDetail());
                throw new ValidationException(createError(ERROR_CODE_TIMEOUT, CALL_TO + SPACE + VALIDATE_TOKEN_URI
                        + SPACE + TIMEOUT + SPACE + CODE
                        + validateTokenResponseError.getErrors().get(0).getStatus()
                        + validateTokenResponseError.getErrors().get(0).getDetail()));
            }
        }

        Gson gson = new Gson();
        String requestString = gson.toJson(validateTokenRequest, ValidateTokenRequest.class);
        String responseString = gson.toJson(validateTokenResponse, ValidateTokenResponse.class);

        Date dateResponse = new CoreFunctionDate().getDatetime();
        LogFunctionalReuse logFunctionalReuse = LogFunctionalReuse.builder()
                .requestReuse(requestString)
                .responseReuse(responseString)
                .dateRequest(dateRequest)
                .dateResponse(dateResponse)
                .build();
        webRequest.setAttribute(LOGFIELD_REUSE_INFO, logFunctionalReuse, RequestAttributes.SCOPE_SESSION);
        return ValidateTokenResponseWithLog.builder()
                .validateTokenResponse((ValidateTokenResponse) validateTokenResponse)
                .infoReuseCommon(InfoReuseCommon.builder()
                        .requestReuse(requestString)
                        .dateRequestReuse(dateRequest)
                        .responseReuse(responseString)
                        .dateResponseReuse(dateResponse)
                        .build())
                .build();
    }

    public HashMap<String, List<ErrorField>> createError(String code, String complement) {
        return new HashMap<String, List<ErrorField>>() {{
            put(code, Collections.singletonList(
                    ErrorField.builder().name(OPER_TOKEN_OPERATION).complement(complement).build()));
        }};
    }
}
