package co.com.bancolombia.restclient;

import co.com.bancolombia.api.model.customerdocumentpersistence.DocumentPersistenceRetriesRequest;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.SecretModelApiKeyInt;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.gateways.ServicePersistenceRestRepository;
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

import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.util.constants.Constants.API_KEY;
import static co.com.bancolombia.util.constants.Constants.DOCUMENTS_RETRIES_REST;
import static co.com.bancolombia.util.constants.Constants.FAIL_DOCUMENTS_CONNECTION;
import static co.com.bancolombia.util.constants.Constants.SUCCESFUL_DOCUMENTS_CONNECTION;

@Component
public class ServicePersistenceRest implements ServicePersistenceRestRepository {

    @Value("${baseUrlMicro.documents}")
    private String BASE_URL_MICROSERVICE_DOCUMENTS;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private WebRequest webRequest;

    private WebClient webClient;
    private TpcClientGeneric tpcClientGeneric;
    private String apiKey;

    private LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE,
            DOCUMENTS_RETRIES_REST);

    public ServicePersistenceRest(TpcClientGeneric tpcClientGeneric,
            SecretModelApiKeyInt secretModelApiKeyInt) {
        this.tpcClientGeneric = tpcClientGeneric;
        this.apiKey = secretModelApiKeyInt.getApiKeyInt();
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader("apiKey", this.apiKey)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(this.tpcClientGeneric.getClient())))
                .build();
    }

    @Override
    public void sendPersistenceDocumentRetriesInfo(List<PersistenceDocument> documentWithLogList) {
        DocumentPersistenceRetriesRequest body = new DocumentPersistenceRetriesRequest(documentWithLogList);
        Mono<ClientResponse> response = webClient.post()
                .uri(BASE_URL_MICROSERVICE_DOCUMENTS + "document-persistence-retries")
                .body(BodyInserters.fromObject(body)).header(API_KEY, apiKey)
                .exchange();
        try {
            response.flatMap(clientResponse -> {
                switch (clientResponse.statusCode()) {
                    case NOT_FOUND:
                        adapter.info(FAIL_DOCUMENTS_CONNECTION);
                        return clientResponse.bodyToMono(ClientResponse.class);
                    case OK:
                        adapter.info(SUCCESFUL_DOCUMENTS_CONNECTION);
                        return clientResponse.bodyToMono(ClientResponse.class);
                    case SERVICE_UNAVAILABLE:
                    case GATEWAY_TIMEOUT:
                        adapter.info(FAIL_DOCUMENTS_CONNECTION);
                        return Mono.error(new RuntimeException());
                    default:
                        adapter.info(FAIL_DOCUMENTS_CONNECTION);
                        return Mono.error(new RuntimeException(ERROR + " " + clientResponse.statusCode()));
                }
            }).block();
        } catch (CustomException e) {
            adapter.error(ERROR);

        }
    }
}
