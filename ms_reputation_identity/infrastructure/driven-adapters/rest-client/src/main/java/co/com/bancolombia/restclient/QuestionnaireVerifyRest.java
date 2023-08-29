package co.com.bancolombia.restclient;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyError;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyErrorResponse;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyRequest;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyResponse;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyTotal;
import co.com.bancolombia.model.validatequestion.gateways.QuestionnaireVerifyRestRepository;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID_CONTROL_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_CONTROL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_VALIDATE_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.QUESTION_VERIFY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TITLE;
import static co.com.bancolombia.commonsvnt.util.ConstantLog.LOGFIELD_REUSE_INFO;

@Component
public class QuestionnaireVerifyRest implements QuestionnaireVerifyRestRepository {

    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGeneric tpcClientGeneric;

    private final LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_IDENTITY, OPER_VALIDATE_QUESTIONS);

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CoreFunctionDate coreFunctionDate;


    @Value("${baseUrl.onPremise}")
    private String BASE_URL_QUESTIONNAIRE;

    public QuestionnaireVerifyRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremise = onPremiseI;

        this.tpcClientGeneric = clientGeneric;

        this.webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, BANK_HEADER)
                .defaultHeader(HttpHeaders.ACCEPT, BANK_HEADER)
                .defaultHeader(CLIENT_ID_KEY, this.onPremise.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, this.onPremise.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient()))).build();
    }

    public InfoReuseCommon saveResponseForLog(QuestionnaireVerifyRequest request, Date dateRequestApi,
                                              QuestionnaireVerifyTotal questionnaireVerifyTotal) {
        Gson gson = new Gson();
        String requestString = gson.toJson(request, QuestionnaireVerifyRequest.class);
        String responseString = gson.toJson(questionnaireVerifyTotal, QuestionnaireVerifyTotal.class);
        Date dateResponseFinalApi = this.coreFunctionDate.getDatetime();
        return InfoReuseCommon.builder().dateRequestReuse(dateRequestApi)
                .dateResponseReuse(dateResponseFinalApi).requestReuse(requestString)
                .responseReuse(responseString).build();
    }

    public QuestionnaireVerifyTotal validateErrors(Object object, QuestionnaireVerifyRequest request,
                                                   Date dateRequestApi) {
        if (object.getClass().equals(QuestionnaireVerifyErrorResponse.class)) {
            QuestionnaireVerifyErrorResponse vIErrorResponse = (QuestionnaireVerifyErrorResponse) object;
            QuestionnaireVerifyError vIError = vIErrorResponse.getErrors().get(0);
            webRequest.setAttribute(LOGFIELD_REUSE_INFO, saveResponseForLog(request, dateRequestApi,
                            QuestionnaireVerifyTotal.builder().errors(vIErrorResponse).build()),
                    RequestAttributes.SCOPE_SESSION);
            throw new ValidationException(new HashMap<String, List<ErrorField>>() {
                {
                    put(ERROR_CODE_SYSTEM, Collections.singletonList(ErrorField.builder().name(OPER_VALIDATE_QUESTIONS)
                            .complement(CODE_ERROR.concat(vIError.getCode()).concat(SPACE).concat(TITLE)
                                    .concat(vIError.getTitle()).concat(SPACE).concat(DETAIL)
                                    .concat(vIError.getDetail())).build()));
                }
            });
        } else {
            QuestionnaireVerifyResponse questionnaireVerifyResponse = (QuestionnaireVerifyResponse) object;
            questionnaireVerifyResponse.setInfoReuseCommon(saveResponseForLog(request, dateRequestApi,
                    QuestionnaireVerifyTotal.builder().questionnaireVerifyResponse(questionnaireVerifyResponse)
                            .build()));
            return QuestionnaireVerifyTotal.builder().questionnaireVerifyResponse(questionnaireVerifyResponse).build();
        }
    }


    @Override
    public QuestionnaireVerifyResponse getUserQuestionnaireVerify(QuestionnaireVerifyRequest request, String messageId,
                                                                  Date dateRequestApi) {
        Mono<ClientResponse> response = this.webClient.post().uri(BASE_URL_QUESTIONNAIRE + QUESTION_VERIFY)
                .body(BodyInserters.fromObject(request)).header(MESSAGE_ID_CONTROL_LIST, messageId)
                .exchange();
        Object questionnaireResponse = null;
        try {
            questionnaireResponse = response.flatMap(clientResponse -> {
                if (clientResponse.statusCode() == HttpStatus.OK) {
                    return clientResponse.bodyToMono(QuestionnaireVerifyResponse.class);
                }
                if (clientResponse.statusCode().is4xxClientError()
                        || clientResponse.statusCode().is5xxServerError()) {
                    adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                    return clientResponse.bodyToMono(QuestionnaireVerifyErrorResponse.class);
                } else {
                    return Mono.error(new RuntimeException());
                }
            }).block();
        } catch (CustomException e) {
            adapter.error(MESSAGE_NOT_CONTROL, e);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            ErrorField errorField = ErrorField.builder().name(OPER_VALIDATE_QUESTIONS).complement(e.toString()).build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_SYSTEM, eFieldList);
            throw new ValidationException(error);
        }
        QuestionnaireVerifyTotal questionnaireVerifyTotal = validateErrors(questionnaireResponse, request,
                dateRequestApi);
        return questionnaireVerifyTotal.getQuestionnaireVerifyResponse();
    }
}
