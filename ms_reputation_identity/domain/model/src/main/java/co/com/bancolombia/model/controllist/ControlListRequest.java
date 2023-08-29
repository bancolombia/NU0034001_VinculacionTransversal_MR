package co.com.bancolombia.model.controllist;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ControlListRequest {

    private List<ControlListRequestItem> data;
}
