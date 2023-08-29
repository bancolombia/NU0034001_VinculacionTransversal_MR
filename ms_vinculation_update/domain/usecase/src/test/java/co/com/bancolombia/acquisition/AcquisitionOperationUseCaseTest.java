package co.com.bancolombia.acquisition;

import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.clauseacquisitionchecklist.ClauseAcquisitionChecklistUseCase;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.matrixtypeacquisition.MatrixTypeAcquisitionUseCase;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRepository;
import co.com.bancolombia.model.matrixtypeacquisition.MatrixTypeAcquisition;
import co.com.bancolombia.model.validatesession.ValidateSession;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;
import co.com.bancolombia.stateacquisition.StateAcquisitionUseCase;
import co.com.bancolombia.util.catalog.CatalogUtilUseCase;
import co.com.bancolombia.validatesession.ValidateSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class AcquisitionOperationUseCaseTest {
    protected static final String CODE_STATE_ACQUISITION_INITIAL = "1";
    protected static final String CODE_USER_ACQUISITION_INITIAL = "Usuario1";

    @InjectMocks
    @Spy
    AcquisitionOperationUseCaseImpl acquisitionOperationUseCase;

    @Mock
    protected AcquisitionUseCase acquisitionUseCase;

    @Mock
    protected StateAcquisitionUseCase stateAcquisitionUseCase;

    @Mock
    protected CheckListUseCase checkListUseCase;

    @Mock
    protected ClauseAcquisitionChecklistUseCase checklistClauseUseCase;

    @Mock
    protected MatrixTypeAcquisitionUseCase matrixTypeAcquisitionUseCase;

    @Mock
    protected CatalogUtilUseCase catalogUtilUseCase;

    @Mock
    protected AcquisitionOpeValidateUseCase opeValidateUseCase;

    @Mock
    protected AcquisitionRepository acquisitionRepository;

    @Mock
    protected ValidateSessionUseCase validateSessionUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startAcquisitionTest() {
        final AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder().build();
        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();
        final MatrixTypeAcquisition matrixTypeAcquisition = MatrixTypeAcquisition.builder().build();

        Mockito.doNothing().when(this.catalogUtilUseCase).validateCatalogs(any(AcquisitionRequestModel.class));
        when(this.checkListUseCase.createCheckList(any(UUID.class), anyString())).thenReturn(new ArrayList<>());
        Mockito.doReturn(acquisitionStartObjectModel).when(this.catalogUtilUseCase)
                .transformObjectCatalog(any(AcquisitionRequestModel.class));
        Mockito.doReturn(null).when(acquisitionUseCase)
                .findAcquisition(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(Optional.of(matrixTypeAcquisition)).when(this.matrixTypeAcquisitionUseCase)
                .search(any(AcquisitionStartObjectModel.class));

        final Acquisition acquisitionInstance = Acquisition.builder().build();

        ValidateSessionResponse validateSessionResponse = ValidateSessionResponse.builder().build();
        doReturn(validateSessionResponse).when(opeValidateUseCase).getValidateSessionResponse(
                any(AcquisitionRequestModel.class), anyString());
        Mockito.doReturn(acquisitionInstance).when(acquisitionOperationUseCase)
                .saveAcquisition(any(AcquisitionStartObjectModel.class), anyString());

        doNothing().when(opeValidateUseCase).saveValidateSession(
                any(ValidateSessionResponse.class), any(Acquisition.class), anyString());

        final Acquisition acquisition = acquisitionOperationUseCase
                .startAcquisition(acquisitionRequestModel, "usrMod", Constants.CODE_START_ACQUISITION);

        assertNotNull(acquisition);

    }

    @Test
    public void startAcquisitionNotSave() {

        final AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder().build();
        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();
        final MatrixTypeAcquisition matrixTypeAcquisition = MatrixTypeAcquisition.builder().build();

        Mockito.doNothing().when(this.catalogUtilUseCase).validateCatalogs(any(AcquisitionRequestModel.class));
        Mockito.doReturn(acquisitionStartObjectModel).when(this.catalogUtilUseCase)
                .transformObjectCatalog(any(AcquisitionRequestModel.class));
        Mockito.doReturn(null).when(acquisitionUseCase)
                .findAcquisition(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(Optional.of(matrixTypeAcquisition)).when(this.matrixTypeAcquisitionUseCase)
                .search(any(AcquisitionStartObjectModel.class));

        Mockito.doReturn(null).when(acquisitionOperationUseCase)
                .saveAcquisition(any(AcquisitionStartObjectModel.class),
                        anyString());

        final Acquisition acquisition = acquisitionOperationUseCase
                .startAcquisition(acquisitionRequestModel, "usrMod", Constants.CODE_START_ACQUISITION);
        assertNull(acquisition);
    }

    @Test
    public void saveAcquisitionTest() {

        final StateAcquisition stateAcquisition = StateAcquisition.builder().build();
        Mockito.doReturn(Optional.of(stateAcquisition)).when(this.stateAcquisitionUseCase)
                .findByCode(any(String.class));
        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();

        final Acquisition acquisition = Acquisition.builder()
                .documentType(acquisitionStartObjectModel.getDocumentType())
                .documentNumber(acquisitionStartObjectModel.getDocumentNumber())
                .typePerson(acquisitionStartObjectModel.getTypePerson())
                .typeProduct(acquisitionStartObjectModel.getTypeProduct())
                .typeChannel(acquisitionStartObjectModel.getTypeChannel())
                .businessLine(acquisitionStartObjectModel.getBusinessLine())
                .stateAcquisition(stateAcquisition)
                .typeAcquisition(acquisitionStartObjectModel.getTypeAcquisition())
                .createdBy(CODE_USER_ACQUISITION_INITIAL).createdDate(new Date())
                .build();

        Mockito.doReturn(acquisition).when(this.acquisitionRepository).save(any(Acquisition.class));

        final Acquisition acq = acquisitionOperationUseCase.saveAcquisition(acquisitionStartObjectModel, "usrMod");

        assertNotNull(acq);
    }

    @Test
    public void saveTest() {
        Acquisition acquisition = Acquisition.builder().updatedDate(new Date()).updatedBy("asd").build();
        doReturn(acquisition).when(acquisitionRepository).save(any(Acquisition.class));
        Acquisition acquisition1 = acquisitionOperationUseCase.save(acquisition);
        assertNotNull(acquisition1);
    }

    @Test
    public void updateAcquisitionTest() {
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final String idAcq = "935a98b8-e59c-4d36-9f88-7820346a3895";
        final String documentNumber = "1061000000";
        //final Clause clause = Clause.builder().code(CODE_STATE_ACQUISITION_INITIAL).build();
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition)
                .documentNumber(documentNumber).documentType(documentType)
                .stateAcquisition(stateAcquisition).build();

        Mockito.doReturn(Optional.of(stateAcquisition)).when(stateAcquisitionUseCase).findByCode(any(String.class));

        Mockito.doReturn(acquisition).when(acquisitionRepository).save(any(Acquisition.class));

        acquisitionOperationUseCase.updateAcquisition(acquisition, CODE_STATE_ACQUISITION_INITIAL);

        verify(this.acquisitionRepository, times(1)).save(acquisition);
    }

    @Test
    public void startAcquisitionUpdateOperationTest() {
        final AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder()
                .documentNumber("123").documentType("003").token("123").build();
        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();
        final MatrixTypeAcquisition matrixTypeAcquisition = MatrixTypeAcquisition.builder().build();
        final ValidateSessionResponse validateSessionResponse = ValidateSessionResponse.builder().build();

        Mockito.doNothing().when(this.catalogUtilUseCase).validateCatalogs(any(AcquisitionRequestModel.class));
        when(this.checkListUseCase.createCheckList(any(UUID.class), anyString())).thenReturn(new ArrayList<>());
        Mockito.doReturn(acquisitionStartObjectModel).when(this.catalogUtilUseCase)
                .transformObjectCatalog(any(AcquisitionRequestModel.class));
        Mockito.doReturn(null).when(acquisitionUseCase)
                .findAcquisition(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(Optional.of(matrixTypeAcquisition)).when(this.matrixTypeAcquisitionUseCase)
                .search(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(validateSessionResponse).when(validateSessionUseCase).validateValidSession(anyString(),
                anyString(), anyString(), anyString());

        final Acquisition acquisitionInstance = Acquisition.builder().build();
        final ValidateSession validateSession = ValidateSession.builder().build();

        Mockito.doReturn(acquisitionInstance).when(acquisitionOperationUseCase)
                .saveAcquisition(any(AcquisitionStartObjectModel.class), anyString());
        doNothing().when(opeValidateUseCase).saveValidateSession(
                any(ValidateSessionResponse.class), any(Acquisition.class), anyString());

        final Acquisition acquisition = acquisitionOperationUseCase
                .startAcquisition(acquisitionRequestModel, "usrMod", Constants.CODE_START_UPDATE);

        assertNotNull(acquisition);
    }

    @Test
    public void startAcquisitionUpdateOperationNotSaveSessionTest() {
        final AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder()
                .documentNumber("123").documentType("003").token("123").build();
        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();
        final MatrixTypeAcquisition matrixTypeAcquisition = MatrixTypeAcquisition.builder().build();

        Mockito.doNothing().when(this.catalogUtilUseCase).validateCatalogs(any(AcquisitionRequestModel.class));
        when(this.checkListUseCase.createCheckList(any(UUID.class), anyString())).thenReturn(new ArrayList<>());
        Mockito.doReturn(acquisitionStartObjectModel).when(this.catalogUtilUseCase)
                .transformObjectCatalog(any(AcquisitionRequestModel.class));
        Mockito.doReturn(null).when(acquisitionUseCase)
                .findAcquisition(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(Optional.of(matrixTypeAcquisition)).when(this.matrixTypeAcquisitionUseCase)
                .search(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(null).when(validateSessionUseCase).validateValidSession(anyString(),
                anyString(), anyString(), anyString());

        final Acquisition acquisitionInstance = Acquisition.builder().build();
        final ValidateSession validateSession = ValidateSession.builder().build();

        Mockito.doReturn(null).when(acquisitionOperationUseCase)
                .saveAcquisition(any(AcquisitionStartObjectModel.class), anyString());
        acquisitionOperationUseCase.startAcquisition(acquisitionRequestModel, "usrMod",
                Constants.CODE_START_UPDATE);

        Mockito.verify(validateSessionUseCase, Mockito.never()).saveValidateSession(any(ValidateSessionResponse.class),
                any(Acquisition.class));
    }

    @Test
    public void startAcquisitionUpdateOperationNotSaveTest() {
        final AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder()
                .documentNumber("123").documentType("003").token("123").build();
        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();
        final MatrixTypeAcquisition matrixTypeAcquisition = MatrixTypeAcquisition.builder().build();
        final ValidateSessionResponse validateSessionResponse = ValidateSessionResponse.builder().build();

        Mockito.doNothing().when(this.catalogUtilUseCase).validateCatalogs(any(AcquisitionRequestModel.class));
        Mockito.doReturn(acquisitionStartObjectModel).when(this.catalogUtilUseCase)
                .transformObjectCatalog(any(AcquisitionRequestModel.class));
        Mockito.doReturn(null).when(acquisitionUseCase)
                .findAcquisition(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(Optional.of(matrixTypeAcquisition)).when(this.matrixTypeAcquisitionUseCase)
                .search(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(validateSessionResponse).when(validateSessionUseCase).validateValidSession(anyString(),
                anyString(), anyString(), anyString());

        Mockito.doReturn(null).when(acquisitionOperationUseCase)
                .saveAcquisition(any(AcquisitionStartObjectModel.class),
                        anyString());

        final Acquisition acquisition = acquisitionOperationUseCase
                .startAcquisition(acquisitionRequestModel, "usrMod", Constants.CODE_START_UPDATE);
        assertNull(acquisition);
    }

    @Test
    public void countAcquisitionByStateTest(){
        List<UUID> acquisitionIdList = new ArrayList<>();
        acquisitionIdList.add(UUID.fromString("919a8157-6beb-4627-bc60-c031d384da3c"));
        Long result=1L;

        Mockito.doReturn(result).when(this.acquisitionRepository).countAcquisitionByState(anyString(),anyList());

        Long resultValue = acquisitionOperationUseCase.countAcquisitionByState("1",acquisitionIdList);
        assertNotNull(resultValue);
    }
}
