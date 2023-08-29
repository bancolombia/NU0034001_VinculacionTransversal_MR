package co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ClauseAcquisitionCheckList  extends Auditing {
    private UUID id;
    private boolean acceptClause;
    private Date dateAcceptClause;
    private Acquisition acquisition;
    private MatrixTypeAcquisitionClause matrixTypeAcquisitionClause;
}
