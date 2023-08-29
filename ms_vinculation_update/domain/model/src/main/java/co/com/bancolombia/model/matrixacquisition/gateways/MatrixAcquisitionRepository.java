package co.com.bancolombia.model.matrixacquisition.gateways;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;

import java.util.List;

public interface MatrixAcquisitionRepository {
    public MatrixAcquisition save(MatrixAcquisition matrixAcquisition);
    public List<MatrixAcquisition> findByTypeAcquisition(TypeAcquisition typeAcquisition);
    MatrixAcquisition findByTypeAcquisitionAndStep(TypeAcquisition typeAcquisition, Step step);
}
