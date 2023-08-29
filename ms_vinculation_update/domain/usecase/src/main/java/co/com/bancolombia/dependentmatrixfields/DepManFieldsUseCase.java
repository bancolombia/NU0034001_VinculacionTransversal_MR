package co.com.bancolombia.dependentmatrixfields;

import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.dependentmatrixfields.DependentMatrixFields;

public interface DepManFieldsUseCase {
    public DependentMatrixFields frontData(TypeAcquisition typeAcquisition);
}