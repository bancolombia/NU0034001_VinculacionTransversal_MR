package co.com.bancolombia.rest;

import co.com.bancolombia.common.interfaces.TpcClientGeneric;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.commons.secretsmodel.OnPremiseCredential;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.generatetoken.gateways.GenerateTokenRestRepository;
import co.com.bancolombia.model.generatetoken.reuserequest.GTRequest;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseOk;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseWithLog;
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

import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ACCEPT_TOKEN_GENERATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_ID_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_SECRET_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONTENT_TYPE_TOKEN_GENERATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENERATE_TOKEN_URI;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_CONTROL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_G_TOKEN_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CALL_TO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_TIMEOUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TIMEOUT;
import static co.com.bancolombia.commonsvnt.util.ConstantLog.LOGFIELD_REUSE_INFO;

@Component
public class GenerateTokenRest implements GenerateTokenRestRepository {
    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGeneric tpcClientGeneric;

    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_IDENTITY, OPER_G_TOKEN_OPERATION);

    @Value("${baseUrl.onPremise}")
    private String BASE_URL_GENERATE_TOKEN;

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private Exceptions exceptions;

    public GenerateTokenRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremise = onPremiseI;
        this.tpcClientGeneric = clientGeneric;
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_TOKEN_GENERATE)
                .defaultHeader(HttpHeaders.ACCEPT, ACCEPT_TOKEN_GENERATE)
                .defaultHeader(CLIENT_ID_KEY, this.onPremise.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, this.onPremise.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient()))).build();
    }

    @Override
    public GTResponseWithLog getToken(
            GTRequest generateTokenRequest, String messageId, Date dateRequest) {
        Mono<ClientResponse> response = webClient.post()
                .uri(BASE_URL_GENERATE_TOKEN+GENERATE_TOKEN_URI)
                .body(BodyInserters.fromObject(generateTokenRequest))
                .header(MESSAGE_ID, messageId)
                .exchange();
        GTResponseOk generateTokenResponse = null;
        try {
            generateTokenResponse = response.flatMap(clientResponse -> {
                switch (clientResponse.statusCode()) {
                    case OK:
                        return clientResponse.bodyToMono(GTResponseOk.class);
                    case SERVICE_UNAVAILABLE:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        return Mono.error(createError(clientResponse.statusCode(), ERROR));
                    case GATEWAY_TIMEOUT:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        return Mono.error(createError(clientResponse.statusCode(), TIMEOUT));
                    case INTERNAL_SERVER_ERROR:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        return Mono.error(new RuntimeException(
                                ERROR.toUpperCase() + SPACE + clientResponse.statusCode()));
                    default:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        if (clientResponse.statusCode().is4xxClientError() ||
                                clientResponse.statusCode().is5xxServerError()){
                            return Mono.error(new RuntimeException(
                                    CALL_TO+SPACE+GENERATE_TOKEN_URI+SPACE+ERROR+SPACE+clientResponse.statusCode()));
                        }else{
                            return Mono.error(new RuntimeException());
                        }
                }
            }).block();
        } catch (CustomException e) {
            adapter.error(MESSAGE_NOT_CONTROL, e);
            exceptions.createException(null, OPER_G_TOKEN_OPERATION, e.toString(), ERROR_CODE_TIMEOUT);
        }

        Gson gson = new Gson();
        String requestString = gson.toJson(generateTokenRequest, GTRequest.class);
        String responseString = gson.toJson(generateTokenResponse, GTResponseOk.class);

        Date dateResponse = new CoreFunctionDate().getDatetime();
        LogFunctionalReuse logFunctionalReuse = LogFunctionalReuse.builder()
                .requestReuse(requestString).responseReuse(responseString)
                .dateRequest(dateRequest).dateResponse(dateResponse).build();
        webRequest.setAttribute(LOGFIELD_REUSE_INFO, logFunctionalReuse, RequestAttributes.SCOPE_SESSION);

        return GTResponseWithLog.builder()
                .generateTokenResponse(generateTokenResponse)
                .infoReuseCommon(InfoReuseCommon.builder()
                        .requestReuse(requestString).dateRequestReuse(dateRequest)
                        .responseReuse(responseString).dateResponseReuse(dateResponse).build())
                .build();
    }

    public ValidationException createError(HttpStatus clientResponse, String type) {
        return exceptions.createValidationException(
                null, OPER_G_TOKEN_OPERATION, createComplementError(clientResponse, type), ERROR_CODE_TIMEOUT);
    }

    public String createComplementError(HttpStatus clientResponse, String type){
        return CALL_TO + SPACE + GENERATE_TOKEN_URI + SPACE + type + SPACE + clientResponse;
    }
}