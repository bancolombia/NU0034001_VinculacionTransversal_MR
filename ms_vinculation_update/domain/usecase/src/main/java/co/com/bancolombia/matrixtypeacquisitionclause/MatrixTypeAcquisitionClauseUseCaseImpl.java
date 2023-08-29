package co.com.bancolombia.matrixtypeacquisitionclause;

import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.model.matrixtypeacquisitionclause.gateways.MatrixTypeAcquisitionClauseRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MatrixTypeAcquisitionClauseUseCaseImpl implements MatrixTypeAcquisitionClauseUseCase {

    private final MatrixTypeAcquisitionClauseRepository repository;

    @Override
    public List<MatrixTypeAcquisitionClause> findByTypeAcquisitionAndActive(
            MatrixTypeAcquisitionClause acquisitionClause) {
        return this.repository.findByTypeAcquisitionAndActive(acquisitionClause);
    }
}
