package co.com.bancolombia.model.merge;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class MergeAttrib {
    private String stepCode;
    private String nameList;
    private boolean isRecordUpgradeable;

}
