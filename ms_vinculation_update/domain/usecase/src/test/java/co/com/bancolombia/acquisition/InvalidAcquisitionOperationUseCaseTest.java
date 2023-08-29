package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.validatesession.ValidateSession;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class InvalidAcquisitionOperationUseCaseTest extends AcquisitionOperationUseCaseTest {

    @Test(expected = ValidationException.class)
    public void startAcquisitionThrowFindAcquisitionTest() {
        final AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder().build();
        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();
        final Acquisition acquisition = Acquisition.builder().build();

        Mockito.doNothing().when(this.catalogUtilUseCase).validateCatalogs(any(AcquisitionRequestModel.class));
        Mockito.doReturn(acquisitionStartObjectModel).when(this.catalogUtilUseCase)
                .transformObjectCatalog(any(AcquisitionRequestModel.class));
        Mockito.doReturn(acquisition).when(acquisitionUseCase)
                .findAcquisition(any(AcquisitionStartObjectModel.class));

        acquisitionOperationUseCase.startAcquisition(acquisitionRequestModel, "usrMod", Constants.CODE_START_ACQUISITION);
        assertNotNull(acquisition);
    }

    @Test(expected = ValidationException.class)
    public void startAcquisitionThrowTypeAcquisitionNoPresentTest() {
        final AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder().build();
        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();
        Acquisition acquisition;

        Mockito.doNothing().when(this.catalogUtilUseCase).validateCatalogs(any(AcquisitionRequestModel.class));
        Mockito.doReturn(acquisitionStartObjectModel).when(this.catalogUtilUseCase)
                .transformObjectCatalog(any(AcquisitionRequestModel.class));
        Mockito.doReturn(null).when(acquisitionUseCase)
                .findAcquisition(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(Optional.empty()).when(this.matrixTypeAcquisitionUseCase)
                .search(any(AcquisitionStartObjectModel.class));

        acquisition = acquisitionOperationUseCase.startAcquisition(acquisitionRequestModel, "usrMod", Constants.CODE_START_ACQUISITION);
        assertNotNull(acquisition);
    }

    @Test(expected = ValidationException.class)
    public void saveAcquisitionThrowTest() {
        final AcquisitionStartObjectModel aSOM = AcquisitionStartObjectModel.builder().build();
        Mockito.doReturn(Optional.ofNullable(null)).when(this.stateAcquisitionUseCase).findByCode(any(String.class));
        acquisitionOperationUseCase.saveAcquisition(aSOM,  "usrMod");
    }

    @Test(expected = ValidationException.class)
    public void updateAcquisitionCatalogNotFoundTest() {
        final StateAcquisition stateAcquisition =
                StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL).build();
        final String documentNumber = "1061000000";
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition)
                .documentNumber(documentNumber).documentType(documentType)
                .stateAcquisition(stateAcquisition).build();

        Mockito.doReturn(Optional.of(stateAcquisition)).when(stateAcquisitionUseCase).findByCode(any(String.class));
        Mockito.doReturn(acquisition).when(acquisitionRepository).save(any(Acquisition.class));

        acquisitionOperationUseCase.updateAcquisition(acquisition, null);
    }

    @Test(expected = ValidationException.class)
    public void startAcquisitionUpdateOperationNotValidSessionTest() {
        final AcquisitionRequestModel acquisitionRequestModel = AcquisitionRequestModel.builder()
                .documentNumber("123").documentType("003").token("123").build();

        final AcquisitionStartObjectModel acquisitionStartObjectModel = AcquisitionStartObjectModel.builder().build();

        Mockito.doNothing().when(this.catalogUtilUseCase).validateCatalogs(any(AcquisitionRequestModel.class));
        when(this.checkListUseCase.createCheckList(any(UUID.class), anyString())).thenReturn(new ArrayList<>());
        Mockito.doReturn(acquisitionStartObjectModel).when(this.catalogUtilUseCase)
                .transformObjectCatalog(any(AcquisitionRequestModel.class));
        Mockito.doReturn(null).when(acquisitionUseCase)
                .findAcquisition(any(AcquisitionStartObjectModel.class));
        Mockito.doReturn(Optional.ofNullable(null)).when(this.matrixTypeAcquisitionUseCase)
                .search(any(AcquisitionStartObjectModel.class));

        Mockito.when(validateSessionUseCase.validateValidSession(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(ValidationException.class);

        final Acquisition acquisitionInstance = Acquisition.builder().build();
        final ValidateSession validateSession = ValidateSession.builder().build();

        Mockito.doReturn(acquisitionInstance).when(acquisitionOperationUseCase)
                .saveAcquisition(any(AcquisitionStartObjectModel.class), anyString());
        Mockito.doReturn(validateSession).when(validateSessionUseCase).saveValidateSession(
                any(ValidateSessionResponse.class), any(Acquisition.class));

        acquisitionOperationUseCase.startAcquisition(acquisitionRequestModel, "usrMod",
                Constants.CODE_START_UPDATE);
    }
}
