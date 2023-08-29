package co.com.bancolombia.matrixtypeacquisition;

import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.matrixtypeacquisition.MatrixTypeAcquisition;
import co.com.bancolombia.model.matrixtypeacquisition.gateways.MatrixTypeAcquisitionRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MatrixTypeAcquisitionUseCaseImpl implements MatrixTypeAcquisitionUseCase {

    private final MatrixTypeAcquisitionRepository matrixTypeAcquisitionRepository;

    @Override
    public Optional<MatrixTypeAcquisition> search(AcquisitionStartObjectModel acquisitionStartObjectModel) {
        return matrixTypeAcquisitionRepository.search(acquisitionStartObjectModel);
    }

    @Override
    public void saveParameterAcquisition(MatrixTypeAcquisition matrixTypeAcquisition) {
        matrixTypeAcquisitionRepository.save(matrixTypeAcquisition);
    }
}
