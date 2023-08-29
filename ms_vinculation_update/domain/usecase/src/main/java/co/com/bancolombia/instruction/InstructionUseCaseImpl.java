package co.com.bancolombia.instruction;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.instruction.Instruction;
import co.com.bancolombia.model.instruction.gateways.InstructionRepository;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PROFILING;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITHOUT_LINK;

@RequiredArgsConstructor
public class InstructionUseCaseImpl implements InstructionUseCase {
    private final InstructionRepository instructionRepository;

    @Override
    public Optional<Instruction> findByCode(String code) {
        return this.instructionRepository.findByCode(code);
    }

    @Override
    public Instruction saveInstruction(Instruction instruction) {
        return this.instructionRepository.save(instruction);
    }

    @Override
    public void validateAcquisitionInstructions(Acquisition acquisition) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        if (acquisition == null) {
            error.put(ERROR_CODE_WITHOUT_LINK, Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
        if (acquisition.getTypeAcquisition() == null) {
            error.put(ERROR_CODE_PROFILING, Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
    }
}
