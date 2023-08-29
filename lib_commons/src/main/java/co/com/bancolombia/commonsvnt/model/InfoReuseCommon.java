package co.com.bancolombia.commonsvnt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@Validated
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class InfoReuseCommon {
    private String requestReuse;
    private Date dateRequestReuse;
    private String responseReuse;
    private Date dateResponseReuse;
    private Map<String, String> mapFields;
}