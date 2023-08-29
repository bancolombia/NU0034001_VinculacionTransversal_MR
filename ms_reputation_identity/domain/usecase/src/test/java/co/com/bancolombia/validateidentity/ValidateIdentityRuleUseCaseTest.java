package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRuleReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
import co.com.bancolombia.model.parameter.Parameter;
import co.com.bancolombia.model.parameter.gateways.ParametersRepository;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityScoreRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_VIGENT_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MIN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_PHONETHICS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class ValidateIdentityRuleUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateIdentityRuleUseCaseImpl vIRuleUseCase;

    @Mock
    private ValidateIdentityRuleUtilUseCase validateIRUtilUC;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ValidateIdentityValidationRuleUseCase vIValidationRuleUC;

    @Mock
    private ValidateIdentityScoreRepository scoreRepository;

    @Mock
    private ParametersRepository parametersRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startValidateIdentityRuleTest() {
        ValidateIdentitySave validate = ValidateIdentitySave.builder().status("00").build();
        ValidateIdentityReply validateReply = ValidateIdentityReply.builder().build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder()
                .ruleOneCellphone(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleTwoEmail(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleThreeAge(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleFourDateExpedition(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleFiveFullName(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleSixSecondSurname(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleSevenWorkPhone(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleEightWorkCellphone(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleNineWorkEmail(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .ruleTenNames(BigDecimal.valueOf(Double.parseDouble("10.0")))
                .accumulated(BigDecimal.valueOf(Double.parseDouble("100.0"))).build();
        List<ValidateIdentityRuleReply> rules = Arrays.asList(ValidateIdentityRuleReply.builder().name("").active("true").score("0.0").build(),
                ValidateIdentityRuleReply.builder().name("2").active("true").score("0.0").build());
        Parameter parameter = Parameter.builder().code("00").parent(PARENT_VIGENT_IDENTITY).name("").build();
        ValidateIdentityRulesReply validateIdentityReply = ValidateIdentityRulesReply.builder()
                .validateIdentityRulesList(rules).typeAcquisition("").build();
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        rulesMap.put(THRESHOLD_PHONETHICS, Double.valueOf("80"));
        Mockito.doReturn(Collections.singletonList(parameter)).when(parametersRepository).findByParent(anyString());
        Mockito.doReturn(rulesMap).when(validateIRUtilUC).findConfiguration(anyString());
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        Mockito.doReturn(validateIdentityScore).when(vIValidationRuleUC).calculateAccumulated(anyList(), any(ValidateIdentitySave.class),
                any(ValidateIdentityReply.class), anyMap());
        Mockito.doReturn(validateIdentityScore).when(scoreRepository).save(any(ValidateIdentityScore.class));
        ValidateIdentity response = vIRuleUseCase.startValidateIdentityRule(validate, validateReply, acquisitionReply, validateIdentityReply);
        assertNotNull(response);
    }

    @Test
    public void validateThresholdMinTest() {
        Mockito.doNothing().when(validateIRUtilUC).refuseAcquisition(any(AcquisitionReply.class));
        ValidateIdentity response = vIRuleUseCase.validateThresholdMin(Double.parseDouble("10.0"), Double.parseDouble("10.1"),
                AcquisitionReply.builder().build());
        assertNotNull(response);
    }

    @Test
    public void validateThresholdBetweenMinAndMaxTest() {
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity response = vIRuleUseCase.validateThresholdBetweenMinAndMax(Double.parseDouble("11.0"),
                Double.parseDouble("10.1"), Double.parseDouble("11.1"),
                AcquisitionReply.builder().build());
        assertNotNull(response);
    }

    @Test
    public void validateThresholdMaxTest() {
        Mockito.doNothing().when(validateIRUtilUC).refuseAcquisition(any(AcquisitionReply.class));
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder()
                .ruleOneCellphone(new BigDecimal(0)).ruleTwoEmail(new BigDecimal(0)).build();
        ValidateIdentity response = vIRuleUseCase.validateThresholdMax(Double.parseDouble("10.0"), Double.parseDouble("10.1"),
                AcquisitionReply.builder().build(), validateIdentityScore);
        assertNull(response);
    }

    @Test
    public void validateThresholdBetweenMinAndMaxGreaterTest() {
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity response = vIRuleUseCase.validateThresholdBetweenMinAndMax(Double.parseDouble("12.0"),
                Double.parseDouble("10.1"), Double.parseDouble("11.1"),
                AcquisitionReply.builder().build());
        assertNull(response);
    }

    @Test
    public void validateThresholdBetweenMinAndMaxLessTest() {
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity response = vIRuleUseCase.validateThresholdBetweenMinAndMax(Double.parseDouble("10.0"),
                Double.parseDouble("10.1"), Double.parseDouble("11.1"),
                AcquisitionReply.builder().build());
        assertNull(response);
    }

    @Test
    public void validateAccumulatedWithoutConfigTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().build();
        ValidateIdentity response = vIRuleUseCase.validateAccumulated(Double.parseDouble("10.0"), acquisitionReply,
                null, validateIdentityScore);
        assertNull(response);
    }

    @Test
    public void validateAccumulatedNullTest() {
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        rulesMap.put(THRESHOLD_PHONETHICS, Double.valueOf("80"));
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        Mockito.doReturn(ValidateIdentity.builder().build()).when(vIRuleUseCase).validateThresholdMin(anyDouble(), anyDouble(),
                any(AcquisitionReply.class));
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().build();
        ValidateIdentity response = vIRuleUseCase.validateAccumulated(Double.parseDouble("8.0"), acquisitionReply,
                rulesMap, validateIdentityScore);
        assertNotNull(response);
    }

    @Test
    public void scaleOperation() {
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity validateIdentity = vIRuleUseCase.scaleOperation("", Double.parseDouble("0"));
        assertNotNull(validateIdentity);
    }

    @Test
    public void validateThresholdMaxDocumentTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().ruleOneCellphone(new BigDecimal(0))
                .ruleTwoEmail(new BigDecimal(0)).build();
        Mockito.doReturn(true).when(validateIRUtilUC).validateParameterStepUploadDocumentIsManual();
        Mockito.doReturn(true).when(validateIRUtilUC).statusStepUploadDocumentIsManual(any(AcquisitionReply.class));
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity result = vIRuleUseCase.validateThresholdMax(Double.parseDouble("10"),
                Double.parseDouble("5"), acquisitionReply, validateIdentityScore);
        assertNotNull(result);
    }

    @Test
    public void validateThresholdMaxDocumentFalseTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().ruleOneCellphone(new BigDecimal(0))
                .ruleTwoEmail(new BigDecimal(0)).build();
        Mockito.doReturn(true).when(validateIRUtilUC).validateParameterStepUploadDocumentIsManual();
        Mockito.doReturn(false).when(validateIRUtilUC).statusStepUploadDocumentIsManual(any(AcquisitionReply.class));
        Mockito.doReturn(true).when(validateIRUtilUC).validateParameterValidateEmailAndCell();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity result = vIRuleUseCase.validateThresholdMax(Double.parseDouble("10"),
                Double.parseDouble("5"), acquisitionReply, validateIdentityScore);
        assertNotNull(result);
    }

    @Test
    public void validateThresholdMaxDocumentFalseTwoTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().ruleOneCellphone(new BigDecimal(0))
                .ruleTwoEmail(new BigDecimal(0)).build();
        Mockito.doReturn(false).when(validateIRUtilUC).validateParameterStepUploadDocumentIsManual();
        Mockito.doReturn(true).when(validateIRUtilUC).statusStepUploadDocumentIsManual(any(AcquisitionReply.class));
        Mockito.doReturn(false).when(validateIRUtilUC).validateParameterValidateEmailAndCell();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity result = vIRuleUseCase.validateThresholdMax(Double.parseDouble("10"),
                Double.parseDouble("5"), acquisitionReply, validateIdentityScore);
        assertNotNull(result);
    }

    @Test
    public void validateThresholdMaxDocumentFalseThreeTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().ruleOneCellphone(new BigDecimal(10))
                .ruleTwoEmail(new BigDecimal(0)).build();
        Mockito.doReturn(false).when(validateIRUtilUC).validateParameterStepUploadDocumentIsManual();
        Mockito.doReturn(false).when(validateIRUtilUC).statusStepUploadDocumentIsManual(any(AcquisitionReply.class));
        Mockito.doReturn(true).when(validateIRUtilUC).validateParameterValidateEmailAndCell();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity result = vIRuleUseCase.validateThresholdMax(Double.parseDouble("10"),
                Double.parseDouble("5"), acquisitionReply, validateIdentityScore);
        assertNotNull(result);
    }

    @Test
    public void validateThresholdMaxDocumentFalseFourTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        ValidateIdentityScore validateIdentityScore = ValidateIdentityScore.builder().ruleOneCellphone(new BigDecimal(0))
                .ruleTwoEmail(new BigDecimal(10)).build();
        Mockito.doReturn(false).when(validateIRUtilUC).validateParameterStepUploadDocumentIsManual();
        Mockito.doReturn(false).when(validateIRUtilUC).statusStepUploadDocumentIsManual(any(AcquisitionReply.class));
        Mockito.doReturn(true).when(validateIRUtilUC).validateParameterValidateEmailAndCell();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateIdentity result = vIRuleUseCase.validateThresholdMax(Double.parseDouble("10"),
                Double.parseDouble("5"), acquisitionReply, validateIdentityScore);
        assertNotNull(result);
    }

    @Test
    public void convertBooleanStringTest() {
        String result = vIRuleUseCase.convertBooleanString(null);
        assertNotNull(result);
    }

    @Test
    public void convertBooleanStringTrueTest() {
        String result = vIRuleUseCase.convertBooleanString(true);
        assertNotNull(result);
    }
}
