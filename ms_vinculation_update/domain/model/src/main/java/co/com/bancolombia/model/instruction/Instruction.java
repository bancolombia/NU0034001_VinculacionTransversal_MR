package co.com.bancolombia.model.instruction;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class Instruction extends Auditing{
	private UUID id;
	private String code;
	private String name;
	private Boolean active;
}
