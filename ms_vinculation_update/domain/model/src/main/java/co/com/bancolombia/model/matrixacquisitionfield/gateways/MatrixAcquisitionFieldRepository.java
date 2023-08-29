package co.com.bancolombia.model.matrixacquisitionfield.gateways;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.matrixacquisitionfield.MatrixAcquisitionField;

import java.util.List;

public interface MatrixAcquisitionFieldRepository {
    List<MatrixAcquisitionField> findByMatrixAcquisition(MatrixAcquisition matrixAcquisition);
}
