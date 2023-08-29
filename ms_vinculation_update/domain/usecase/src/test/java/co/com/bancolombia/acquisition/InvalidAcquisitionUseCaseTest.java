package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class InvalidAcquisitionUseCaseTest extends AcquisitionUseCaseTest {

    @Test(expected = ValidationException.class)
    public void findAndValidateByIdAcquisitionNotFoundExceptionTest() {
        final UUID idAcquisition = UUID.randomUUID();
        final StateAcquisition stateAcquisition =
                StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL).build();
        final Acquisition acquisition =
                Acquisition.builder().id(idAcquisition).stateAcquisition(stateAcquisition).build();
        Mockito.doReturn(acquisition).when(this.acquisitionRepository).findById(any(UUID.class));
        acquisitionUseCase.findAndValidateById(null);
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberTest() {
        StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL).build();
        List<Acquisition> acq = Collections.singletonList(Acquisition.builder()
                .stateAcquisition(stateAcquisition).build());
        final DocumentType td = DocumentType.builder().build();
        List<Acquisition> acqResp = new ArrayList<>();

        when(this.acquisitionRepository.findByDocumentTypeAndDocumentNumber(any(DocumentType.class), any(String.class)))
                .thenReturn(acq);
        acqResp = acquisitionValidateUseCase.findByDocumentTypeAndDocumentNumber(td, "1000001");

        assertNotNull(acqResp);
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberNullTest() {
        List<Acquisition> acq = Collections.singletonList(Acquisition.builder().stateAcquisition(null).build());
        final DocumentType td = DocumentType.builder().build();
        List<Acquisition> acqResp = new ArrayList<>();

        when(this.acquisitionRepository.findByDocumentTypeAndDocumentNumber(any(DocumentType.class), any(String.class)))
                .thenReturn(acq);
        acqResp = acquisitionValidateUseCase.findByDocumentTypeAndDocumentNumber(td, "1000001");

        assertNotNull(acqResp);
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberCodeTest() {
        StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_COMPLETE).build();
        List<Acquisition> acq = Collections.singletonList(Acquisition.builder()
                .stateAcquisition(stateAcquisition).build());
        final DocumentType td = DocumentType.builder().build();
        List<Acquisition> acqResp = new ArrayList<>();

        when(this.acquisitionRepository.findByDocumentTypeAndDocumentNumber(any(DocumentType.class), any(String.class)))
                .thenReturn(acq);
        acqResp = acquisitionValidateUseCase.findByDocumentTypeAndDocumentNumber(td, "1000001");

        assertNotNull(acqResp);
    }
}