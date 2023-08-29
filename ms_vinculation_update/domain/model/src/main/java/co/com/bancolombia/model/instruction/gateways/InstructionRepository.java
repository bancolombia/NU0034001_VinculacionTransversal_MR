package co.com.bancolombia.model.instruction.gateways;

import co.com.bancolombia.model.instruction.Instruction;

import java.util.Optional;

public interface InstructionRepository {
	public Optional<Instruction> findByCode(String code);
	public Instruction save(Instruction instruction);

}
