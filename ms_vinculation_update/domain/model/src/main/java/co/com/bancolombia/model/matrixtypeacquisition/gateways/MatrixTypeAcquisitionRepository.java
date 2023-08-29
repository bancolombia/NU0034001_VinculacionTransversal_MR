package co.com.bancolombia.model.matrixtypeacquisition.gateways;

import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.matrixtypeacquisition.MatrixTypeAcquisition;

import java.util.Optional;

public interface MatrixTypeAcquisitionRepository {

    public Optional<MatrixTypeAcquisition> search(AcquisitionStartObjectModel acquisitionStartObjectModel);

    public MatrixTypeAcquisition save(MatrixTypeAcquisition matrixTypeAcquisition);
}
