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
import co.com.bancolombia.model.expoquestion.DataResponse;
import co.com.bancolombia.model.expoquestion.ExpoQuestionError;
import co.com.bancolombia.model.expoquestion.ExpoQuestionErrorResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationRequest;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationTotalResponse;
import co.com.bancolombia.model.expoquestion.gateways.ValidateIdentificationRestRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID_CONTROL_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_CONTROL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPO_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPO_QUESTIONS_I;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_IDE_URI;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TITLE;

@Component
public class ValidateIdentificationRest implements ValidateIdentificationRestRepository {

    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGeneric tpcClientGeneric;

    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_IDENTITY, OPER_EXPO_QUESTIONS_I);

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CoreFunctionDate coreFD;

    @Value("${baseUrl.onPremise}")
    private String BASE_URL_VAL_IDE;

    public ValidateIdentificationRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremise = onPremiseI;
        this.tpcClientGeneric = clientGeneric;
        this.webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, BANK_HEADER)
                .defaultHeader(HttpHeaders.ACCEPT, BANK_HEADER)
                .defaultHeader(CLIENT_ID_KEY, this.onPremise.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, this.onPremise.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient()))).build();
    }

    public ValidateIdentificationTotalResponse errorValidate(Object object) {
        if (object.getClass().equals(ExpoQuestionErrorResponse.class)) {
            ExpoQuestionErrorResponse vIErrorResponse = (ExpoQuestionErrorResponse) object;
            ExpoQuestionError vIError = vIErrorResponse.getErrors().get(0);
            throw new ValidationException(new HashMap<String, List<ErrorField>>() {
                {
                    put(ERROR_CODE_SYSTEM, Collections.singletonList(ErrorField.builder().name(OPER_EXPO_QUESTIONS)
                            .complement(CODE_ERROR.concat(vIError.getCode()).concat(SPACE).concat(TITLE)
                                    .concat(vIError.getTitle()).concat(SPACE).concat(DETAIL)
                                    .concat(vIError.getDetail())).build()));
                }
            });
        } else {
            return ValidateIdentificationTotalResponse.builder().errors(null)
                    .validateIdentificationResponse((ValidateIdentificationResponse) object).build();
        }
    }

    @Override
    public ValidateIdentificationResponse getUserInfoIdentification(
            ValidateIdentificationRequest vIdentificationRequest, String messageId) {
        Date dateRequestApi = coreFD.getDatetime();
        Mono<ClientResponse> response = this.webClient.post().uri(BASE_URL_VAL_IDE + VALIDATE_IDE_URI)
                .body(BodyInserters.fromObject(vIdentificationRequest)).header(MESSAGE_ID_CONTROL_LIST, messageId)
                .exchange();
        Object validateIdentificationResponse = null;
        try {
            validateIdentificationResponse = response.flatMap(clientResponse -> {
                if (clientResponse.statusCode() == HttpStatus.OK) {
                    return clientResponse.bodyToMono(ValidateIdentificationResponse.class);
                }
                if (clientResponse.statusCode().is4xxClientError() || clientResponse.statusCode().is5xxServerError()) {
                    adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                    return clientResponse.bodyToMono(ExpoQuestionErrorResponse.class);
                } else {
                    return Mono.error(new RuntimeException());
                }
            }).block();
        } catch (CustomException e) {
            adapter.error(MESSAGE_NOT_CONTROL, e);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            ErrorField errorField = ErrorField.builder().name(OPER_EXPO_QUESTIONS).complement(e.toString()).build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_SYSTEM, eFieldList);
            throw new ValidationException(error);
        }
        ValidateIdentificationResponse identificationResponse = errorValidate(validateIdentificationResponse)
                .getValidateIdentificationResponse();
        Gson gson = new Gson();
        String requestString = gson.toJson(vIdentificationRequest, ValidateIdentificationRequest.class);
        String responseListString = gson.toJson(validateIdentificationResponse, ValidateIdentificationResponse.class);
        LogFunctionalReuse logFunctional = saveResponseForLog(requestString, responseListString,
                dateRequestApi);
        identificationResponse.setInfoReuseCommon(InfoReuseCommon.builder().dateRequestReuse(dateRequestApi)
                .dateResponseReuse(logFunctional.getDateResponse()).requestReuse(logFunctional.getRequestReuse())
                .responseReuse(logFunctional.getResponseReuse()).build());
        return identificationResponse;
    }

    public LogFunctionalReuse saveResponseForLog(String requestString, String responseListString, Date dateRequestApi) {
        Date dateResponseFinalApi = this.coreFD.getDatetime();
        JsonObject jsonObjectNull = new JsonObject();
        DataResponse dataRequest = DataResponse.builder()
                .identification(requestString != null ? new JsonParser().parse(requestString)
                        .getAsJsonObject() : jsonObjectNull)
                .build();
        DataResponse dataResponse = DataResponse.builder()
                .identification(responseListString != null ? new JsonParser().parse(responseListString)
                        .getAsJsonObject() : jsonObjectNull)
                .build();
        Gson gson = new Gson();
        return LogFunctionalReuse.builder().requestReuse(gson.toJson(dataRequest))
                .responseReuse(gson.toJson(dataResponse)).dateRequest(dateRequestApi)
                .dateResponse(dateResponseFinalApi).build();
    }
}
