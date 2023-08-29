package co.com.bancolombia.usecase.basicinformation;

import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.model.basicinformation.gateways.BasicInformationRepository;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.util.ValidateMandatoryFields;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class BasicInformationUseCaseTest {

    @InjectMocks
    @Spy
    private BasicInformationUseCase basicInformationUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;
    @Mock
    private ValidateCatalogsBasicUseCase validateCatalogsBasicUseCase;
    @Mock
    private ValidateMandatoryFields checklistValidateMandatoryFields;
    @Mock
    private MergeUseCase mergeUseCase;
    @Mock
    private BasicInformationRepository basicInformationRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveTest() {
        BasicInformation basicInformation = BasicInformation.builder().build();
        doReturn(basicInformation).when(basicInformationRepository).save(any(BasicInformation.class));
        BasicInformation basicInformationResponse = this.basicInformationUseCase.save(basicInformation);
        assertNotNull(basicInformationResponse);
    }

    @Test
    public void findByAcquisition() {
        BasicInformation basicInformation = BasicInformation.builder().build();
        doReturn(basicInformation)
                .when(basicInformationRepository).findByAcquisition(any(Acquisition.class));
        Optional<BasicInformation> basicInformationResponse = basicInformationUseCase
                .findByAcquisition(Acquisition.builder().build());
        assertNotNull(basicInformationResponse);
    }

    @Test
    public void startProcessNewBasicInformationTest() {
        doReturn(EmptyReply.builder().build()).when(validateCatalogsBasicUseCase)
                .validateBasicInfoCatalogs(any(BasicInformation.class));

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        doReturn(null).when(basicInformationRepository).findByAcquisition(any(Acquisition.class));

        ExecFieldReply execField = ExecFieldReply.builder().mandatory(true).build();
        ChecklistReply reply = ChecklistReply.builder().execFieldList(Collections.singletonList(execField)).build();
        doReturn(reply).when(vinculationUpdateUseCase)
                .checkListStatus(any(UUID.class), anyString());
        doNothing().when(basicInformationUseCase).validateMandatoryFields(any(BasicInformation.class), any(List.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        BasicInformation basicInformation = BasicInformation.builder().acquisition(acquisition).build();
        doReturn(basicInformation).when(basicInformationUseCase).save(any(BasicInformation.class));
        basicInformationUseCase.startProcessBasicInformation(basicInformation);
        verify(basicInformationUseCase, times(1))
                .startProcessBasicInformation(basicInformation);
    }

    @Test
    public void startProcessNewBasicInformationNotNullTest() {
        doReturn(EmptyReply.builder().build()).when(validateCatalogsBasicUseCase)
                .validateBasicInfoCatalogs(any(BasicInformation.class));
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();

        BasicInformation basicInformation = BasicInformation.builder().acquisition(acquisition).build();
        doReturn(basicInformation).when(basicInformationRepository).findByAcquisition(any(Acquisition.class));
        doReturn(true).when(basicInformationUseCase)
                .validateFieldsBasicInfoNull(any(BasicInformation.class), any(BasicInformation.class));
        doNothing().when(basicInformationUseCase).showException(anyString(), any(List.class));

        doReturn(MergeAttrib.builder().build()).when(basicInformationUseCase).constructMergeObject(anyBoolean());
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        doReturn(errorFields).when(mergeUseCase)
                .merge(any(Mergeable.class), any(Mergeable.class), any(MergeAttrib.class));


        ExecFieldReply execField = ExecFieldReply.builder().mandatory(true).build();
        ChecklistReply reply = ChecklistReply.builder().execFieldList(Collections.singletonList(execField)).build();
        doReturn(reply).when(vinculationUpdateUseCase)
                .checkListStatus(any(UUID.class), anyString());
        doNothing().when(basicInformationUseCase).validateMandatoryFields(any(BasicInformation.class), any(List.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        this.basicInformationUseCase.startProcessBasicInformation(basicInformation);
        verify(this.basicInformationUseCase, times(1)).startProcessBasicInformation(basicInformation);
    }

    @Test(expected = ValidationException.class)
    public void validateMandatoryFields() {
        List<ExecFieldReply> execFields = new ArrayList<>();
        BasicInformation basicInformation = BasicInformation.builder().build();

        doReturn(Collections.singletonList(ErrorField.builder().build())).when(checklistValidateMandatoryFields)
               .validateMandatoryFields(any(Object.class), any(List.class));
        basicInformationUseCase.validateMandatoryFields(basicInformation, execFields);
    }

    @Test(expected = ValidationException.class)
    public void showException() {
        basicInformationUseCase.showException("asd", Collections.singletonList(ErrorField.builder().build()));
    }

    @Test
    public void constructMergeObject() {
        MergeAttrib mergeAttrib = MergeAttrib.builder().isRecordUpgradeable(true).stepCode("BASCINFO").build();
        MergeAttrib mergeAttrib1 = basicInformationUseCase.constructMergeObject(true);
        assertEquals(mergeAttrib, mergeAttrib1);
    }

    @Test
    public void validateFieldsBasicInfoNull() {
        BasicInformation basicInformation = BasicInformation.builder()
                .birthCity(null).birthDepartment(null).country(null).build();
        doReturn(true).when(basicInformationUseCase)
                .validateFieldsBasicInfoNullComplement(any(BasicInformation.class));
        doReturn(true).when(basicInformationUseCase)
                .validateFieldsBasicInfoNullSupplementary(any(BasicInformation.class));
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        doReturn(errorFields).when(basicInformationUseCase)
                .validateIfRecordUpgradeable(any(BasicInformation.class), any(BasicInformation.class));
        doNothing().when(basicInformationUseCase).showException(anyString(), any(List.class));

        boolean res = basicInformationUseCase.validateFieldsBasicInfoNull(basicInformation, basicInformation);
        assertTrue(res);
    }

    @Test
    public void validateFieldsBasicInfoNullComplement() {
        BasicInformation basicInformation = BasicInformation.builder()
                .educationLevel(null).housingType(null).entryCompanyDate(null).nationality(null)
                .pep(null).socialStratum(null).build();
        boolean res = basicInformationUseCase.validateFieldsBasicInfoNull(basicInformation, basicInformation);
        assertTrue(res);
    }

    @Test
    public void validateFieldsBasicInfoNullSupplementary() {
        BasicInformation basicInformation = BasicInformation.builder()
                .civilStatus(null).contractType(null).dependants(null).build();
        boolean res = basicInformationUseCase.validateFieldsBasicInfoNull(basicInformation, basicInformation);
        assertTrue(res);
    }

    @Test
    public void validateIfRecordUpgradeable() {
        BasicInformation basicInformation = BasicInformation.builder().gender("asd").build();
        List<ErrorField> errorFields = basicInformationUseCase
                .validateIfRecordUpgradeable(basicInformation, basicInformation);
        assertNotNull(errorFields);
    }
}