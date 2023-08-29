package co.com.bancolombia.restclient;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.controllist.ControlListRequest;
import co.com.bancolombia.model.controllist.ControlListResponse;
import co.com.bancolombia.model.controllist.DataControlList;
import co.com.bancolombia.model.controllist.StatusControlList;
import co.com.bancolombia.model.controllist.gateways.ControlListRestRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.BASIC_CONTROL_LIST_URI;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_ID_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_SECRET_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_NOT_FOUND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID_CONTROL_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_CONTROL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NAME_CONTROL_LIST_OPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CALL_TO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_TIMEOUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TIMEOUT;

@Component
public class ControlListRest implements ControlListRestRepository {

    @Value("${baseUrl.onPremise}")
    private String BASE_URL_CONTROL_LIST;

    private WebClient webClient;
    private OnPremiseCredential onPremiseControlList;
    private TpcClientGeneric tpcClientGeneric;
    private LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_VALIDATION, NAME_CONTROL_LIST_OPE);

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    public ControlListRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremiseControlList = onPremiseI;

        this.tpcClientGeneric = clientGeneric;

        this.webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, BANK_HEADER)
                .defaultHeader(HttpHeaders.ACCEPT, BANK_HEADER)
                .defaultHeader(CLIENT_ID_KEY, this.onPremiseControlList.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, this.onPremiseControlList.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient())))
                .build();
    }

    @Override
    public ControlListResponse getUserInfoFromControlList(ControlListRequest controlListRequest, String messageId,
                                                          Date dateRequestApi) {
        Mono<ClientResponse> response = this.webClient.post().uri(BASE_URL_CONTROL_LIST + BASIC_CONTROL_LIST_URI)
                .body(BodyInserters.fromObject(controlListRequest)).header(MESSAGE_ID_CONTROL_LIST, messageId)
                .exchange();

        ControlListResponse controlListResponse = null;
        try {
            controlListResponse = response.flatMap(clientResponse -> {
                switch (clientResponse.statusCode()) {
                    case NOT_FOUND:
                        StatusControlList statusControlList = StatusControlList.builder().code(CODE_NOT_FOUND)
                                .build();
                        DataControlList dataControlList = DataControlList.builder().status(statusControlList).build();

                        List<DataControlList> dataList = new ArrayList<>();
                        dataList.add(dataControlList);

                        ControlListResponse responseError = ControlListResponse.builder().data(dataList).build();
                        return Mono.just(responseError);
                    case OK:
                        return clientResponse.bodyToMono(ControlListResponse.class);
                    case SERVICE_UNAVAILABLE:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        return Mono.error(new ValidationException(new HashMap<String, List<ErrorField>>() {
                            {
                                put(ERROR_CODE_TIMEOUT, Collections.singletonList(ErrorField.builder()
                                        .name(NAME_CONTROL_LIST_OPE).complement(CALL_TO + SPACE +
                                                BASIC_CONTROL_LIST_URI + SPACE + ERROR + SPACE +
                                                clientResponse.statusCode()).build()));
                            }
                        }));
                    case GATEWAY_TIMEOUT:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        return Mono.error(new ValidationException(new HashMap<String, List<ErrorField>>() {
                            {
                                put(ERROR_CODE_TIMEOUT, Collections.singletonList(ErrorField.builder()
                                        .name(NAME_CONTROL_LIST_OPE).complement(CALL_TO + SPACE +
                                                BASIC_CONTROL_LIST_URI + SPACE + TIMEOUT + SPACE +
                                                clientResponse.statusCode()).build()));
                            }
                        }));
                    case INTERNAL_SERVER_ERROR:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        return Mono.error(new RuntimeException(ERROR.toUpperCase() + SPACE + clientResponse
                                .statusCode()));
                    default:
                        adapter.error(MESSAGE_CODE.concat(CODE).concat(clientResponse.statusCode().toString()));
                        if (clientResponse.statusCode().is4xxClientError()
                                || clientResponse.statusCode().is5xxServerError()) {

                            return Mono.error(new RuntimeException(CALL_TO + SPACE + BASIC_CONTROL_LIST_URI + SPACE
                                    + ERROR + SPACE + clientResponse.statusCode()));
                        } else {

                            return Mono.error(new RuntimeException());
                        }
                }
            }).block();
        } catch (CustomException e) {
            adapter.error(MESSAGE_NOT_CONTROL, e);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            ErrorField errorField = ErrorField.builder().name(NAME_CONTROL_LIST_OPE).complement(e.toString()).build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_TIMEOUT, eFieldList);
            throw new ValidationException(error);
        }

        Gson gson = new Gson();
        String requestString = gson.toJson(controlListRequest, ControlListRequest.class);
        String responseString = gson.toJson(controlListResponse, ControlListResponse.class);
        Date dateResponseFinalApi = this.coreFunctionDate.getDatetime();
        controlListResponse.setInfoReuseCommon(InfoReuseCommon.builder().dateRequestReuse(dateRequestApi)
                .dateResponseReuse(dateResponseFinalApi).requestReuse(requestString)
                .responseReuse(responseString).build());
        return controlListResponse;
    }
}
