package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.ErrorItemValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentityRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponseError;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.ValidateIdentityTotalResponse;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityRestRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class ValidateIdentityUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateIdentityUseCaseImpl vIUseCase;

    @Mock
    private NaturalPersonUseCase naturalUseCase;

    @Mock
    private ValidateIdentityRestRepository validateIdentityRestRepository;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ValidateIdentityRuleUseCase vIdentityRuleUseCase;

    @Mock
    private ValidateIdentitySaveUseCase identitySaveUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startProcessValidateIdentityTest() {
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        ValidateIdentityResponse identityResponse = ValidateIdentityResponse.builder().build();
        ValidateIdentityTotalResponse response = ValidateIdentityTotalResponse.builder().infoReuseCommon(infoReuseCommon)
                .validateIdentityResponse(identityResponse).build();
        ValidateIdentityRulesReply validateRulesReply = ValidateIdentityRulesReply.builder().validateIdentityRulesList(new ArrayList<>()).build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentTypeOrderExperian("").documentNumber("").build();

        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(ValidateIdentityReply.builder().firstSurname("").cellphonePersonal("").emailPersonal("")
                .expeditionDate(new Date()).birthDate(new Date()).build()).when(naturalUseCase).validateIdentity(anyString());
        Mockito.doReturn(response).when(validateIdentityRestRepository).getUserInfoValidateIdentity(any(ValidateIdentityRequest.class),
                anyString(), any(Date.class));
        Mockito.doReturn(ValidateIdentity.builder().documentManual(true).build()).when(vIdentityRuleUseCase).startValidateIdentityRule(any(ValidateIdentitySave.class),
                any(ValidateIdentityReply.class), any(AcquisitionReply.class), any(ValidateIdentityRulesReply.class));
        Mockito.doReturn(ValidateIdentitySave.builder().build()).when(identitySaveUseCase).save(any(ValidateIdentityResponse.class),
                any(AcquisitionReply.class), any(BasicAcquisitionRequest.class));
        Mockito.doReturn(validateRulesReply).when(vinculationUpdateUseCase).validateRules(anyString());

        ValidateIdentity result = vIUseCase.startProcessValidateIdentity(acquisitionReply, BasicAcquisitionRequest.builder().messageId("").build());

        assertNotNull(result);
    }

    @Test(expected = ValidationException.class)
    public void startProcessValidateIdentityNotFoundTest() {
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        ErrorItemValidateIdentity errorIdentity = ErrorItemValidateIdentity.builder().code("404").detail("").build();
        ValidateIdentityResponseError identityResponse = ValidateIdentityResponseError.builder()
                .errors(Collections.singletonList(errorIdentity)).title("").build();
        ValidateIdentityTotalResponse response = ValidateIdentityTotalResponse.builder().infoReuseCommon(infoReuseCommon)
                .errors(identityResponse).build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentTypeOrderExperian("").documentNumber("").build();

        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(ValidateIdentityReply.builder().firstSurname("").cellphonePersonal("").emailPersonal("")
                .expeditionDate(new Date()).birthDate(new Date()).build()).when(naturalUseCase).validateIdentity(anyString());
        Mockito.doReturn(response).when(validateIdentityRestRepository).getUserInfoValidateIdentity(any(ValidateIdentityRequest.class),
                anyString(), any(Date.class));
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());

        vIUseCase.startProcessValidateIdentity(acquisitionReply, BasicAcquisitionRequest.builder().messageId("").build());
    }

    @Test(expected = ValidationException.class)
    public void startProcessValidateIdentityNullTest() {
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        ValidateIdentityResponse identityResponse = ValidateIdentityResponse.builder().build();
        ValidateIdentityTotalResponse response = ValidateIdentityTotalResponse.builder().infoReuseCommon(infoReuseCommon)
                .validateIdentityResponse(identityResponse).build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentTypeOrderExperian("").documentNumber("").build();

        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(ValidateIdentityReply.builder().firstSurname(null).cellphonePersonal(null).emailPersonal(null)
                .expeditionDate(null).birthDate(null).build()).when(naturalUseCase).validateIdentity(anyString());
        Mockito.doReturn(response).when(validateIdentityRestRepository).getUserInfoValidateIdentity(any(ValidateIdentityRequest.class),
                anyString(), any(Date.class));

        vIUseCase.startProcessValidateIdentity(acquisitionReply, BasicAcquisitionRequest.builder().messageId("").build());
    }
}
