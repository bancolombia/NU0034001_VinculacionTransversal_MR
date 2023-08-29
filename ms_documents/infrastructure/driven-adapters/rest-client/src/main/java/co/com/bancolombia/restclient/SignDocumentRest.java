package co.com.bancolombia.restclient;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.TpcClientGenericSign;
import co.com.bancolombia.model.signdocument.SDRequest;
import co.com.bancolombia.model.signdocument.SDRequestTotal;
import co.com.bancolombia.model.signdocument.SDResponseError;
import co.com.bancolombia.model.signdocument.SDResponseTotal;
import co.com.bancolombia.model.signdocument.SDResponseTotalWithLog;
import co.com.bancolombia.model.signdocument.gateways.SignDocumentRestRepository;
import co.com.bancolombia.signdocument.SignDocumentResponseUseCase;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_ID_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_SECRET_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_SIGN_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_TIMEOUT;
import static co.com.bancolombia.util.constants.Constants.ES_URI;
import static co.com.bancolombia.util.constants.Constants.ES_X_USERNAME;
import static co.com.bancolombia.util.constants.Constants.ES_X_USERTOKEN;
import static co.com.bancolombia.util.constants.Constants.ES_X_USERTOKEN_VALUE;

@Component
public class SignDocumentRest implements SignDocumentRestRepository {

    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGenericSign tpcClientGeneric;

    @Value("${signDocument.baseUrl.onPremise}")
    private String BASE_URL;

    @Value("${sign.rest.clientId}")
    public String ES_CLIENT_ID;

    @Value("${sign.rest.clientSecret}")
    public String ES_CLIENT_SECRET;

    @Value("${signDocument.rest.userName}")
    private String USER_NAME;

    @Autowired
    private Exceptions exceptions;

    @Autowired
    private SignDocumentResponseUseCase signDocumentResponseUseCase;

    @Autowired
    private WebRequest webRequest;

    private String contentTypeSplit = null;

    public SignDocumentRest(OnPremiseCredential onPremiseI, TpcClientGenericSign tpcClientGeneric) {
        this.onPremise = onPremiseI;
        this.tpcClientGeneric = tpcClientGeneric;
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient()))).build();
    }

    @Override
    public SDResponseTotalWithLog getSignature(SDRequestTotal sdRequestTotal)
            throws MessagingException, IOException {
        Mono<ClientResponse> response = webClient.post()
                .uri(BASE_URL.concat(ES_URI))
                .body(BodyInserters.fromObject(sdRequestTotal.getData()))
                .headers(httpHeaders -> {
                    httpHeaders.add(ES_X_USERTOKEN, ES_X_USERTOKEN_VALUE);
                    httpHeaders.add(CLIENT_ID_KEY, ES_CLIENT_ID);
                    httpHeaders.add(CLIENT_SECRET_KEY, ES_CLIENT_SECRET);
                    httpHeaders.add(MESSAGE_ID, sdRequestTotal.getMessageId());
                    httpHeaders.add(ES_X_USERNAME, USER_NAME);
                }).exchange();
        Object esResponse = null;
        try {
            esResponse = Objects.requireNonNull(response.flatMap(clientResponse -> {
                if (!clientResponse.statusCode().equals(HttpStatus.OK)) {
                    return clientResponse.bodyToMono(SDResponseError.class);
                }
                contentTypeSplit = clientResponse.headers().contentType().get().getParameter("boundary");
                return clientResponse.bodyToMono(String.class);
            }).block());
        } catch (CustomException e) {
            exceptions.createException(null, OPER_SIGN_DOCUMENT, e.toString(), ERROR_CODE_TIMEOUT);
        }
        SDResponseTotal responseTotal = validateResponse(esResponse);
        Gson gson = new Gson();
        String requestString = gson.toJson(sdRequestTotal.getData(), SDRequest.class);
        String responseString = gson.toJson(responseTotal, SDResponseTotal.class);
        Date dateResponse = new CoreFunctionDate().getDatetime();
        return SDResponseTotalWithLog.builder()
                .responseTotal(responseTotal)
                .infoReuseCommon(InfoReuseCommon.builder()
                        .requestReuse(requestString).dateRequestReuse(sdRequestTotal.getDateRequest())
                        .responseReuse(responseString).dateResponseReuse(dateResponse).build())
                .build();
    }

    public SDResponseTotal validateResponse(Object object) throws MessagingException, IOException {
        if (object.getClass().equals(SDResponseError.class)) {
            return SDResponseTotal.builder().responseError((SDResponseError) object).build();
        }
        return signDocumentResponseUseCase.createResponse(object.toString(), contentTypeSplit);
    }
}