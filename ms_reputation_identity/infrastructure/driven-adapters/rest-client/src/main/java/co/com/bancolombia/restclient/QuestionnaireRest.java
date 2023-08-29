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
import co.com.bancolombia.model.expoquestion.QuestionnaireRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.QuestionnaireTotalResponse;
import co.com.bancolombia.model.expoquestion.gateways.QuestionnaireRestRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPO_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPO_QUESTIONS_Q;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.QUESTION_URI;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TITLE;

@Component
public class QuestionnaireRest implements QuestionnaireRestRepository {

    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGeneric tpcClientGeneric;

    private final LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_IDENTITY, OPER_EXPO_QUESTIONS_Q);

    @Autowired
    private CoreFunctionDate coreFD;

    @Value("${baseUrl.onPremise}")
    private String BASE_URL_QUESTIONNAIRE;

    public QuestionnaireRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremise = onPremiseI;
        this.tpcClientGeneric = clientGeneric;
        this.webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, BANK_HEADER)
                .defaultHeader(HttpHeaders.ACCEPT, BANK_HEADER)
                .defaultHeader(CLIENT_ID_KEY, this.onPremise.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, this.onPremise.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient()))).build();
    }

    @Override
    public QuestionnaireResponse getUserInfoQuestionnaire(QuestionnaireRequest qRequest, String messageId,
                                                               Date dateRequestApi, String vIRequest,
                                                               String vIResponse) {
        Mono<ClientResponse> response = this.webClient.post().uri(BASE_URL_QUESTIONNAIRE + QUESTION_URI)
                .body(BodyInserters.fromObject(qRequest)).header(MESSAGE_ID_CONTROL_LIST, messageId)
                .exchange();
        Object questionnaireResponse = null;
        try {
            questionnaireResponse = response.flatMap(clientResponse -> {
                if (clientResponse.statusCode() == HttpStatus.OK) {
                    return clientResponse.bodyToMono(QuestionnaireResponse.class);
                }
                if (clientResponse.statusCode().is4xxClientError() || clientResponse.statusCode().is5xxServerError()) {
                    adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                    return clientResponse.bodyToMono(ExpoQuestionErrorResponse.class);
                } else {
                    return Mono.error(new RuntimeException());
                }
            }).block();
        } catch (CustomException e) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            ErrorField errorField = ErrorField.builder().name(OPER_EXPO_QUESTIONS).complement(e.toString()).build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_SYSTEM, eFieldList);
            throw new ValidationException(error);
        }
        QuestionnaireResponse questionnaireRes = errorValidate(questionnaireResponse).getQuestionnaireResponse();
        Gson gson = new Gson();
        String requestString = gson.toJson(qRequest, QuestionnaireRequest.class);
        String responseListString = gson.toJson(questionnaireRes, QuestionnaireResponse.class);
        LogFunctionalReuse logFunctional = saveResponseForLog(requestString, responseListString, dateRequestApi,
                vIRequest, vIResponse);
        questionnaireRes.setInfoReuseCommon(InfoReuseCommon.builder().dateRequestReuse(dateRequestApi)
                .dateResponseReuse(logFunctional.getDateResponse()).requestReuse(logFunctional.getRequestReuse())
                .responseReuse(logFunctional.getResponseReuse()).build());
        return questionnaireRes;
    }

    public QuestionnaireTotalResponse errorValidate(Object object) {
        if (object.getClass().equals(ExpoQuestionErrorResponse.class)) {
            ExpoQuestionErrorResponse eqErrorResponse = (ExpoQuestionErrorResponse) object;
            ExpoQuestionError eqError = eqErrorResponse.getErrors().get(0);
            throw new ValidationException(new HashMap<String, List<ErrorField>>() {
                {
                    put(ERROR_CODE_SYSTEM, Collections.singletonList(ErrorField.builder()
                            .name(OPER_EXPO_QUESTIONS).complement(CODE_ERROR.concat(eqError.getCode())
                                    .concat(SPACE).concat(TITLE).concat(eqError.getTitle()).concat(SPACE).concat(DETAIL)
                                    .concat(eqError.getDetail())).build()));
                }
            });
        }
        return QuestionnaireTotalResponse.builder().questionnaireResponse((QuestionnaireResponse) object).build();
    }


    public LogFunctionalReuse saveResponseForLog(String requestString, String responseListString, Date dateRequestApi,
                                                 String vIRequest, String vIResponse) {
        Date dateResponseFinalApi = this.coreFD.getDatetime();
        JsonObject jsonObjectNull = new JsonObject();
        DataResponse dataRequest = DataResponse.builder()
                .identification(vIRequest != null ? new JsonParser().parse(vIRequest)
                        .getAsJsonObject() : jsonObjectNull)
                .questionnaire(requestString != null ? new JsonParser().parse(requestString)
                        .getAsJsonObject() : jsonObjectNull)
                .build();
        DataResponse dataResponse = DataResponse.builder()
                .identification(vIResponse != null ? new JsonParser().parse(vIResponse)
                        .getAsJsonObject() : jsonObjectNull)
                .questionnaire(responseListString != null ? new JsonParser().parse(responseListString)
                        .getAsJsonObject() : jsonObjectNull)
                .build();
        Gson gson = new Gson();
        return LogFunctionalReuse.builder().requestReuse(gson.toJson(dataRequest))
                .responseReuse(gson.toJson(dataResponse)).dateRequest(dateRequestApi)
                .dateResponse(dateResponseFinalApi).build();
    }
}
