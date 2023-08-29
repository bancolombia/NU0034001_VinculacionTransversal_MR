package co.com.bancolombia.instruction;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.instruction.Instruction;
import co.com.bancolombia.model.instruction.gateways.InstructionRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class InstructionUseCaseTest {

    @InjectMocks
    @Spy
    private InstructionUseCaseImpl instructionUseCase;

    @Mock
    private InstructionRepository instructionRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeTest() {
        Instruction instruction = Instruction.builder().code("001").active(true).build();
        Mockito.doReturn(Optional.of(instruction)).when(this.instructionRepository).findByCode(any(String.class));
        Optional<Instruction> tcc = this.instructionUseCase.findByCode("");
        assertNotNull(tcc);
    }

    @Test
    public void saveInstructionTest() {
        Instruction instruction = Instruction.builder().build();
        Mockito.doReturn(instruction).when(this.instructionRepository).save(any(Instruction.class));
        this.instructionUseCase.saveInstruction(instruction);
        Mockito.verify(this.instructionRepository, Mockito.times(1)).save(any(Instruction.class));
    }

    @Test
    public void testValidateAcquisitionInstructions() {
        final Acquisition acquisition = Acquisition.builder()
                .typeAcquisition(TypeAcquisition.builder().build())
                .build();
        this.instructionUseCase.validateAcquisitionInstructions(acquisition);
        assertNotNull(acquisition);
    }

    @Test(expected = ValidationException.class)
    public void testValidateAcquisitionInstructionsNullAcquisition() {
        final Acquisition acquisition = null;
        this.instructionUseCase.validateAcquisitionInstructions(acquisition);
    }

    @Test(expected = ValidationException.class)
    public void testValidateAcquisitionInstructionsNullAcquisitionType() {
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(null).build();
        this.instructionUseCase.validateAcquisitionInstructions(acquisition);
    }
}
