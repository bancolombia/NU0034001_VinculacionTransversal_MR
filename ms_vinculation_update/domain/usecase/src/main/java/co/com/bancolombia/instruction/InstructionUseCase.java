package co.com.bancolombia.instruction;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.instruction.Instruction;

import java.util.Optional;

public interface InstructionUseCase {
    public Optional<Instruction> findByCode(String code);

    public Instruction saveInstruction(Instruction instruction);

    public void validateAcquisitionInstructions(Acquisition acquisition);
}
