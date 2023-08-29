package co.com.bancolombia.commonsvnt.model.execfield;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ExecField  extends Auditing {

    private UUID id;
    private String code;
    private String name;
    private boolean mandatory;
    private boolean upgradeable;
    private CheckList checkList;

    public  boolean isNotUpgradeable(){
        return  !upgradeable;
    }
}
