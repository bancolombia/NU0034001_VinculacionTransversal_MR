package co.com.bancolombia.usecase.personalinformation;


import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCase;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.util.ValidateMandatoryFields;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class PersonalInformationUseCaseImplTest {

    @InjectMocks
    @Spy
    private PersonalInformationUseCaseImpl personalInformationUseCase;

    @Mock
    private PersonalInformationRepository personalInformationRepository;

    @Mock
    private ValidateCatalogsPersonalUseCase validateCatalogsPersonalUseCase;

    @Mock
    private ContactInformationUseCase contactInformationUseCase;

    @Mock
    private MergeUseCase mergeUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private ParametersUseCase parametersUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ValidateMandatoryFields validateMandatoryFields;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByAcquisitionTest() {
        Optional<PersonalInformation> pResponse = this.personalInformationUseCase.findByAcquisition(Acquisition.builder().build());
        assertNotNull(pResponse);
    }

    @Test
    public void validateMandatoryFieldsEmptyTest() {
        List<ErrorField> errorFields = new ArrayList<>();
        List<ExecFieldReply> execFields = new ArrayList<>();
        PersonalInformation personalInformation = PersonalInformation.builder().build();

        Mockito.doReturn(errorFields).when(validateMandatoryFields)
                .validateMandatoryFields(any(Object.class), any(List.class));

        this.personalInformationUseCase.validateMandatoryFields(personalInformation, execFields);
        verify(this.personalInformationUseCase, times(1)).validateMandatoryFields(personalInformation, execFields);
    }

    @Test
    public void validateMandatoryFieldsNullTest() {
        List<ExecFieldReply> execFields = new ArrayList<>();
        PersonalInformation personalInformation = PersonalInformation.builder().build();

        Mockito.doReturn(null).when(validateMandatoryFields)
                .validateMandatoryFields(any(Object.class), any(List.class));

        this.personalInformationUseCase.validateMandatoryFields(personalInformation, execFields);
        verify(this.personalInformationUseCase, times(1)).validateMandatoryFields(personalInformation, execFields);
    }

    @Test(expected = ValidationException.class)
    public void validateMandatoryFieldsErrorsTest() {
        List<ErrorField> errorFields = new ArrayList<>();
        List<ExecFieldReply> execFields = new ArrayList<>();
        PersonalInformation personalInformation = PersonalInformation.builder().build();
        errorFields.add(ErrorField.builder().name("error").build());

        Mockito.doReturn(errorFields).when(validateMandatoryFields)
                .validateMandatoryFields(any(Object.class), any(List.class));

        this.personalInformationUseCase.validateMandatoryFields(personalInformation, execFields);
    }

    @Test
    public void validationRepetitionMaxEmailNull(){
        HashMap<String, List<ErrorField>> error = new HashMap<>(
                this.personalInformationUseCase.validationRepetitionMaxEmail(null, null));
        assertNotNull(error);
    }

    @Test
    public void validationRepetitionMaxEmailEmpty(){
        List<ErrorField> errorFields = new ArrayList<>();
        Mockito.doReturn(errorFields).when(parametersUseCase).validationMaxRepeat(any(Long.class),any(String.class));

        HashMap<String, List<ErrorField>> error = new HashMap<>(
                this.personalInformationUseCase.validationRepetitionMaxEmail("null", UUID.randomUUID()));
        assertNotNull(error);
    }

    @Test
    public void validationRepetitionMaxEmail(){
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        Mockito.doReturn(errorFields).when(parametersUseCase).validationMaxRepeat(any(Long.class),any(String.class));

        HashMap<String, List<ErrorField>> error = new HashMap<>(
                this.personalInformationUseCase.validationRepetitionMaxEmail("null", UUID.randomUUID()));
        assertNotNull(error);
    }

    @Test
    public void validationRepetitionMaxCellphoneNull(){
        HashMap<String, List<ErrorField>> error = new HashMap<>(
                this.personalInformationUseCase.validationRepetitionMaxCellphone(null, null));
        assertNotNull(error);
    }

    @Test
    public void validationRepetitionMaxCellphoneEmpty(){
        List<ErrorField> errorFields = new ArrayList<>();
        Mockito.doReturn(errorFields).when(parametersUseCase).validationMaxRepeat(any(Long.class),any(String.class));

        HashMap<String, List<ErrorField>> error = new HashMap<>(
                this.personalInformationUseCase.validationRepetitionMaxCellphone("null", UUID.randomUUID()));
        assertNotNull(error);
    }

    @Test
    public void validationRepetitionMaxCellphone(){
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        Mockito.doReturn(errorFields).when(parametersUseCase).validationMaxRepeat(any(Long.class),any(String.class));

        HashMap<String, List<ErrorField>> error = new HashMap<>(
                this.personalInformationUseCase.validationRepetitionMaxCellphone("null", UUID.randomUUID()));
        assertNotNull(error);
    }

    @Test(expected = ValidationException.class)
    public void saveMaxCellphoneEmailTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        PersonalInformation personalInformation = PersonalInformation.builder().acquisition(acquisition)
                .cellphone("1111144432323223").birthdate(date)
                .expeditionDate(date).build();
        ExecFieldReply execField = ExecFieldReply.builder().mandatory(false).build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        HashMap<String,List<ErrorField>> errors = new HashMap<>();
        errors.put("",errorFields);

        ChecklistReply checklistReply = ChecklistReply.builder()
                .execFieldList(Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        doReturn(checklistReply).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class),
                anyString());
        Mockito.doNothing().when(personalInformationUseCase)
                .validateMandatoryFields(any(PersonalInformation.class), any(List.class));
        Mockito.doReturn(errors).when(personalInformationUseCase).validationRepetitionMaxEmail(any(String.class),any(UUID.class));
        Mockito.doReturn(errors).when(personalInformationUseCase).validationRepetitionMaxCellphone(any(String.class),any(UUID.class));
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class),anyString(),anyString());

        PersonalInformation personalInformationResponse = this.personalInformationUseCase
                .save(personalInformation, personalInformation);
        assertNotNull(personalInformationResponse);
    }

    @Test
    public void saveTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        PersonalInformation personalInformation = PersonalInformation.builder().acquisition(acquisition)
                .cellphone("1111144432323223")
                .build();
        ExecFieldReply execField = ExecFieldReply.builder().mandatory(false).build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        HashMap<String,List<ErrorField>> errors = new HashMap<>();
        ChecklistReply checklistReply = ChecklistReply.builder()
                .execFieldList(Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        doReturn(checklistReply).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class),
                anyString());
        Mockito.doNothing().when(personalInformationUseCase)
                .validateMandatoryFields(any(PersonalInformation.class), any(List.class));
        Mockito.doReturn(errors).when(personalInformationUseCase).validationRepetitionMaxEmail(any(String.class),any(UUID.class));
        Mockito.doReturn(errors).when(personalInformationUseCase).validationRepetitionMaxCellphone(any(String.class),any(UUID.class));
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class),anyString(),anyString());
        doReturn(EmptyReply.builder().build()).when(validateCatalogsPersonalUseCase)
                .validatePersonalInfoCatalogs(any(PersonalInformation.class));
        PersonalInformation personalInformationResponse = this.personalInformationUseCase
                .save(personalInformation, personalInformation);
        verify(personalInformationUseCase,times(1)).save(personalInformation,personalInformation);
    }

    @Test
    public void validationRepetitionMaxEmailTest(){
        String email="email";
        UUID acquisitionId= UUID.randomUUID();
        List<UUID> acquisitionIdList = new ArrayList<>();
        acquisitionIdList.add(UUID.fromString("f0bc028d-82ae-4fec-9bdb-e288fede2887"));
        acquisitionIdList.add(UUID.fromString("cd1e84bd-6e47-4971-ad25-243935aecf89"));
        acquisitionIdList.add(UUID.fromString("935a8126-01f9-49aa-aad3-9d9a62c97b15"));
        acquisitionIdList.add(UUID.fromString("798e7e16-6917-4ecb-ae98-9c2f8792397c"));
        List<ErrorField> errorFields = new ArrayList<>();
        errorFields.add(ErrorField.builder().name("").build());
        Optional<Parameters> parameters = Optional.ofNullable(Parameters.builder().code("3").build());


        Mockito.doReturn(acquisitionIdList).when(personalInformationRepository).findAcquisitionListByEmail(anyString(),any(UUID.class));
        Mockito.doReturn(parameters).when(parametersUseCase).findByNameAndParent(anyString(),anyString());
        Mockito.doReturn(1L).when(vinculationUpdateUseCase).countAcquisitionByState(anyString(),anyList());
        Mockito.doReturn(errorFields).when(parametersUseCase).validationMaxRepeat(any(Long.class),any(String.class));

        assertNotNull(personalInformationUseCase.validationRepetitionMaxEmail(email,acquisitionId));
    }

    @Test
    public void validationRepetitionMaxCellphoneTest(){
        String cellphone="cellphone";
        UUID acquisitionId= UUID.randomUUID();
        List<UUID> acquisitionIdList = new ArrayList<>();
        acquisitionIdList.add(UUID.fromString("f0bc028d-82ae-4fec-9bdb-e288fede2887"));
        acquisitionIdList.add(UUID.fromString("cd1e84bd-6e47-4971-ad25-243935aecf89"));
        acquisitionIdList.add(UUID.fromString("935a8126-01f9-49aa-aad3-9d9a62c97b15"));
        acquisitionIdList.add(UUID.fromString("798e7e16-6917-4ecb-ae98-9c2f8792397c"));
        List<ErrorField> errorFields = new ArrayList<>();
        errorFields.add(ErrorField.builder().name("").build());
        Optional<Parameters> parameters = Optional.ofNullable(Parameters.builder().code("3").build());


        Mockito.doReturn(acquisitionIdList).when(personalInformationRepository).findAcquisitionListByCellphone(anyString(),any(UUID.class));
        Mockito.doReturn(parameters).when(parametersUseCase).findByNameAndParent(anyString(),anyString());
        Mockito.doReturn(1L).when(vinculationUpdateUseCase).countAcquisitionByState(anyString(),anyList());
        Mockito.doReturn(errorFields).when(parametersUseCase).validationMaxRepeat(any(Long.class),any(String.class));

        assertNotNull(personalInformationUseCase.validationRepetitionMaxCellphone(cellphone,acquisitionId));
    }

    @Test
    public void saveTwoTest(){
        PersonalInformation personalInformation = PersonalInformation.builder().build();

        Mockito.doReturn(personalInformation).when(personalInformationRepository).save(any(PersonalInformation.class));
        
        assertNotNull(personalInformationUseCase.save(personalInformation));
    }
}