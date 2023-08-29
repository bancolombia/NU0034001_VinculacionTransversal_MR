package co.com.bancolombia.matrixtypeacquisition;

import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.matrixtypeacquisition.MatrixTypeAcquisition;

import java.util.Optional;

public interface MatrixTypeAcquisitionUseCase {

    /**
     * This function searches the type acquisition
     *
     * @param acquisitionStartObjectModel
     * @return Optional<MatrixTypeAcquisition>
     */
    public Optional<MatrixTypeAcquisition> search(AcquisitionStartObjectModel acquisitionStartObjectModel);

    /**
     * This function save matrix type acquisition
     *
     * @param matrixTypeAcquisition
     */
    public void saveParameterAcquisition(MatrixTypeAcquisition matrixTypeAcquisition);
}
