package co.com.bancolombia.restclient;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.validatesession.ValidateSessionRequest;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;
import co.com.bancolombia.model.validatesession.gateways.ValidateSessionRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CHANNEL_VNT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_CONTROL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_START_UPDATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_ACCEPT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_BODY_GRANT_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_BODY_PASSWORD;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_BODY_SCOPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_BODY_USERNAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_CLIENT_IP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_CONTENT_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_ERROR_API;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_HEADER_AUTHORIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_HEADER_CLIENT_IP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_HEADER_CONSUMER_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_HEADER_SESSION_STATUS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_SESSION_STATUS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_TOKEN_URI;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;

@Component
public class ValidateSessionRest implements ValidateSessionRestRepository {

    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGeneric tpcClientGeneric;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, OPER_START_UPDATE);


    @Value("${baseUrl.onPremise}")
    private String BASE_URL_SESSION;

    @Autowired
    private WebRequest webRequest;

    public ValidateSessionRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremise = onPremiseI;
        this.tpcClientGeneric = clientGeneric;

        String client = onPremise.getClientId() + ":" + onPremise.getClientSecret();
        String authorization = "Basic " + Base64.getEncoder().encodeToString(client.getBytes());

        String consumer = SYSTEM_VTN + "_" + CHANNEL_VNT;

        this.webClient = WebClient.builder()
                .defaultHeader(SI_HEADER_CLIENT_IP, SI_CLIENT_IP)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, SI_CONTENT_TYPE)
                .defaultHeader(HttpHeaders.ACCEPT, SI_ACCEPT)
                .defaultHeader(SI_HEADER_AUTHORIZATION, authorization)
                .defaultHeader(SI_HEADER_CONSUMER_ID, consumer)
                .defaultHeader(SI_HEADER_SESSION_STATUS, SI_SESSION_STATUS)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient())))
                .build();
    }

    @Override
    public ValidateSessionResponse getTokenValidation(ValidateSessionRequest validateSessionRequest, String operation) {
        MultiValueMap<String, String> formData = getFormData(validateSessionRequest);
        Mono<ClientResponse> response = webClient.post()
                .uri(BASE_URL_SESSION + SI_TOKEN_URI)
                .body(BodyInserters.fromFormData(formData))
                .exchange();

        ValidateSessionResponse validateSessionResponse = null;

        try {
            validateSessionResponse = response.flatMap(clientResponse -> {
                switch (clientResponse.statusCode()) {
                    case OK:
                        return clientResponse.bodyToMono(ValidateSessionResponse.class);
                    case BAD_REQUEST:
                    case UNAUTHORIZED:
                        String complement = SI_ERROR_API + SPACE + SI_TOKEN_URI + SPACE + MIDDLE_SCREEN + SPACE
                                + clientResponse.statusCode();
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        HashMap<String, List<ErrorField>> error = getError(operation, complement);
                        return Mono.error(new ValidationException(error));
                    default:
                        return Mono.error(new RuntimeException("Error " + clientResponse.statusCode()));
                }
            }).block();
        } catch (CustomException e) {
            adapter.error(MESSAGE_NOT_CONTROL, e);
            HashMap<String, List<ErrorField>> error = getError(operation, e.toString());
            throw new ValidationException(error);
        }
        return validateSessionResponse;
    }

    private MultiValueMap<String, String> getFormData(ValidateSessionRequest validateSessionRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add(SI_BODY_GRANT_TYPE, validateSessionRequest.getGrantType());
        formData.add(SI_BODY_USERNAME, validateSessionRequest.getUsername());
        formData.add(SI_BODY_PASSWORD, validateSessionRequest.getPassword());
        formData.add(SI_BODY_SCOPE, validateSessionRequest.getScope());

        return formData;
    }

    private HashMap<String, List<ErrorField>> getError(String name, String complement) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();

        ErrorField errorField = ErrorField.builder().name(name).complement(complement).build();
        List<ErrorField> eFieldList = new ArrayList<>();
        eFieldList.add(errorField);

        error.put(ERROR_CODE_SYSTEM, eFieldList);

        return error;
    }
}
