package co.com.bancolombia.validatesession;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.documenttype.DocumentTypeUseCaseImpl;
import co.com.bancolombia.model.validatesession.ValidateSession;
import co.com.bancolombia.model.validatesession.ValidateSessionRequest;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;
import co.com.bancolombia.model.validatesession.gateways.ValidateSessionRepository;
import co.com.bancolombia.model.validatesession.gateways.ValidateSessionRestRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ValidateSessionUseCaseImpTest {

    @InjectMocks
    @Spy
    private ValidateSessionUseCaseImpl validateSessionUseCase;

    @Mock
    private DocumentTypeUseCaseImpl documentTypeUseCase;

    @Mock
    private ValidateSessionRestRepository validateSessionRestRepository;

    @Mock
    private ValidateSessionRepository validateSessionRepository;

    private DocumentType docType;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        docType = DocumentType.builder().code("TIPDOC_FS003").codeHomologation("NIT").build();
    }

    @Test(expected = ValidationException.class)
    public void validateValidSessionDocumentTypeErrorTest() {
        Mockito.doReturn(Optional.empty()).when(documentTypeUseCase).findByCode(anyString());

        validateSessionUseCase.validateValidSession("123456", "TIPDOC_FS003",
                "AaBbCcDdEe-123456", Constants.CODE_START_UPDATE);
    }

    @Test(expected = ValidationException.class)
    public void validateValidSessionTokenErrorTest() {
        Mockito.doReturn(Optional.of(docType)).when(documentTypeUseCase).findByCode(anyString());
        Mockito.when(validateSessionRestRepository.getTokenValidation(any(ValidateSessionRequest.class),
                anyString())).thenThrow(ValidationException.class);

        validateSessionUseCase.validateValidSession("123456", "TIPDOC_FS003",
                "AaBbCcDdEe-123456", Constants.CODE_START_UPDATE);
    }

    @Test
    public void validateValidSessionSuccessTest() {
        ValidateSessionResponse validateSessionResponse = ValidateSessionResponse.builder()
                .tokenType("tokenType")
                .accessToken("AaBbCcDdEe-123456")
                .expiresIn("1200")
                .consentedOn("1619195484")
                .scope("scope")
                .refreshToken("AaBbCcDdEe-123456")
                .refreshTokenExpiresIn("864000").build();

        Mockito.doReturn(Optional.of(docType)).when(documentTypeUseCase).findByCode(anyString());
        Mockito.doReturn(validateSessionResponse).when(validateSessionRestRepository).getTokenValidation(
                any(ValidateSessionRequest.class), anyString());

        ValidateSessionResponse response = validateSessionUseCase.validateValidSession("123456",
                "TIPDOC_FS003", "AaBbCcDdEe-123456", Constants.CODE_START_UPDATE);

        assertNotNull(response);
    }

    @Test
    public void saveValidateSessionTest() {
        final ValidateSessionResponse validateSessionResponse = ValidateSessionResponse.builder().build();
        final Acquisition acquisition = Acquisition.builder().build();
        final ValidateSession validateSession = ValidateSession.builder().id(UUID.randomUUID()).build();

        doReturn(validateSession).when(validateSessionRepository).save(any(ValidateSession.class));
        ValidateSession validateSession1 = validateSessionUseCase.saveValidateSession(validateSessionResponse,
                acquisition);

        assertNotNull(validateSession1);
    }

    @Test
    public void getInfoReuseFromValidateSessionNotNullTest() {
        final ValidateSessionResponse validateSessionResponse = ValidateSessionResponse.builder()
                .requestReuse("").requestDateApi(new Date())
                .responseReuse("").responseDateApi(new Date())
                .build();

        InfoReuseCommon infoReuse = validateSessionUseCase.getInfoReuseFromValidateSession(validateSessionResponse);

        assertNotNull(infoReuse.getRequestReuse());
    }

    @Test
    public void getInfoReuseFromValidateSessionNullTest() {
        InfoReuseCommon infoReuse = validateSessionUseCase.getInfoReuseFromValidateSession(null);
        assertNull(infoReuse.getRequestReuse());
    }
}
