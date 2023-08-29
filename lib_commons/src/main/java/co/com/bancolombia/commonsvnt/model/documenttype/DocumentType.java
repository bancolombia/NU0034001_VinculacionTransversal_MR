package co.com.bancolombia.commonsvnt.model.documenttype;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class DocumentType  extends Auditing {

    private UUID id;
    private String code;
    private String name;
    private boolean active;
    private TypePerson typePerson;
    private String codeHomologation;
    private String codeOrderExperian;
    private String codeGenericType;
    private String codeOrderControlList;
}
