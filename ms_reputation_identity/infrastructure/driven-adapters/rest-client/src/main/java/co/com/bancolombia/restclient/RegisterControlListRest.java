package co.com.bancolombia.restclient;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.markcustomer.MarkCustomerResponseWithLog;
import co.com.bancolombia.model.markcustomer.RegisterControlListError;
import co.com.bancolombia.model.markcustomer.RegisterControlListErrorResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListRequest;
import co.com.bancolombia.model.markcustomer.RegisterControlListResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListTotal;
import co.com.bancolombia.model.markcustomer.RegisterGenericErrorResponse;
import co.com.bancolombia.model.markcustomer.gateways.RegisterControlListRestRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MARK_CUSTOMER_URI;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID_CONTROL_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_CONTROL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_MARK_CUSTOMER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VALIDATION_ERRS;
import static co.com.bancolombia.commonsvnt.util.ConstantLog.LOGFIELD_REUSE_INFO;

@Component
public class RegisterControlListRest implements RegisterControlListRestRepository {

    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGeneric tpcClientGeneric;
    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_VALIDATION, OPER_MARK_CUSTOMER);

    @Value("${baseUrl.onPremise}")
    private String BASE_URL_CONTROL;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private WebRequest webRequest;

    public RegisterControlListRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremise = onPremiseI;
        this.tpcClientGeneric = clientGeneric;

        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, BANK_HEADER)
                .defaultHeader(HttpHeaders.ACCEPT, BANK_HEADER)
                .defaultHeader(CLIENT_ID_KEY, onPremise.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, onPremise.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient())))
                .build();
    }

    public RegisterControlListTotal validateErrors(Object object, RegisterControlListRequest request,
                                                   Date dateRequestApi) {
        if (object.getClass().equals(RegisterControlListErrorResponse.class)) {
            RegisterControlListTotal vIErrorResponse = RegisterControlListTotal.builder()
                    .errors((RegisterControlListErrorResponse) object).build();
            saveResponseForLog(request, dateRequestApi, vIErrorResponse);
            return vIErrorResponse;
        } else if (object.getClass().equals(RegisterGenericErrorResponse.class)) {
            RegisterGenericErrorResponse genericErrorResponse = (RegisterGenericErrorResponse) object;
            List<RegisterControlListError> errors = Collections.singletonList(RegisterControlListError.builder()
                    .code(VALIDATION_ERRS + genericErrorResponse.getHttpCode())
                    .title(genericErrorResponse.getHttpMessage()).detail(genericErrorResponse.getMoreInformation())
                    .status(genericErrorResponse.getHttpCode()).build());
            RegisterControlListTotal vIErrorResponse = RegisterControlListTotal.builder()
                    .errors(RegisterControlListErrorResponse.builder().errors(errors).build()).build();
            saveResponseForLog(request, dateRequestApi, vIErrorResponse);
            return vIErrorResponse;
        } else {
            return RegisterControlListTotal.builder().response((RegisterControlListResponse) object).build();
        }
    }

    public LogFunctionalReuse saveResponseForLog(RegisterControlListRequest request, Date dateRequestApi,
                                                 RegisterControlListTotal questionnaireVerifyTotal) {
        Gson gson = new Gson();
        String requestString = gson.toJson(request, RegisterControlListRequest.class);
        String responseString = gson.toJson(questionnaireVerifyTotal, RegisterControlListTotal.class);

        Date dateResponseFinalApi = this.coreFunctionDate.getDatetime();
        LogFunctionalReuse logFunctionalReuse = LogFunctionalReuse.builder()
                .requestReuse(requestString)
                .responseReuse(responseString)
                .dateRequest(dateRequestApi)
                .dateResponse(dateResponseFinalApi)
                .build();
        webRequest.setAttribute(LOGFIELD_REUSE_INFO, logFunctionalReuse, RequestAttributes.SCOPE_SESSION);
        return logFunctionalReuse;
    }

    @Override
    public MarkCustomerResponseWithLog getRegisterControl(RegisterControlListRequest request, String messageId,
                                                          Date dateRequestApi) {
        Date requestDateReuse = coreFunctionDate.getDatetime();
        Mono<ClientResponse> response = webClient.post().uri(BASE_URL_CONTROL + MARK_CUSTOMER_URI)
                .body(BodyInserters.fromObject(request)).header(MESSAGE_ID_CONTROL_LIST, messageId).exchange();

        Object controlResponse = null;
        try {
            controlResponse = response.flatMap(clientResponse -> {
                if (clientResponse.statusCode() == HttpStatus.OK) {
                    return clientResponse.bodyToMono(RegisterControlListResponse.class);
                }
                if (clientResponse.statusCode() == HttpStatus.UNAUTHORIZED
                        || clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                    adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                    return clientResponse.bodyToMono(RegisterGenericErrorResponse.class);
                }
                if (clientResponse.statusCode().is4xxClientError()
                        || clientResponse.statusCode().is5xxServerError()) {
                    adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                    return clientResponse.bodyToMono(RegisterControlListErrorResponse.class);
                } else {
                    return Mono.error(new RuntimeException());
                }
            }).block();
        } catch (CustomException e) {
            adapter.error(MESSAGE_NOT_CONTROL, e);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            ErrorField errorField = ErrorField.builder().name(OPER_MARK_CUSTOMER).complement(e.toString()).build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_SYSTEM, eFieldList);
            throw new ValidationException(error);
        }
        RegisterControlListTotal registerControlListTotal = validateErrors(controlResponse, request,
                dateRequestApi);
        LogFunctionalReuse logFunctionalReuse = saveResponseForLog(request, dateRequestApi, registerControlListTotal);
        return MarkCustomerResponseWithLog.builder()
                .controlListResponse(registerControlListTotal)
                .infoReuseCommon(InfoReuseCommon.builder()
                        .requestReuse(logFunctionalReuse.getRequestReuse())
                        .dateRequestReuse(dateRequestApi)
                        .responseReuse(logFunctionalReuse.getResponseReuse())
                        .dateResponseReuse(logFunctionalReuse.getDateResponse())
                        .build()).build();
    }
}
