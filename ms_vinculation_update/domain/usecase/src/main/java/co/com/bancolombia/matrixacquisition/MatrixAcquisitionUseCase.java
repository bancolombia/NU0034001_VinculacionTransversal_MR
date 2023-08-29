package co.com.bancolombia.matrixacquisition;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;

import java.util.List;

public interface MatrixAcquisitionUseCase {

    /**
     * This function save profiling to linked or update
     *
     * @param field
     * @return MatrixAcquisition
     */
    public MatrixAcquisition saveMatrixAcquisition(MatrixAcquisition field);

    /**
     * This function searches the matrix acquisition create for type profiling
     * @param typeAcquisition
     * @return List<MatrixAcquisition>
     */
    List<MatrixAcquisition> findByTypeAcquisition(TypeAcquisition typeAcquisition);

    /**
     * This function searches the matrix acquisition create for type profiling and step
     * @param typeAcquisition
     * @param step
     * @return MatrixAcquisition
     */
    MatrixAcquisition findByTypeAcquisitionAndStep(TypeAcquisition typeAcquisition, Step step);
}
