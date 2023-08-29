package co.com.bancolombia.model.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class ClauseInstructionsWithAcquisition {
    private ClauseInstructions clauseInstructions;
    private Acquisition acquisition;
}
