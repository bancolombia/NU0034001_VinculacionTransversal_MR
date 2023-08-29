package co.com.bancolombia.commonsvnt.model.activity;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Activity {
    private String idActivity;
    private String nameActivity;
    private String codeStateActivity;
    private String nameStateActivity;
}
