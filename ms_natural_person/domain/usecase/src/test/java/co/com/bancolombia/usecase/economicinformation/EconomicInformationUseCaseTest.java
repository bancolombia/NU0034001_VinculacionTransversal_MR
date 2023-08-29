package co.com.bancolombia.usecase.economicinformation;

import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class EconomicInformationUseCaseTest  {

    @InjectMocks
    @Spy
    private EconomicInformationUseCaseImpl economicInformationUseCaseImpl;

    @Mock
    private ValidateCatalogsEconomicUseCase validateCatalogsEconomicUseCase;

    @Mock
    private EconomicInformationRepository economicInformationRepository;

    @Mock
    private MergeUseCase mergeUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ParametersUseCase parametersUseCase;

    @Mock
    private DependentFieldUseCase dependentFieldUseCase;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startProcessEconomicInformationNullTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        EconomicInformation economicInformation = EconomicInformation.builder().acquisition(acquisition).build();
        Mockito.doReturn(null).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doReturn(economicInformation).when(economicInformationRepository).save(any(EconomicInformation.class));
        Mockito.doNothing().when(economicInformationUseCaseImpl).validateMandatory(economicInformation);
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        EconomicInformation infoEconomic = economicInformationUseCaseImpl.startProcessEconomicInformation(economicInformation);
        assertNotNull(infoEconomic);
    }

    @Test
    public void economicActivityMarkTest(){
        String response = economicInformationUseCaseImpl.economicActivityMark("");
        assertNotNull(response);
    }

    @Test
    public void economicActivityMarkNullTest(){
        String response = economicInformationUseCaseImpl.economicActivityMark(null);
        assertNotNull(response);
    }

    @Test
    public void startProcessEconomicInformationNotNullTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        EconomicInformation economicInformation = EconomicInformation.builder().acquisition(acquisition).build();
        Mockito.doReturn(economicInformation).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doReturn(economicInformation).when(economicInformationRepository).save(any(EconomicInformation.class));
        Mockito.doNothing().when(economicInformationUseCaseImpl).validateMandatory(economicInformation);
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        Mockito.doReturn(new ArrayList<>()).when(mergeUseCase)
                .merge(any(Mergeable.class), any(Mergeable.class), any(MergeAttrib.class));
        Mockito.doReturn(null).when(parametersUseCase).findByCodeAndParent(anyString(), anyString());
        economicInformationUseCaseImpl.startProcessEconomicInformation(economicInformation);
        verify(economicInformationUseCaseImpl, times(1)).startProcessEconomicInformation(economicInformation);
    }

    @Test(expected = ValidationException.class)
    public void startProcessEconomicInformationUpErrFieldNullTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        EconomicInformation economicInformation = EconomicInformation.builder().acquisition(acquisition).build();
        List<ErrorField> errorFields = new ArrayList<>();
        Optional<Parameters> parameter = Optional.ofNullable(Parameters.builder().id(UUID.randomUUID()).build());
        errorFields.add(ErrorField.builder().build());
        Mockito.doReturn(economicInformation).when(economicInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doReturn(errorFields).when(mergeUseCase)
                .merge(any(Mergeable.class), any(Mergeable.class), any(MergeAttrib.class));
        Mockito.doReturn(parameter).when(parametersUseCase).findByCodeAndParent(anyString(), anyString());
        economicInformationUseCaseImpl.startProcessEconomicInformation(economicInformation);
        verify(economicInformationUseCaseImpl, times(1)).startProcessEconomicInformation(economicInformation);
    }

    @Test
    public void saveTest(){
        Mockito.doReturn(EconomicInformation.builder().build()).when(economicInformationRepository).save(any(EconomicInformation.class));
        EconomicInformation information = economicInformationUseCaseImpl.save(EconomicInformation.builder().id(UUID.randomUUID()).build());
        assertNotNull(information);
    }

    @Test
    public void findByAcquisition() {
        Acquisition acquisition = Acquisition.builder().build();
        doReturn(EconomicInformation.builder().build()).when(economicInformationRepository)
                .findByAcquisition(any(Acquisition.class));
        Optional<EconomicInformation> economicInformation = economicInformationUseCaseImpl.findByAcquisition(acquisition);
        assertTrue(economicInformation.isPresent());
    }

    @Test
    public void validateMandatoryFieldsTest(){
        ChecklistReply checklistReply = ChecklistReply.builder().build();
        List<ExecFieldReply> execFieldList = new ArrayList<>();
        execFieldList.add(ExecFieldReply.builder().mandatory(true).build());
        checklistReply.setExecFieldList(execFieldList);
        doReturn(checklistReply).when(vinculationUpdateUseCase)
                .checkListStatus(any(UUID.class), anyString());
        doNothing().when(economicInformationUseCaseImpl).validateMandatoryFields(any(EconomicInformation.class)
        , anyList(), anyString(), any(DependentFieldParamValidator.class));
        EconomicInformation economicInformation = EconomicInformation.builder().
                acquisition(Acquisition.builder().id(UUID.randomUUID()).build()).build();
        economicInformationUseCaseImpl.validateMandatory(economicInformation);
        verify(this.economicInformationUseCaseImpl, times(1)).validateMandatory(economicInformation);
    }
}