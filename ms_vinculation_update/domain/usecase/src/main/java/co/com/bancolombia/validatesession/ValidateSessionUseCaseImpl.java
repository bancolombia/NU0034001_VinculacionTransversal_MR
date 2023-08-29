package co.com.bancolombia.validatesession;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.documenttype.DocumentTypeUseCase;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.validatesession.ValidateSession;
import co.com.bancolombia.model.validatesession.ValidateSessionRequest;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;
import co.com.bancolombia.model.validatesession.gateways.ValidateSessionRepository;
import co.com.bancolombia.model.validatesession.gateways.ValidateSessionRestRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL_RESULT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_START_UPDATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_GRANT_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SI_SCOPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;

@RequiredArgsConstructor
public class ValidateSessionUseCaseImpl implements ValidateSessionUseCase {

    private final DocumentTypeUseCase documentTypeUseCase;
    private final ValidateSessionRestRepository validateSessionRestRepository;
    private final ValidateSessionRepository validateSessionRepository;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, OPER_START_UPDATE);

    @Override
    public ValidateSessionResponse validateValidSession(String documentNumber, String documentType, String token,
                                                        String operation) {
        String docTypeHomologous = getDocumentTypeHomologous(documentType, operation);
        String username = docTypeHomologous + "-" + documentNumber;

        ValidateSessionRequest validateSessionRequest = ValidateSessionRequest.builder()
                .grantType(SI_GRANT_TYPE)
                .username(username)
                .password(token)
                .scope(SI_SCOPE).build();
        adapter.info(CONSUME_EXTERNAL);
        return validateSessionRestRepository.getTokenValidation(validateSessionRequest, operation);
    }

    @Override
    public ValidateSession saveValidateSession(ValidateSessionResponse validateSessionResponse,
                                               Acquisition acquisition) {
        ValidateSession validateSession = ValidateSession.builder().acquisition(acquisition)
                .tokenType(validateSessionResponse.getTokenType())
                .accessToken(validateSessionResponse.getAccessToken())
                .expiresIn(validateSessionResponse.getExpiresIn())
                .consentedOn(validateSessionResponse.getConsentedOn())
                .scope(validateSessionResponse.getScope())
                .refreshToken(validateSessionResponse.getRefreshToken())
                .refreshTokenExpiresIn(validateSessionResponse.getRefreshTokenExpiresIn())
                .createdBy(acquisition.getCreatedBy()).createdDate(new CoreFunctionDate().getDatetime()).build();
        adapter.info(CONSUME_EXTERNAL_RESULT + validateSession.getTokenType());
        return validateSessionRepository.save(validateSession);
    }

    @Override
    public InfoReuseCommon getInfoReuseFromValidateSession(ValidateSessionResponse validateSessionResponse) {
        InfoReuseCommon infoReuse = null;
        if (validateSessionResponse != null) {
            infoReuse = InfoReuseCommon.builder()
                    .requestReuse(validateSessionResponse.getRequestReuse())
                    .dateRequestReuse(validateSessionResponse.getRequestDateApi())
                    .responseReuse(validateSessionResponse.getResponseReuse())
                    .dateResponseReuse(validateSessionResponse.getResponseDateApi())
                    .mapFields(new HashMap<>())
                    .build();
        } else {
            infoReuse = InfoReuseCommon.builder().mapFields(new HashMap<>()).build();
        }

        return infoReuse;
    }

    private String getDocumentTypeHomologous(String documentType, String operation) {
        Optional<DocumentType> oTyDoc = documentTypeUseCase.findByCode(documentType);

        String docTypeHomologous = null;
        if (oTyDoc.isPresent()) {
            docTypeHomologous = oTyDoc.get().getCodeHomologation();
        }

        if (docTypeHomologous == null) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            adapter.error(ERROR_CODE_SYSTEM + "SIN HOMOLOGACION");
            ErrorField errorField = ErrorField.builder()
                    .name(operation)
                    .complement("El tipo de documento " + documentType + " no tiene homologación").build();
            List<ErrorField> eFieldList = new ArrayList<>();
            eFieldList.add(errorField);
            error.put(ERROR_CODE_SYSTEM, eFieldList);
            throw new ValidationException(error);
        }

        return docTypeHomologous;
    }
}
