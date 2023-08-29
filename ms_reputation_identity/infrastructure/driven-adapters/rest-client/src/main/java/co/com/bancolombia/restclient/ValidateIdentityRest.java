package co.com.bancolombia.restclient;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.OnPremiseCredential;
import co.com.bancolombia.model.commons.TpcClientGeneric;
import co.com.bancolombia.model.validateidentity.ErrorItemValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentityRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponseError;
import co.com.bancolombia.model.validateidentity.ValidateIdentityTotalResponse;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityRestRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_NOT_FOUND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_ID_CONTROL_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAM_FIRST_SURNAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_IDENTITY_URI;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CALL_TO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_VIDENTITY_BACKEND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TITLE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VALIDATION_ERRB;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VALIDATION_ERRS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VAL_IDE_CODE_ERROR_BP10;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VAL_IDE_CODE_ERROR_BP14;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VAL_IDE_CODE_ERROR_BP9;

@Component
public class ValidateIdentityRest implements ValidateIdentityRestRepository {

    private WebClient webClient;
    private OnPremiseCredential onPremise;
    private TpcClientGeneric tpcClientGeneric;

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CoreFunctionDate coreFunctionDate;


    @Value("${baseUrl.onPremise}")
    private String BASE_URL_VALIDATE_IDENTITY;

    public ValidateIdentityRest(OnPremiseCredential onPremiseI, TpcClientGeneric clientGeneric) {
        this.onPremise = onPremiseI;
        this.tpcClientGeneric = clientGeneric;

        this.webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, BANK_HEADER)
                .defaultHeader(HttpHeaders.ACCEPT, BANK_HEADER)
                .defaultHeader(CLIENT_ID_KEY, this.onPremise.getClientId())
                .defaultHeader(CLIENT_SECRET_KEY, this.onPremise.getClientSecret())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tpcClientGeneric.getClient()))).build();
    }

    public ValidateIdentityTotalResponse errorValidate(Object object) {
        if (object.getClass().equals(ValidateIdentityResponseError.class)) {
            ValidateIdentityResponseError vIError = (ValidateIdentityResponseError) object;
            ErrorItemValidateIdentity errorItemVI = vIError.getErrors().get(0);
            String error = errorItemVI.getCode();
            if (CODE_NOT_FOUND.equals(vIError.getStatus().toString()) && (error.equals(VAL_IDE_CODE_ERROR_BP9) ||
                    error.equals(VAL_IDE_CODE_ERROR_BP10) || error.equals(VAL_IDE_CODE_ERROR_BP14))) {
                return ValidateIdentityTotalResponse.builder().errors(vIError)
                        .validateIdentityResponse(null).build();
            } else {
                if (error.charAt(0) == VALIDATION_ERRS) {
                    throw new ValidationException(new HashMap<String, List<ErrorField>>() {
                        {
                            put(ERROR_CODE_SYSTEM, Collections.singletonList(ErrorField.builder()
                                    .name(OPER_VALIDATE_IDENTITY).complement(CODE_ERROR.concat(error)
                                            .concat(SPACE).concat(TITLE).concat(vIError.getTitle()).concat(SPACE)
                                            .concat(DETAIL).concat(errorItemVI.getDetail())).build()));
                        }
                    });
                } else if (error.charAt(0) == VALIDATION_ERRB) {
                    throw new ValidationException(new HashMap<String, List<ErrorField>>() {
                        {
                            put(ERROR_CODE_VIDENTITY_BACKEND, Collections.singletonList(ErrorField.builder()
                                    .name(OPER_VALIDATE_IDENTITY).complement(CODE_ERROR.concat(error).concat(SPACE)
                                            .concat(TITLE).concat(vIError.getTitle()).concat(SPACE).concat(DETAIL)
                                            .concat(errorItemVI.getDetail())).build()));
                        }
                    });
                } else {
                    throw new RuntimeException(CALL_TO + SPACE + VALIDATE_IDENTITY_URI + SPACE +
                            ERROR + SPACE + vIError.getStatus());
                }
            }
        }
        return ValidateIdentityTotalResponse.builder().errors(null)
                .validateIdentityResponse((ValidateIdentityResponse) object).build();
    }

    @Override
    public ValidateIdentityTotalResponse getUserInfoValidateIdentity(ValidateIdentityRequest validateIdentityRequest,
                                                                     String messageId, Date dateRequestApi) {
        Mono<ClientResponse> response = webClient.mutate().baseUrl(BASE_URL_VALIDATE_IDENTITY).build()
                .get().uri(uriBase -> uriBase.path(VALIDATE_IDENTITY_URI + validateIdentityRequest.getDocument())
                        .queryParam(PARAM_FIRST_SURNAME, validateIdentityRequest.getFirstSurname()).build())
                .header(MESSAGE_ID_CONTROL_LIST, messageId).exchange();
        Object validateIdentityResponse = null;
        try {
            validateIdentityResponse = response.flatMap(clientResponse -> {
                if (clientResponse.statusCode().is2xxSuccessful()) {
                    return clientResponse.bodyToMono(ValidateIdentityResponse.class);
                }
                return clientResponse.bodyToMono(ValidateIdentityResponseError.class);
            }).block();
        } catch (CustomException e) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            ErrorField errorField = ErrorField.builder().name(OPER_VALIDATE_IDENTITY).complement(e.toString()).build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_SYSTEM, eFieldList);
            throw new ValidationException(error);
        }
        ValidateIdentityTotalResponse vIdentityTotalResponse = errorValidate(validateIdentityResponse);
        Gson gson = new Gson();
        String requestString = gson.toJson(validateIdentityRequest, ValidateIdentityRequest.class);
        String responseString = gson.toJson(vIdentityTotalResponse, ValidateIdentityTotalResponse.class);
        Date dateResponseFinalApi = this.coreFunctionDate.getDatetime();
        vIdentityTotalResponse.setInfoReuseCommon(InfoReuseCommon.builder().dateRequestReuse(dateRequestApi)
                .dateResponseReuse(dateResponseFinalApi).requestReuse(requestString)
                .responseReuse(responseString).build());
        return vIdentityTotalResponse;
    }
}
