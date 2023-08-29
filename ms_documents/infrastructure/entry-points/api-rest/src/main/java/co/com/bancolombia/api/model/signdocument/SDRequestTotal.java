package co.com.bancolombia.api.model.signdocument;

import co.com.bancolombia.model.signdocument.SDRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SDRequestTotal {

    private SDRequest data;
    private String messageId;
    private Date dateRequest;
}
