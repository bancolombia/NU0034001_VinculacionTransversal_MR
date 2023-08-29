package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.documenttype.DocumentTypeUseCase;
import co.com.bancolombia.matrixacquisition.MatrixAcquisitionUseCase;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class AcquisitionUseCaseTest {
    protected static final String CODE_STATE_ACQUISITION_INITIAL = "1";
    protected static final String CODE_STATE_ACQUISITION_COMPLETE = "2";
    protected static final String CODE_STATE_ACQUISITION_PEND_MAR = "5";
    protected static final String CODE_STATE_ACQUISITION_PEND_PUB = "6";
    protected static final String CODE_STATE_ACQUISITION_PEND_NOT = "7";

    @InjectMocks
    @Spy
    AcquisitionUseCaseImpl acquisitionUseCase;

    @Mock
    protected AcquisitionRepository acquisitionRepository;

    @Mock
    protected MatrixAcquisitionUseCase matrixAcquisitionUseCase;

    @Mock
    protected DocumentTypeUseCase documentTypeUseCase;

    @Mock
    protected AcquisitionValidateUseCase acquisitionValidateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setAcquisitionMatricesTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition =
                StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL).build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition).typeAcquisition(null).build();

        doReturn(null).when(this.matrixAcquisitionUseCase).findByTypeAcquisition(null);

        acquisitionUseCase.setAcquisitionMatrices(acquisition);
        assertNull(acquisition.getTypeAcquisition());
    }

    @Test
    public void setAcquisitionMatricesNotNullTest() {
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition =
                StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL).build();
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .documentType(documentType).documentNumber("1234")
                .stateAcquisition(stateAcquisition).typeAcquisition(typeAcquisition)
                .build();

        doReturn(new ArrayList<>()).when(this.matrixAcquisitionUseCase).findByTypeAcquisition(typeAcquisition);

        acquisitionUseCase.setAcquisitionMatrices(acquisition);
        assertNotNull(acquisition.getTypeAcquisition().getAcquisitionMatrices());
    }

    @Test
    public void findAndValidateByIdTest() {
        final UUID idAcquisition = UUID.randomUUID();
        final StateAcquisition stateAcquisition =
                StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL).build();
        final Acquisition acquisition =
                Acquisition.builder().id(idAcquisition).stateAcquisition(stateAcquisition).build();
        Mockito.doReturn(acquisition).when(this.acquisitionRepository).findById(any(UUID.class));
        final Acquisition acq = acquisitionUseCase.findAndValidateById(acquisition.getId());

        assertNotNull(acq);
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberTest() {
        List<Acquisition> acq = null;
        final DocumentType td = DocumentType.builder().build();

        when(this.acquisitionRepository.findByDocumentTypeAndDocumentNumber(
                any(DocumentType.class), any(String.class))).thenReturn(new ArrayList<>());
        acq = acquisitionValidateUseCase.findByDocumentTypeAndDocumentNumber(td, "1000001");

        assertNotNull(acq);
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberFilterTest() {
        List<Acquisition> acq = null;
        final DocumentType td = DocumentType.builder().build();

        when(this.acquisitionRepository.findByDocumentTypeAndDocumentNumber(
                any(DocumentType.class), any(String.class))).thenReturn(new ArrayList<>());
        acq = acquisitionValidateUseCase.findByDocumentTypeAndDocumentNumber(td, "1000001");

        assertNotNull(acq);
    }

    @Test
    public void findAcquisitionTest() {
        final AcquisitionStartObjectModel aSOM = AcquisitionStartObjectModel.builder().build();
        final Optional<Acquisition> aOp = Optional.of(Acquisition.builder().build());

        Mockito.doReturn(aOp).when(this.acquisitionRepository).findAcquisition(any(AcquisitionStartObjectModel.class));
        final Acquisition acq = acquisitionUseCase.findAcquisition(aSOM);

        assertNotNull(acq);
    }

    @Test
    public void findByIdActiveTest() {
        final UUID idAcquisition = UUID.randomUUID();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder().id(idAcquisition).stateAcquisition(stateAcquisition)
                .build();
        Mockito.doReturn(acquisition).when(this.acquisitionRepository).findById(any(UUID.class));
        final Acquisition acq = acquisitionUseCase.findById(acquisition.getId());

        assertNotNull(acq);
    }

    @Test
    public void findByIdPendMarkingTest() {
        final UUID idAcquisition = UUID.randomUUID();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_PEND_MAR)
                .build();
        final Acquisition acquisition = Acquisition.builder().id(idAcquisition).stateAcquisition(stateAcquisition)
                .build();
        Mockito.doReturn(acquisition).when(this.acquisitionRepository).findById(any(UUID.class));
        final Acquisition acq = acquisitionUseCase.findById(acquisition.getId());

        assertNotNull(acq);
    }

    @Test
    public void findByIdPendPublishingTest() {
        final UUID idAcquisition = UUID.randomUUID();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_PEND_PUB)
                .build();
        final Acquisition acquisition = Acquisition.builder().id(idAcquisition).stateAcquisition(stateAcquisition)
                .build();
        Mockito.doReturn(acquisition).when(this.acquisitionRepository).findById(any(UUID.class));
        final Acquisition acq = acquisitionUseCase.findById(acquisition.getId());

        assertNotNull(acq);
    }

    @Test
    public void findByIdPendNotificationTest() {
        final UUID idAcquisition = UUID.randomUUID();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_PEND_NOT)
                .build();
        final Acquisition acquisition = Acquisition.builder().id(idAcquisition).stateAcquisition(stateAcquisition)
                .build();
        Mockito.doReturn(acquisition).when(this.acquisitionRepository).findById(any(UUID.class));
        final Acquisition acq = acquisitionUseCase.findById(acquisition.getId());

        assertNotNull(acq);
    }

    @Test
    public void findByIdTestOrState() {
        final UUID idAcquisition = UUID.randomUUID();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_COMPLETE)
                .build();
        final Acquisition acquisition = Acquisition.builder().id(idAcquisition).stateAcquisition(stateAcquisition)
                .build();
        Mockito.doReturn(acquisition).when(this.acquisitionRepository).findById(any(UUID.class));
        final Acquisition acq = acquisitionUseCase.findById(acquisition.getId());
        assertNull(acq);
    }

    @Test
    public void findByIdTestNull() {
        final Acquisition acquisition = Acquisition.builder().id(null).build();
        Mockito.doReturn(acquisition).when(this.acquisitionRepository).findById(any(UUID.class));
        final Acquisition acq = acquisitionUseCase.findById(acquisition.getId());
        assertNull(acq);
    }

    @Test
    public void getAllByOpAcqIdDocTypeAndDocNumIdNullTest() {
        List<Acquisition> list = Collections.singletonList(Acquisition.builder().build());
        Mockito.doReturn(Optional.of(DocumentType.builder().build())).when(documentTypeUseCase).findByCode(anyString());
        Mockito.doReturn(list).when(acquisitionValidateUseCase)
                .findByDocumentTypeAndDocumentNumber(any(DocumentType.class), anyString());
        List<Acquisition> acquisitionList = acquisitionValidateUseCase
                .getAllByOpAcqIdDocTypeAndDocNum(null, "", "", "");
        assertNotNull(acquisitionList);
    }

    @Test
    public void getAllByOpAcqIdDocTypeAndDocNumTest() {
        UUID idAcq = UUID.randomUUID();
        List<Acquisition> list = Collections.singletonList(Acquisition.builder().build());
        Mockito.doReturn(Optional.of(Acquisition.builder().build())).when(acquisitionValidateUseCase)
                .validateInfoSearchAndGet(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(list).when(acquisitionValidateUseCase)
                .findByDocumentTypeAndDocumentNumber(any(DocumentType.class), anyString());
        List<Acquisition> acquisitionList = acquisitionValidateUseCase
                .getAllByOpAcqIdDocTypeAndDocNum(idAcq, "", "", "");
        assertNotNull(acquisitionList);
    }

    @Test
    public void findByIdAndDocumentTypeAndDocumentNumberTest() {
        doReturn(Optional.of(DocumentType.builder().build())).when(documentTypeUseCase).findByCode(anyString());

        List<Acquisition> acquisitionList = Collections.singletonList(Acquisition.builder().build());
        doReturn(acquisitionList).when(acquisitionRepository)
                .findByIdAndDocumentTypeAndDocumentNumber(any(UUID.class), any(DocumentType.class), anyString());

        Acquisition acquisition = acquisitionUseCase
                .findByIdAndDocumentTypeAndDocumentNumber(UUID.randomUUID(), "FS001", "123");

        assertNotNull(acquisition);
    }

    @Test
    public void findByIdAndDocumentTypeAndDocumentNumberNoDocumentTypeTest() {
        doReturn(Optional.empty()).when(documentTypeUseCase).findByCode(anyString());

        Acquisition acquisition = acquisitionUseCase
                .findByIdAndDocumentTypeAndDocumentNumber(UUID.randomUUID(), "FS001", "123");

        assertNull(acquisition);
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberLocalTest() {
        doReturn(Optional.of(DocumentType.builder().build())).when(documentTypeUseCase).findByCode(anyString());

        List<Acquisition> acquisitionList = Collections.singletonList(Acquisition.builder().build());
        doReturn(acquisitionList).when(acquisitionRepository).findByDocumentTypeAndDocumentNumber(
                any(DocumentType.class), anyString());

        Acquisition acquisition = acquisitionUseCase.findByDocumentTypeAndDocumentNumber("FS001", "123");
        assertNotNull(acquisition);
    }

    @Test
    public void findByDocumentTypeAndDocumentNumberNoDocumentTypeLocalTest() {
        doReturn(Optional.empty()).when(documentTypeUseCase).findByCode(anyString());
        Acquisition acquisition = acquisitionUseCase.findByDocumentTypeAndDocumentNumber("FS001", "123");
        assertNull(acquisition);
    }

    @Test
    public void findByIdWitOutStateTest() {
        doReturn(Acquisition.builder().build()).when(acquisitionRepository).findById(any(UUID.class));
        Acquisition acquisition = acquisitionUseCase.findByIdWitOutState(UUID.randomUUID());
        assertNotNull(acquisition);
    }

    @Test
    public void findByIdWitOutStateNullAcquisitionTest() {
        doReturn(null).when(acquisitionRepository).findById(any(UUID.class));
        Acquisition acquisition = acquisitionUseCase.findByIdWitOutState(UUID.randomUUID());
        assertNull(acquisition);
    }
}
