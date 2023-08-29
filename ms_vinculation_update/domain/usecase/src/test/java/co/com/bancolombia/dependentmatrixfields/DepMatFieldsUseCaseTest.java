package co.com.bancolombia.dependentmatrixfields;

import co.com.bancolombia.commonsvnt.model.dependentfield.DependentField;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.matrixacquisition.MatrixAcquisitionUseCase;
import co.com.bancolombia.model.dependentfield.gateways.DependentFieldRepository;
import co.com.bancolombia.model.dependentmatrixfields.DependentMatrixFields;
import co.com.bancolombia.model.dependentmatrixfields.DependentsList;
import co.com.bancolombia.model.dependentmatrixfields.StepFieldList;
import co.com.bancolombia.model.dependentmatrixfields.StepList;
import co.com.bancolombia.model.field.Field;
import co.com.bancolombia.model.matrixacquisitionfield.MatrixAcquisitionField;
import co.com.bancolombia.model.matrixacquisitionfield.gateways.MatrixAcquisitionFieldRepository;
import co.com.bancolombia.step.StepUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_TAX_INFO;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class DepMatFieldsUseCaseTest {

    @InjectMocks
    @Spy
    private DepManFieldsUseCaseImpl depManFieldsUseCase;

    @Mock
    private MatrixAcquisitionUseCase matAcqUseCase;
    @Mock
    private StepUseCase stepUseCase;
    @Mock
    private MatrixAcquisitionFieldRepository matrixAcqFieldRep;
    @Mock
    private DependentFieldRepository dependentFieldRepository;
    @Mock
    private Exceptions exceptions;

    private TypeAcquisition typeAcquisition;
    private Step step;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.typeAcquisition = TypeAcquisition.builder()
                .name("asd").code("asd").build();
        this.step = Step.builder().code(CODE_FOREIGN_INFORMATION).operation("a").build();
    }

    @Test
    public void frontData(){
        doReturn(Optional.of(step)).when(stepUseCase).findByCode(anyString());
        doReturn(Collections.singletonList(StepFieldList.builder().build())).when(depManFieldsUseCase)
                .buildStepFieldList(any(TypeAcquisition.class), any(Step.class));
        doReturn(Collections.singletonList(DependentsList.builder().build())).when(depManFieldsUseCase)
                .buildDependentsLists(any(TypeAcquisition.class), any(Step.class));
        doReturn(Collections.singletonList(StepFieldList.builder().build())).when(depManFieldsUseCase)
                .buildList(any(StepList.class), any(TypeAcquisition.class), any(Step.class));
        DependentMatrixFields fields = depManFieldsUseCase.frontData(typeAcquisition);
        assertNotNull(fields);
    }

    @Test
    public void frontData2(){
        doReturn(Optional.of(step)).when(stepUseCase).findByCode(anyString());
        doReturn(Collections.emptyList()).when(depManFieldsUseCase)
                .buildStepFieldList(any(TypeAcquisition.class), any(Step.class));
        doReturn(Collections.singletonList(DependentsList.builder().build())).when(depManFieldsUseCase)
                .buildDependentsLists(any(TypeAcquisition.class), any(Step.class));
        doReturn(Collections.singletonList(StepFieldList.builder().build())).when(depManFieldsUseCase)
                .buildList(any(StepList.class), any(TypeAcquisition.class), any(Step.class));
        doNothing().when(exceptions).createException(anyMap(), anyString(), anyString(), anyString());
        DependentMatrixFields fields = depManFieldsUseCase.frontData(typeAcquisition);
        assertNotNull(fields);
    }

    @Test
    public void buildStepFieldList(){
        doReturn(MatrixAcquisition.builder().build()).when(matAcqUseCase)
                .findByTypeAcquisitionAndStep(any(TypeAcquisition.class), any(Step.class));
        doReturn(Collections.singletonList(MatrixAcquisitionField.builder().field(Field.builder().name("a").build())
                .mandatory(true).upgradeable(true).build())).when(matrixAcqFieldRep)
                .findByMatrixAcquisition(any(MatrixAcquisition.class));
        List<StepFieldList> stepFieldLists = depManFieldsUseCase.buildStepFieldList(typeAcquisition, step);
        assertNotNull(stepFieldLists);
    }

    @Test
    public void buildList(){
        doReturn(Optional.of(step)).when(stepUseCase).findByCode(anyString());
        doReturn(Collections.singletonList(StepFieldList.builder().build())).when(depManFieldsUseCase)
                .buildStepFieldList(any(TypeAcquisition.class), any(Step.class));
        List<StepFieldList> stepFieldLists = depManFieldsUseCase.buildList(StepList.builder().build(),
                typeAcquisition, step);
        assertNotNull(stepFieldLists);
    }

    @Test
    public void buildList2(){
        doReturn(Optional.of(step)).when(stepUseCase).findByCode(anyString());
        doReturn(Collections.singletonList(StepFieldList.builder().build())).when(depManFieldsUseCase)
                .buildStepFieldList(any(TypeAcquisition.class), any(Step.class));
        List<StepFieldList> stepFieldLists = depManFieldsUseCase.buildList(StepList.builder().build(),
                typeAcquisition, step.toBuilder().code(CODE_TAX_INFO).build());
        assertNotNull(stepFieldLists);
    }

    @Test
    public void buildDependentsLists(){
        doReturn(Collections.singletonList(DependentField.builder().fieldDependent("a").dependentValue("a")
                .dependentOperation("a").currentField("a").mandatory(true).build())).when(dependentFieldRepository)
                .findByTypeAcquisitionAndCurrentOperationAndActive(anyString(), anyString(), anyBoolean());
        List<DependentsList> dependentsLists = depManFieldsUseCase.buildDependentsLists(typeAcquisition, step);
        assertNotNull(dependentsLists);
    }
}