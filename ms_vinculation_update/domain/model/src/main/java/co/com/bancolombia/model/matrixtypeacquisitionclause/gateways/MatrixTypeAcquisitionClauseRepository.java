package co.com.bancolombia.model.matrixtypeacquisitionclause.gateways;

import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;

import java.util.List;

public interface MatrixTypeAcquisitionClauseRepository {
    public List<MatrixTypeAcquisitionClause> findByTypeAcquisitionAndActive(MatrixTypeAcquisitionClause acquisition);
}
