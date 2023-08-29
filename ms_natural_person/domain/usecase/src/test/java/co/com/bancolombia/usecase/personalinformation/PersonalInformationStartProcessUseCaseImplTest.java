package co.com.bancolombia.usecase.personalinformation;


import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCase;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.util.ValidateMandatoryFields;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
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
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class PersonalInformationStartProcessUseCaseImplTest {

    @InjectMocks
    @Spy
    private PersonalInformationStartProcessUseCaseImpl personalInformationStartProcessUseCase;

    @Mock
    private PersonalInformationUseCaseImpl personalInformationUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private ValidateCatalogsPersonalUseCase validateCatalogsPersonalUseCase;

    @Mock
    private ContactInformationUseCase contactInformationUseCase;

    @Mock
    private ValidateMandatoryFields validateMandatoryFields;

    @Mock
    private PersonalInformationRepository personalInformationRepository;

    @Mock
    private MergeUseCase mergeUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validationDateNullTest() {
        this.personalInformationStartProcessUseCase.validationDate(null, "");
        verify(this.personalInformationStartProcessUseCase, times(1)).validationDate(null, "");
    }

    @Test
    public void validationDateTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        this.personalInformationStartProcessUseCase.validationDate(date, "");
        verify(this.personalInformationStartProcessUseCase,times(1)).validationDate(date,"");
    }

    @Test
    public void validationDateMayorTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2099-12-12");
        List<ErrorField> errorFields = this.personalInformationStartProcessUseCase.validationDate(date, "");
        assertNotNull(errorFields);
    }



    @Test
    public void saveNewTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        PersonalInformation personalInformation = PersonalInformation.builder().acquisition(acquisition)
                .cellphone("1111144432323223").birthdate(date)
                .expeditionDate(date).build();
        ExecFieldReply execField = ExecFieldReply.builder().mandatory(false).build();
        ContactInformation contactInformation = ContactInformation.builder().email(personalInformation.getEmail())
                .cellphone(personalInformation.getCellphone())
                .acquisition(acquisition).createdBy("prueba")
                .createdDate(coreFunctionDate.getDatetime()).build();
        EmptyReply emptyReply = EmptyReply.builder().build();

        Mockito.doReturn(new ArrayList<>()).when(personalInformationStartProcessUseCase).validationDate(any(Date.class), anyString());
        Mockito.doReturn(emptyReply).when(validateCatalogsPersonalUseCase).validatePersonalInfoCatalogs(any(PersonalInformation.class));
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(),anyString());
        ChecklistReply checklistReply = ChecklistReply.builder()
                .execFieldList(Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        doReturn(checklistReply).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class),
                anyString());
        Mockito.doReturn(null).when(personalInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doNothing().when(personalInformationUseCase)
                .validateMandatoryFields(any(PersonalInformation.class), any(List.class));
        Mockito.doReturn(new HashMap<>()).when(personalInformationUseCase).validationRepetitionMaxEmail(any(String.class),any(UUID.class));
        Mockito.doReturn(new HashMap<>()).when(personalInformationUseCase).validationRepetitionMaxCellphone(any(String.class),any(UUID.class));

        Mockito.doReturn(contactInformation).when(contactInformationUseCase).save(any(ContactInformation.class));

        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(),anyString());
        Mockito.doReturn(personalInformation).when(personalInformationUseCase).save(any(PersonalInformation.class), any(PersonalInformation.class));

        PersonalInformation personalInformationResponse = this.personalInformationStartProcessUseCase
                .startProcessPersonalInformation(personalInformation);

        assertNotNull(personalInformationResponse);
    }



    @Test(expected = ValidationException.class)
    public void saveUpdateExceptionTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        PersonalInformation personalInformation = PersonalInformation.builder().acquisition(acquisition)
                .cellphone("1111144432323223").birthdate(date)
                .expeditionDate(date).build();
        ExecFieldReply execField = ExecFieldReply.builder().mandatory(false).build();
        ContactInformation contactInformation = ContactInformation.builder().email(personalInformation.getEmail())
                .cellphone(personalInformation.getCellphone())
                .acquisition(acquisition).createdBy("prueba")
                .createdDate(coreFunctionDate.getDatetime()).build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        EmptyReply emptyReply = EmptyReply.builder().build();

        Mockito.doReturn(new ArrayList<>()).when(personalInformationStartProcessUseCase).validationDate(any(Date.class), anyString());
        Mockito.doReturn(emptyReply).when(validateCatalogsPersonalUseCase).validatePersonalInfoCatalogs(any(PersonalInformation.class));

        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(),anyString());
        ChecklistReply checklistReply = ChecklistReply.builder()
                .execFieldList(Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        doReturn(checklistReply).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class),
                anyString());
        Mockito.doReturn(personalInformation).when(personalInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doReturn(errorFields).when(this.mergeUseCase).merge(any(PersonalInformation.class),any(PersonalInformation.class)
                ,any(MergeAttrib.class));

        this.personalInformationStartProcessUseCase.startProcessPersonalInformation(personalInformation);
    }


    @Test(expected = ValidationException.class)
    public void saveDateMayorTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        PersonalInformation personalInformation = PersonalInformation.builder().acquisition(acquisition)
                .cellphone("1111144432323223").birthdate(date)
                .expeditionDate(date).build();

        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());

        Mockito.doReturn(errorFields).when(personalInformationStartProcessUseCase).validationDate(any(Date.class), anyString());

        this.personalInformationStartProcessUseCase.startProcessPersonalInformation(personalInformation);
    }

    @Test
    public void saveExistTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        PersonalInformation personalInformation = PersonalInformation.builder().acquisition(acquisition)
                .cellphone("1111144432323223").birthdate(date)
                .expeditionDate(date).build();
        EmptyReply emptyReply = EmptyReply.builder().build();

        Mockito.doReturn(new ArrayList<>()).when(personalInformationStartProcessUseCase).validationDate(any(Date.class), anyString());
        Mockito.doReturn(emptyReply).when(validateCatalogsPersonalUseCase).validatePersonalInfoCatalogs(any(PersonalInformation.class));
        Mockito.doReturn(personalInformation).when(personalInformationRepository)
                .findByAcquisition(any(Acquisition.class));
        Mockito.doReturn(new ArrayList<ErrorField>()).when(mergeUseCase)
                .merge(any(Mergeable.class), any(Mergeable.class), any(MergeAttrib.class));
        Mockito.doReturn(personalInformation).when(personalInformationUseCase).save(any(PersonalInformation.class), any(PersonalInformation.class));

        PersonalInformation personalInformationResponse = this.personalInformationStartProcessUseCase
                .startProcessPersonalInformation(personalInformation);
        assertNotNull(personalInformationResponse);
    }

    @Test
    public void validateIfRecordUpgradeableFirstTest(){

        PersonalInformation piOld = PersonalInformation.builder().firstName("Pedro").secondName("Pablo")
                .firstSurname("Perez").secondSurname("Perez").build();
        PersonalInformation piNew = PersonalInformation.builder().firstName("Pedro").secondName("Pablo")
                .firstSurname("Perez").secondSurname("Perez").build();
        List<ErrorField> errorFields ;

        errorFields=this.personalInformationStartProcessUseCase.validateIfRecordUpgradeableFirst(piOld,piNew);
        assertNotNull(errorFields);
    }

    @Test
    public void validateIfRecordUpgradeableTest() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        Date dateExp = format.parse("2021-05-12");
        PersonalInformation piOld = PersonalInformation.builder().birthdate(date).expeditionDate(dateExp).build();
        PersonalInformation piNew = PersonalInformation.builder().birthdate(date).expeditionDate(dateExp).build();
        List<ErrorField> errorFields = new ArrayList<ErrorField>();
        errorFields=this.personalInformationStartProcessUseCase.validateIfRecordUpgradeable(errorFields,piOld,piNew);
        assertNotNull(errorFields);
    }

    @Test
    public void validateFieldsPersonalInfoNullTest(){
        PersonalInformation personalInformation = PersonalInformation.builder().build();
        boolean flag;
        flag=personalInformationStartProcessUseCase.validateFieldsPersonalInfoNull(personalInformation);
        Assert.assertTrue(flag);
    }

    @Test
    public void validateFieldsPersonalInfoNoNullTest(){
        PersonalInformation personalInformation = PersonalInformation.builder().cellphone("3001234567")
                .email("email@test.com").expeditionCountry("CO2").expeditionDepartment("ANT")
                .expeditionPlace("5001000").build();
        boolean flag;
        flag=personalInformationStartProcessUseCase.validateFieldsPersonalInfoNull(personalInformation);
        Assert.assertFalse(flag);
    }

    @Test
    public void validateIfRecordUpgradeableFirstNoTest(){
        PersonalInformation piOld = PersonalInformation.builder().firstName("Pedro").secondName("Pablo")
                .firstSurname("Perez").secondSurname("Perez").build();
        PersonalInformation piNew = PersonalInformation.builder().build();

        List<ErrorField> errorFields ;

        errorFields=this.personalInformationStartProcessUseCase.validateIfRecordUpgradeableFirst(piOld,piNew);
        assertNotNull(errorFields);
    }

    @Test
    public void validateIfRecordNoUpgradeableTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        Date dateExp = format.parse("2021-05-12");
        PersonalInformation piOld = PersonalInformation.builder().birthdate(date).expeditionDate(dateExp).build();
        PersonalInformation piNew = PersonalInformation.builder().build();
        List<ErrorField> errorFields = new ArrayList<ErrorField>();
        errorFields=this.personalInformationStartProcessUseCase.validateIfRecordUpgradeable(errorFields,piOld,piNew);
        assertNotNull(errorFields);
    }



}
