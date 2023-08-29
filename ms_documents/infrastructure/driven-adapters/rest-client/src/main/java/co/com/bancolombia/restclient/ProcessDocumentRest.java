package co.com.bancolombia.restclient;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxRequest;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotalRequest;
import co.com.bancolombia.model.uploaddocument.UploadDocumentErrorResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.model.uploaddocument.gateways.ProcessDocumentRestRepository;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.BANK_HEADER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_ID_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CLIENT_SECRET_KEY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_CONTROL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.util.constants.Constants.PROCESS_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.STATUS_CODE;

@Component
public class ProcessDocumentRest implements ProcessDocumentRestRepository {

    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGeneric tpcClientGeneric;

    @Value("${baseUrlDataPower.onPremise}")
    private String BASE_URL_PROCESS_DOCUMENT;

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_DIGITALIZATION, OPER_UPLOAD_DOCUMENT);

    public ProcessDocumentRest(OnPremiseCredential onPremise, TpcClientGeneric clientGeneric) {
        this.onPremise = onPremise;
        this.tpcClientGeneric = clientGeneric;
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, BANK_HEADER)
                .defaultHeader(HttpHeaders.ACCEPT, BANK_HEADER)
                .defaultHeader(CLIENT_ID_KEY, this.onPremise.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, this.onPremise.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient()))).build();
    }

    @Override
    public UploadDocumentWithLog getRest(
            ProcessDocumentKofaxRequest processDocument, String messageId, Date dateRequestApi) {

        adapter.info(MESSAGE_ID + SPACE + messageId);
        ProcessDocumentKofaxTotalRequest processDocumentKofaxTotalRequest =
                ProcessDocumentKofaxTotalRequest.builder().data(processDocument).build();

        Flux<ClientResponse> response = webClient.post()
                .uri(BASE_URL_PROCESS_DOCUMENT + PROCESS_DOCUMENT)
                .body(BodyInserters.fromObject(processDocumentKofaxTotalRequest))
                .header(MESSAGE_ID, messageId)
                .exchange().flux();

        Object kofaxResponse = null;
        try {
            kofaxResponse = Objects.requireNonNull(response.flatMap(clientResponse -> {
                adapter.info(STATUS_CODE + clientResponse.statusCode());
                if (clientResponse.statusCode().is2xxSuccessful()) {
                    return clientResponse.bodyToMono(UploadDocumentResponse.class);
                }
                if (clientResponse.statusCode().is4xxClientError()
                        || clientResponse.statusCode().is5xxServerError()) {
                    return clientResponse.bodyToMono(UploadDocumentErrorResponse.class);
                } else {
                    adapter.error(MESSAGE_NOT_CONTROL + SPACE + clientResponse.rawStatusCode());
                    return Mono.error(new RuntimeException());
                }
            }).collectList().block()).get(0);
        } catch (CustomException ex) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            ErrorField errorField = ErrorField.builder().name(OPER_UPLOAD_DOCUMENT).complement(ex.toString()).build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_SYSTEM, eFieldList);
            throw new ValidationException(error);
        }

        UploadDocumentTotal uploadDocumentTotal = validateErrors(kofaxResponse);
        Gson gson = new Gson();
        String requestString = gson.toJson(processDocumentKofaxTotalRequest, ProcessDocumentKofaxTotalRequest.class);
        String responseString = gson.toJson(uploadDocumentTotal, UploadDocumentTotal.class);
        Date dateResponseFinalApi = this.coreFunctionDate.getDatetime();

        return UploadDocumentWithLog.builder()
                .uploadDocumentTotal(uploadDocumentTotal)
                .infoReuseCommon(InfoReuseCommon.builder()
                        .requestReuse(requestString)
                        .dateRequestReuse(dateRequestApi)
                        .responseReuse(responseString)
                        .dateResponseReuse(dateResponseFinalApi)
                        .build())
                .build();
    }

    private UploadDocumentTotal validateErrors(Object object) {
        if (object.getClass().equals(UploadDocumentErrorResponse.class)) {
            return UploadDocumentTotal.builder()
                    .uploadDocumentErrorResponse((UploadDocumentErrorResponse) object).build();
        } else {
            return UploadDocumentTotal.builder()
                    .uploadDocumentResponse((UploadDocumentResponse) object).build();
        }
    }
}
