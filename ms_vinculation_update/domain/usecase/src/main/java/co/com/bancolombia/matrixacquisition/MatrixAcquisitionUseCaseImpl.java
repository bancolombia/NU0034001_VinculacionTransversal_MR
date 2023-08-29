package co.com.bancolombia.matrixacquisition;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.matrixacquisition.gateways.MatrixAcquisitionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MatrixAcquisitionUseCaseImpl implements MatrixAcquisitionUseCase {

    private final MatrixAcquisitionRepository matrixAcquisitionRepository;

    @Override
    public MatrixAcquisition saveMatrixAcquisition(MatrixAcquisition field) {
        return this.matrixAcquisitionRepository.save(field);
    }

    @Override
    public List<MatrixAcquisition> findByTypeAcquisition(TypeAcquisition typeAcquisition) {
        return this.matrixAcquisitionRepository.findByTypeAcquisition(typeAcquisition);
    }

    @Override
    public MatrixAcquisition findByTypeAcquisitionAndStep(TypeAcquisition typeAcquisition, Step step) {
        return matrixAcquisitionRepository.findByTypeAcquisitionAndStep(typeAcquisition, step);
    }
}