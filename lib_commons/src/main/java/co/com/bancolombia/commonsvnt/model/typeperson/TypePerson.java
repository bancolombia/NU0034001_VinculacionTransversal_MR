package co.com.bancolombia.commonsvnt.model.typeperson;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TypePerson  extends Auditing {

    private UUID id;
    private String code;
    private String name;
    private boolean active;

}
