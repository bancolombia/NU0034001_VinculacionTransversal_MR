package co.com.bancolombia.matrixtypeacquisitionclause;

import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;

import java.util.List;

public interface MatrixTypeAcquisitionClauseUseCase {
    public List<MatrixTypeAcquisitionClause> findByTypeAcquisitionAndActive(
            MatrixTypeAcquisitionClause acquisitionClause);
}
