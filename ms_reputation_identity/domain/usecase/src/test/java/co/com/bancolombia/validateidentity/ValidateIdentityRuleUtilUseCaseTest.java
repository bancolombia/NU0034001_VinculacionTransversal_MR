package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRuleReply;
import co.com.bancolombia.model.parameter.Parameter;
import co.com.bancolombia.model.parameter.gateways.ParametersRepository;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_VIGENT_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MIN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_PHONETHICS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ValidateIdentityRuleUtilUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateIdentityRuleUtilUseCaseImpl vIRuleUseCase;

    @Mock
    private ParametersRepository parametersRepository;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateVigenceInParametersTest() {
        Parameter parameter = Parameter.builder().code("00").parent(PARENT_VIGENT_IDENTITY).name("").build();
        doReturn(Collections.singletonList(parameter)).when(parametersRepository).findByParent(anyString());
        vIRuleUseCase.validateVigenceInParameters(AcquisitionReply.builder().build(), "00");
        Mockito.verify(this.vIRuleUseCase, Mockito.times(1))
                .validateVigenceInParameters(AcquisitionReply.builder().build(), "00");
    }

    @Test(expected = ValidationException.class)
    public void validateVigenceInParametersExceptionTest() {
        Parameter parameter = Parameter.builder().code("1").parent(PARENT_VIGENT_IDENTITY).name("").build();
        doReturn(Collections.singletonList(parameter)).when(parametersRepository).findByParent(anyString());
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        doReturn(null).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        vIRuleUseCase.validateVigenceInParameters(AcquisitionReply.builder().acquisitionId("").build(), "00");
        Mockito.verify(this.vIRuleUseCase, Mockito.times(1))
                .validateVigenceInParameters(AcquisitionReply.builder().acquisitionId("").build(), "00");
    }

    @Test
    public void findRulesActiveTest() {
        List<ValidateIdentityRuleReply> rules = Arrays.asList(ValidateIdentityRuleReply.builder().name("").active("true").score("0.0").build(),
                ValidateIdentityRuleReply.builder().name("2").active("true").score("0.0").build());
        List<ValidateIdentityRuleReply> list = vIRuleUseCase.findRulesActive(rules);
        assertNotNull(list);
    }

    @Test
    public void findConfigurationThresholdTest() {
        List<Parameter> listParameterValidateIdentity = new ArrayList<>();

        listParameterValidateIdentity.add(Parameter.builder()
                .name(THRESHOLD_MIN)
                .code("50")
                .build());
        listParameterValidateIdentity.add(Parameter.builder()
                .name(THRESHOLD_MAX)
                .code("80")
                .build());
        listParameterValidateIdentity.add(Parameter.builder()
                .name(THRESHOLD_PHONETHICS)
                .code("80")
                .build());

        doReturn(listParameterValidateIdentity).when(parametersRepository).findByParentTypeAcquisition(anyString(), anyString());

        Map<String, Double> result = vIRuleUseCase.findConfiguration("");
        assertNotNull(result);
    }

    @Test
    public void validateIfApplyRuleTest() {
        List<ValidateIdentityRuleReply> rules = Arrays.asList(ValidateIdentityRuleReply.builder().name("").active("true").score("0.0").build(),
                ValidateIdentityRuleReply.builder().name("2").active("true").score("0.0").build());
        ValidateIdentityRuleReply result = vIRuleUseCase.validateIfApplyRule(rules, "2");
        assertNotNull(result);
    }

    @Test
    public void validateIfApplyRuleNullTest() {
        List<ValidateIdentityRuleReply> rules = Arrays.asList(ValidateIdentityRuleReply.builder().name("").active("true").score("0.0").build(),
                ValidateIdentityRuleReply.builder().name("2").active("true").score("0.0").build());
        ValidateIdentityRuleReply result = vIRuleUseCase.validateIfApplyRule(rules, "0");
        assertNull(result);
    }

    @Test
    public void isRangeTest() {
        boolean result = vIRuleUseCase.isInRange("12", "20", 18);
        assertTrue(result);
    }

    @Test
    public void isRangeFalseTest() {
        boolean result = vIRuleUseCase.isInRange("12", "20", 25);
        assertFalse(result);
    }

    @Test
    public void getConfThresholdPhonethicsTest() {
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        rulesMap.put(THRESHOLD_PHONETHICS, Double.valueOf("80"));
        Double result = vIRuleUseCase.getConfThresholdPhonethics(rulesMap);
        assertNotNull(result);
    }

    @Test
    public void getConfThresholdPhonethicsNotRuleTest() {
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        Double result = vIRuleUseCase.getConfThresholdPhonethics(rulesMap);
        assertNotNull(result);
    }

    @Test
    public void getFullNameInverseTest() {
        ValidateIdentityReply infoNecesaryValidateRule = ValidateIdentityReply.builder().firstName("Juan").secondName("")
                .firstSurname("").secondSurname(null).build();
        String names = vIRuleUseCase.getFullNameInverse(infoNecesaryValidateRule);
        assertNotNull(names);
    }

    @Test
    public void getNamesTest() {
        ValidateIdentityReply infoNecesaryValidateRule = ValidateIdentityReply.builder().firstName("Juan").secondName("")
                .firstSurname("").secondSurname(null).build();
        String names = vIRuleUseCase.getNames(infoNecesaryValidateRule);
        assertNotNull(names);
    }

    @Test
    public void getNamesTwoTest() {
        ValidateIdentityReply infoNecesaryValidateRule = ValidateIdentityReply.builder().firstName("").secondName(null)
                .firstSurname("").secondSurname(null).build();
        String names = vIRuleUseCase.getNames(infoNecesaryValidateRule);
        assertNotNull(names);
    }

    @Test
    public void compareStringTest() {
        boolean result = vIRuleUseCase.compareString("", "", Double.valueOf("10"));
        assertTrue(result);
    }

    @Test
    public void compareStringJaroWinklerTest() {
        boolean result = vIRuleUseCase.compareString("Hello World", "Hello World", Double.valueOf("10"));
        assertTrue(result);
    }

    @Test
    public void compareStringTrueTest() {
        boolean result = vIRuleUseCase.compareString("Hello World", "Hello", Double.valueOf("0"));
        assertTrue(result);
    }

    @Test
    public void compareStringFalseTwoTest() {
        boolean result = vIRuleUseCase.compareString("", "Hello Juan", Double.valueOf("100"));
        assertFalse(result);
    }

    @Test
    public void compareStringFalseThreeTest() {
        boolean result = vIRuleUseCase.compareString("Hello JuanD", "Hello Juan", Double.valueOf("100"));
        assertFalse(result);
    }

    @Test
    public void statusStepUploadDocumentIsManualTest() {
        ChecklistReply checklistReply = ChecklistReply.builder().stateOperation("").build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        doReturn(checklistReply).when(vinculationUpdateUseCase).checkListStatus(anyString(), anyString());
        boolean result = vIRuleUseCase.statusStepUploadDocumentIsManual(acquisitionReply);
        assertFalse(result);
    }

    @Test
    public void validateParameterStepUploadDocumentIsManualTest() {
        Parameter parameter = Parameter.builder().code("1").build();
        doReturn(Optional.of(parameter)).when(parametersRepository).findByNameParent(anyString(), anyString());
        boolean result = vIRuleUseCase.validateParameterStepUploadDocumentIsManual();
        assertTrue(result);
    }

    @Test
    public void validateParameterValidateEmailAndCellTest() {
        Parameter parameter = Parameter.builder().code("1").build();
        doReturn(Optional.of(parameter)).when(parametersRepository).findByNameParent(anyString(), anyString());
        boolean result = vIRuleUseCase.validateParameterValidateEmailAndCell();
        assertTrue(result);
    }
}
