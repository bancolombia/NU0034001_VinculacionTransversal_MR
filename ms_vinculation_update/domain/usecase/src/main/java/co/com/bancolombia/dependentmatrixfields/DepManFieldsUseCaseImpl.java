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
import co.com.bancolombia.model.matrixacquisitionfield.MatrixAcquisitionField;
import co.com.bancolombia.model.matrixacquisitionfield.gateways.MatrixAcquisitionFieldRepository;
import co.com.bancolombia.step.StepUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FOREIGN_CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_TAX_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_TAX_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.util.constants.Constants.opeNatLegPerson;

@RequiredArgsConstructor
public class DepManFieldsUseCaseImpl implements DepManFieldsUseCase {

    private final MatrixAcquisitionUseCase matAcqUseCase;
    private final StepUseCase stepUseCase;
    private final MatrixAcquisitionFieldRepository matrixAcqFieldRep;
    private final DependentFieldRepository dependentFieldRepository;
    private final Exceptions exceptions;

    @Override
    public DependentMatrixFields frontData(TypeAcquisition typeAcquisition) {
        List<StepList> stepLists = new ArrayList<>();
        boolean existData = false;
        for(String stepCode : opeNatLegPerson){
            Step step = stepUseCase.findByCode(stepCode).orElse(null);
            assert step != null;
            StepList stepList = StepList.builder().stepCode(stepCode)
                    .stepFieldList(buildStepFieldList(typeAcquisition, step))
                    .dependentsLists(buildDependentsLists(typeAcquisition, step)).build();
            if (stepCode.contains(String.join(CODE_FOREIGN_INFORMATION, CODE_TAX_INFO))){
                stepList.setList(buildList(stepList, typeAcquisition, step));
            }
            existData = existData || !stepList.getStepFieldList().isEmpty();
            stepLists.add(stepList);
        }
        if (!existData){
            exceptions.createException(null, EMPTY, EMPTY, "exceptionCode");//TODO EXCEPTION
        }
        return DependentMatrixFields.builder()
                .acquisitionCode(typeAcquisition.getCode()).acquisitionName(typeAcquisition.getName())
                .stepList(stepLists).build();
    }

    public List<StepFieldList> buildStepFieldList(TypeAcquisition typeAcquisition, Step step){
        MatrixAcquisition matrixAcquisition = matAcqUseCase.findByTypeAcquisitionAndStep(typeAcquisition, step);
        List<MatrixAcquisitionField> matrixAcquisitionField = matrixAcqFieldRep
                .findByMatrixAcquisition(matrixAcquisition);
        List<StepFieldList> stepFieldLists = new ArrayList<>();
        matrixAcquisitionField.forEach(maf -> stepFieldLists.add(
                StepFieldList.builder().field(maf.getField().getName()).mandatory(maf.isMandatory())
                        .upgradable(maf.isUpgradeable()).build()));
        return stepFieldLists;
    }

    public List<StepFieldList> buildList(StepList stepList, TypeAcquisition typeAcquisition, Step step){
        String stepCode = (step.getCode().equals(CODE_FOREIGN_INFORMATION)) ? CODE_FOREIGN_CURRENCY : CODE_TAX_COUNTRY;
        stepList.setListOperation(stepCode);
        Step step1 = stepUseCase.findByCode(stepCode).orElse(null);
        return buildStepFieldList(typeAcquisition, step1);
    }

    public List<DependentsList> buildDependentsLists(TypeAcquisition typeAcquisition, Step step){
        List<DependentField> dependentFields = dependentFieldRepository
                .findByTypeAcquisitionAndCurrentOperationAndActive(
                typeAcquisition.getCode(), step.getOperation(), true);
        List<DependentsList> dependentsLists = new ArrayList<>();
        dependentFields.forEach(df -> dependentsLists.add(
                DependentsList.builder().dependentField(df.getFieldDependent()).dependentValue(df.getDependentValue())
                        .dependentOperation(df.getDependentOperation()).currentField(df.getCurrentField())
                        .mandatory(df.isMandatory()).build()));
        return dependentsLists;
    }
}