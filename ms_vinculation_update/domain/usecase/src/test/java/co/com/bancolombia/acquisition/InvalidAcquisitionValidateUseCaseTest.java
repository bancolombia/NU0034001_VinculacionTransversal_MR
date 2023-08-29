package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class InvalidAcquisitionValidateUseCaseTest extends AcquisitionValidateUseCaseTest {

    @Test(expected = ValidationException.class)
    public void validateInfoSearchOrStateThrowsAcquisitionTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition =
                StateAcquisition.builder().code(CODE_STATE_ACQUISITION_COMPLETE).build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition).build();

        Mockito.doReturn(acquisition).when(acquisitionRepository).findById(any(UUID.class));

        acquisitionValidateUseCase.validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d",
                "FS001", "1234", "");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchOrStateErrorThrowsAcquisitionTest() {
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code("5").build();
        UUID id = UUID.randomUUID();
        final Acquisition acquisition = Acquisition.builder().id(id)
                .stateAcquisition(stateAcquisition).build();

        Mockito.doReturn(acquisition).when(this.acquisitionUseCase).findById(any(UUID.class));

        acquisitionValidateUseCase.validateInfoSearchAndGet(id.toString(), "FS001", "1234", "");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchOrAcquisitionErrorThrowsAcquisitionTest() {
        Mockito.doReturn(null).when(acquisitionRepository).findById(any(UUID.class));
        acquisitionValidateUseCase.validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d",
                "FS001", "1234", "");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchThrowsNullAndStateOneTest() {
        Mockito.doReturn(null).when(acquisitionRepository).findById(any(UUID.class));
        acquisitionValidateUseCase.validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d", "", "", "");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchThrowsCatalogNoTypeTest() {
        final DocumentType documentType = DocumentType.builder().code("FZ001").build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234").stateAcquisition(
                        StateAcquisition.builder().code(Numbers.ONE.getNumber()).build()).build();

        Mockito.doReturn(acquisition).when(acquisitionRepository).findById(any(UUID.class));
        acquisitionValidateUseCase.validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d",
                "", "", "");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchThrowsCatalogNoDocumentTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234").stateAcquisition(
                        StateAcquisition.builder().code(Numbers.ONE.getNumber()).build()).build();

        Mockito.doReturn(acquisition).when(acquisitionRepository).findById(any(UUID.class));
        acquisitionValidateUseCase.validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d",
                "FS001", "", "");
    }

    @Test(expected = RuntimeException.class)
    public void validateInfoSearchTestErrorRuntime() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234").build();

        Mockito.doReturn(acquisition).when(acquisitionRepository).findById(any(UUID.class));

        final Optional<Acquisition> ac = acquisitionValidateUseCase
                .validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d", "FS001",
                        "1235", "");
        assertNotNull(ac);
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchAcquisitionValidityTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition).createdDate(new CoreFunctionDate().getDatetime()).build();
        Mockito.doReturn(acquisition).when(acquisitionRepository).findById(any(UUID.class));

        Mockito.doReturn("block").when(acquisitionValidateUseCase).validityAcquisition(any(Acquisition.class));
        Mockito.doNothing().when(acquisitionOperationUseCase).updateAcquisition(any(Acquisition.class), anyString());
        acquisitionValidateUseCase.validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d", "FS001", "1234", "");

    }

    @Test(expected = ValidationException.class)
    public void getAllByOpAcqIdDocTypeAndDocNumNullAcqTest() {
        UUID idAcq = UUID.randomUUID();
        Mockito.doReturn(Optional.empty()).when(acquisitionValidateUseCase).validateInfoSearchAndGet(anyString(),
                anyString(), anyString(), anyString());
        acquisitionValidateUseCase.getAllByOpAcqIdDocTypeAndDocNum(idAcq, "", "", "");
    }

    @Test(expected = ValidationException.class)
    public void getAllByOpAcqIdDocTypeAndDocNumIdEmptyListTest() {
        Mockito.doReturn(Optional.of(DocumentType.builder().build())).when(documentTypeUseCase).findByCode(anyString());
        Mockito.doReturn(new ArrayList<>()).when(acquisitionValidateUseCase)
                .findByDocumentTypeAndDocumentNumber(any(DocumentType.class), anyString());
        acquisitionValidateUseCase.getAllByOpAcqIdDocTypeAndDocNum(null, "", "", "");
    }

    @Test(expected = ValidationException.class)
    public void getAllByOpAcqIdDocTypeAndDocNumIdNullDocumentTest() {
        List<Acquisition> list = Collections.singletonList(Acquisition.builder().build());
        Mockito.doReturn(Optional.empty()).when(documentTypeUseCase).findByCode(anyString());
        List<Acquisition> acquisitionList = acquisitionValidateUseCase
                .getAllByOpAcqIdDocTypeAndDocNum(null, "", "", "");
        assertNotNull(acquisitionList);
    }

    @Test(expected = ValidationException.class)
    public void getAllByOpAcqIdDocTypeAndDocNumIdNullListTest() {
        Mockito.doReturn(Optional.of(DocumentType.builder().build())).when(documentTypeUseCase).findByCode(anyString());
        Mockito.doReturn(null).when(acquisitionValidateUseCase)
                .findByDocumentTypeAndDocumentNumber(any(DocumentType.class), anyString());
        acquisitionValidateUseCase.getAllByOpAcqIdDocTypeAndDocNum(null, "", "", "");
    }
}