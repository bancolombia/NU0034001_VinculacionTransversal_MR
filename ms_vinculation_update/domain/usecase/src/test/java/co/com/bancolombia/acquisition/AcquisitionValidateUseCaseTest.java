package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.documenttype.DocumentTypeUseCase;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRepository;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.prerequisitesstep.PrerequisitesStepUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class AcquisitionValidateUseCaseTest {
    protected static final String CODE_STATE_ACQUISITION_INITIAL = "1";
    protected static final String CODE_STATE_ACQUISITION_COMPLETE = "2";
    protected static final String CODE_STATE_ACQUISITION_MARK = "5";

    @InjectMocks
    @Spy
    AcquisitionValidateUseCaseImpl acquisitionValidateUseCase;

    @Mock
    protected AcquisitionUseCase acquisitionUseCase;

    @Mock
    protected AcquisitionRepository acquisitionRepository;

    @Mock
    protected AcquisitionOperationUseCase acquisitionOperationUseCase;

    @Mock
    protected ParametersUseCase parametersUseCase;

    @Mock
    protected CoreFunctionDate coreFunctionDate;

    @Mock
    protected DocumentTypeUseCase documentTypeUseCase;

    @Mock
    protected PrerequisitesStepUseCase prerequisitesStepUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateInfoSearchTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition).createdDate(new CoreFunctionDate().getDatetime()).build();

        Mockito.doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));
        Mockito.doReturn(null).when(acquisitionValidateUseCase).validityAcquisition(any(Acquisition.class));
        final Optional<Acquisition> ac = acquisitionValidateUseCase
                .validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d", "FS001",
                        "1234","");

        assertNotNull(ac);
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchDifferentDocumentTypeTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition).createdDate(new CoreFunctionDate().getDatetime()).build();

        Mockito.doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));
        Mockito.doReturn(null).when(acquisitionValidateUseCase).validityAcquisition(any(Acquisition.class));
        acquisitionValidateUseCase.validateInfoSearchAndGet(
                "38400000-8cf0-11bd-b23e-10b96e4ef00d", "FS002", "1234","");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchDifferentDocumentNumberTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition).createdDate(new CoreFunctionDate().getDatetime()).build();

        Mockito.doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));
        Mockito.doReturn(null).when(acquisitionValidateUseCase).validityAcquisition(any(Acquisition.class));
        acquisitionValidateUseCase.validateInfoSearchAndGet(
                "38400000-8cf0-11bd-b23e-10b96e4ef00d", "FS001", "12345","");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchNullAcquisitionTest() {
        acquisitionValidateUseCase.validateInfoSearchAndGet(null, "FS001", "12345","");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchEmptyAcquisitionTest() {
        acquisitionValidateUseCase.validateInfoSearchAndGet("", "FS001", "12345","");
    }

    @Test
    public void validateInfoSearchReturnEmptyTest() {
        doNothing().when(acquisitionValidateUseCase).acquisitionValidations(any(), anyString());

        final Optional<Acquisition> ac = acquisitionValidateUseCase.validateInfoSearchAndGet("",
                "FS001", "12345","");

        assertFalse(ac.isPresent());
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchNoTimeValidityTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition)
                .createdDate(new CoreFunctionDate().getDateFromString("01/01/2000", "dd/MM/yyyy")).build();

        Mockito.doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));
        Mockito.doReturn("123").when(acquisitionValidateUseCase).validityAcquisition(any(Acquisition.class));
        acquisitionValidateUseCase.validateInfoSearchAndGet(
                "38400000-8cf0-11bd-b23e-10b96e4ef00d", "FS001", "1234","");
    }

    @Test(expected = ValidationException.class)
    public void validateInfoSearchNoErrorMarkTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_MARK)
                .build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition).createdDate(new CoreFunctionDate().getDatetime()).build();

        Mockito.doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));
        Mockito.doReturn(null).when(acquisitionValidateUseCase).validityAcquisition(any(Acquisition.class));
        final Optional<Acquisition> ac = acquisitionValidateUseCase
                .validateInfoSearchAndGet("38400000-8cf0-11bd-b23e-10b96e4ef00d", "FS001",
                        "1234","");

        assertNotNull(ac);
    }

    @Test
    public void validityAcquisitionTest(){
        Acquisition acquisition = Acquisition.builder()
                .createdDate(new CoreFunctionDate().getDatetime()).build();
        Parameters par = Parameters.builder().code("1440").build();
        Mockito.doReturn(Collections.singletonList(par)).when(parametersUseCase).findByParent(anyString());

        Mockito.doReturn("02:59:00").when(coreFunctionDate)
                .compareDifferenceTime(any(Date.class), anyString(), any(Boolean.class), any(Boolean.class));

        String response = acquisitionValidateUseCase.validityAcquisition(acquisition);
        assertNull(response);
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberTest() {
        DocumentType documentType = DocumentType.builder().code("FS001").build();
        StateAcquisition stateAcquisition =
                StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL).build();
        Acquisition acquisition = Acquisition.builder()
                .documentType(documentType).documentNumber("123")
                .stateAcquisition(stateAcquisition).build();

        List<Acquisition> acquisitionList = Collections.singletonList(acquisition);

        doReturn(acquisitionList).when(acquisitionRepository)
                .findByDocumentTypeAndDocumentNumber(any(DocumentType.class), anyString());

        List<Acquisition> response = acquisitionValidateUseCase
                .findByDocumentTypeAndDocumentNumber(documentType, "123");

        assertFalse(response.isEmpty());
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberCompleteAcquisitionTest() {
        DocumentType documentType = DocumentType.builder().code("FS001").build();
        StateAcquisition stateAcquisition =
                StateAcquisition.builder().code(CODE_STATE_ACQUISITION_COMPLETE).build();
        Acquisition acquisition = Acquisition.builder()
                .documentType(documentType).documentNumber("123")
                .stateAcquisition(stateAcquisition).build();

        List<Acquisition> acquisitionList = Collections.singletonList(acquisition);

        doReturn(acquisitionList).when(acquisitionRepository)
                .findByDocumentTypeAndDocumentNumber(any(DocumentType.class), anyString());

        List<Acquisition> response = acquisitionValidateUseCase
                .findByDocumentTypeAndDocumentNumber(documentType, "123");

        assertTrue(response.isEmpty());
    }
}
