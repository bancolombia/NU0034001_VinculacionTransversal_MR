package co.com.bancolombia.prerequisitesstep;

import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.prerequisitesstep.PrerequisitesStep;
import co.com.bancolombia.model.prerequisitesstep.gateways.PrerequisitesStepRepository;
import co.com.bancolombia.step.StepUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class PrerequisitesStepUseCaseTest {

    @InjectMocks
    @Spy
    private PrerequisitesStepUseCaseImpl prerequisitesStepUseCase;

    @Mock
    private PrerequisitesStepRepository prerequisitesStepRepository;

    @Mock
    private StepUseCase stepUseCase;

    @Mock
    private CheckListUseCase checkListUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void findByTypeAcquisitionAndCurrentStepTest() {
        TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        Optional<Step> currentStep = Optional.ofNullable(Step.builder().build());
        List<PrerequisitesStep> list = Collections.singletonList(PrerequisitesStep.builder().build());

        Mockito.doReturn(currentStep).when(stepUseCase).findByCode(anyString());
        Mockito.doReturn(list).when(prerequisitesStepUseCase).findByTypeAcquisitionAndCurrentStep(
                any(TypeAcquisition.class), anyString());

        Optional<Step> step1 = stepUseCase.findByCode(anyString());

        prerequisitesStepUseCase.findByTypeAcquisitionAndCurrentStep(typeAcquisition, "");

        List<PrerequisitesStep> pre = prerequisitesStepRepository.findByTypeAcquisitionAndCurrentStep(
                typeAcquisition, currentStep.get());

        verify(stepUseCase, times(1)).findByCode("");

        verify(prerequisitesStepRepository, times(1))
                .findByTypeAcquisitionAndCurrentStep(typeAcquisition, currentStep.get());

        verify(prerequisitesStepUseCase, times(1))
                .findByTypeAcquisitionAndCurrentStep(typeAcquisition, "");

        assertNotNull(pre);
        assertNotNull(step1);
    }

    @Test
    public void validatePrerequisitesTest() {
        CheckList identity = CheckList.builder().state(StateStep.builder().code("2").build())
                .step(Step.builder().code("VALIDENT").operation("validate-identity").build()).build();
        CheckList expo = CheckList.builder().state(StateStep.builder().code("2").build())
                .step(Step.builder().code("EXPQUESTIONS").operation("expo-questions").build()).build();
        CheckList valQuestion = CheckList.builder().state(StateStep.builder().code("2").build())
                .step(Step.builder().code("VALQUESTIONS").operation("validate-questions").build()).build();

        List<CheckList> lists = new LinkedList<>(Arrays.asList(identity, expo, valQuestion));

        PrerequisitesStep preIdentity = PrerequisitesStep.builder().currentStep(Step.builder().code("SEGCUSTOMER").build())
                .step(Step.builder().code("VALIDENT").build()).states(Arrays.asList(new String[]{"2"})).build();
        PrerequisitesStep preExpo = PrerequisitesStep.builder().currentStep(Step.builder().code("SEGCUSTOMER").build())
                .step(Step.builder().code("EXPQUESTIONS").build()).states(Arrays.asList(new String[]{"2"})).build();
        PrerequisitesStep preValQuestion = PrerequisitesStep.builder().currentStep(Step.builder().code("SEGCUSTOMER").build())
                .step(Step.builder().code("VALQUESTIONS").build()).states(Arrays.asList(new String[]{"2"})).build();

        List<PrerequisitesStep> preLists = new LinkedList<>(Arrays.asList(preIdentity, preExpo, preValQuestion));

        TypeAcquisition t = TypeAcquisition.builder().code("VT001").build();

        Mockito.doReturn(preLists.stream().filter(PrerequisitesStep::isActive).collect(Collectors.toList()))
                .when(prerequisitesStepUseCase).findByTypeAcquisitionAndCurrentStep(
                        any(TypeAcquisition.class), anyString());
        Mockito.doReturn(lists).when(checkListUseCase).getCheckListByAcquisition(any(Acquisition.class));

        prerequisitesStepUseCase.validatePrerrequisites(Acquisition.builder().typeAcquisition(t).build(),
                "SEGCUSTOMER");

        verify(prerequisitesStepUseCase, times(1))
                .validatePrerrequisites(Acquisition.builder().typeAcquisition(t).build(), "SEGCUSTOMER");
    }

    @Test(expected = ValidationException.class)
    public void validatePrerequisitesWithExceptionTest() {
        CheckList identity = CheckList.builder().state(StateStep.builder().code("2").build())
                .step(Step.builder().code("VALIDENT").operation("validate-identity").build()).build();
        CheckList expo = CheckList.builder().state(StateStep.builder().code("2").build())
                .step(Step.builder().code("EXPQUESTIONS").operation("expo-questions").build()).build();
        CheckList valQuestion = CheckList.builder().state(StateStep.builder().code("2").build())
                .step(Step.builder().code("VALQUESTIONS").operation("validate-questions").build()).build();

        List<CheckList> lists = new LinkedList<>(Arrays.asList(identity, expo, valQuestion));

        PrerequisitesStep preIdentity = PrerequisitesStep.builder().currentStep(Step.builder().code("SEGCUSTOMER").build())
                .step(Step.builder().code("VALIDENT").build()).states(Arrays.asList(new String[]{"2"})).build();
        PrerequisitesStep preExpo = PrerequisitesStep.builder().currentStep(Step.builder().code("SEGCUSTOMER").build())
                .step(Step.builder().code("EXPQUESTIONS").build()).states(Arrays.asList(new String[]{"2"})).build();
        PrerequisitesStep preValQuestion = PrerequisitesStep.builder().currentStep(Step.builder().code("SEGCUSTOMER").build())
                .step(Step.builder().code("VALQUESTIONS").build()).states(Arrays.asList(new String[]{"7"})).build();

        List<PrerequisitesStep> preLists = new LinkedList<>(Arrays.asList(preIdentity, preExpo, preValQuestion));

        TypeAcquisition t = TypeAcquisition.builder().code("VT001").build();

        Mockito.doReturn(preLists).when(prerequisitesStepUseCase).findByTypeAcquisitionAndCurrentStep(
                any(TypeAcquisition.class), anyString());
        Mockito.doReturn(lists).when(checkListUseCase).getCheckListByAcquisition(any(Acquisition.class));

        prerequisitesStepUseCase.validatePrerrequisites(Acquisition.builder().typeAcquisition(t).build(),
                "SEGCUSTOMER");

        verify(prerequisitesStepUseCase, times(1))
                .validatePrerrequisites(Acquisition.builder().typeAcquisition(t).build(), "SEGCUSTOMER");
    }
}
