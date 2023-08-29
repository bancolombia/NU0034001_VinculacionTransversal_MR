package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_RES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_BRAND_TYPE_RES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANY_ADDRESS_TYPE;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ContactValidationUseCaseImplTest {

    @InjectMocks
    @Spy
    ContactValidationUseCaseImpl useCase;

    @Mock
    ContactInformationRepository repository;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    ParametersUseCase parametersUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test//(expected = ValidationException.class)
    public void validateIfErrorFieldTest() {
        this.useCase.validateIfErrorField(new ArrayList<>(), new ArrayList<>());
        verify(this.useCase, times(1)).validateIfErrorField(new ArrayList<>(), new ArrayList<>());
    }

    @Test(expected = ValidationException.class)
    public void validateIfErrorFieldTestExpMandatory() {
        ErrorField errorField = ErrorField.builder().name(ERROR_CODE_MANY_ADDRESS_TYPE).build();
        List<ErrorField> efMandatory = new ArrayList<>();
        List<ErrorField> efMerge = new ArrayList<>();

        efMandatory.add(errorField);
        //efMerge.add(errorField);

        this.useCase.validateIfErrorField(efMandatory, efMerge);
        //verify(this.useCase, times(1)).validateIfErrorField(new ArrayList<>(),new ArrayList<>());
    }

    @Test(expected = ValidationException.class)
    public void validateIfErrorFieldTestExpMerge() {
        ErrorField errorField = ErrorField.builder().name(ERROR_CODE_MANY_ADDRESS_TYPE).build();
        List<ErrorField> efMandatory = new ArrayList<>();
        List<ErrorField> efMerge = new ArrayList<>();

        //efMandatory.add(errorField);
        efMerge.add(errorField);

        this.useCase.validateIfErrorField(efMandatory, efMerge);
        //verify(this.useCase, times(1)).validateIfErrorField(new ArrayList<>(),new ArrayList<>());
    }

    @Test
    public void validateFirstFieldsContactInfoNullTest() {
        ContactInformation contactInformation = ContactInformation.builder().build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertTrue(res);
    }

    @Test
    public void validateFirstFieldsContactInfoSomeInfoNullTest() {
        ContactInformation contactInformation = ContactInformation.builder()
                .city("")
                .brand("")
                .companyName("")
                .updatedBy("")
                .updatedDate(new Date())
                .address("")
                .neighborhood("")
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoSomeInfoNull3Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .updatedBy(null)
                .updatedDate(null)
                .brand(null)
                .companyName(null)
                .city(null)
                .address(null)
                .neighborhood(null)
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertTrue(res);
    }
    @Test
    public void validateFirstFieldsContactInfoSomeInfoNull4Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .updatedBy("")
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoSomeInfoNull5Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .updatedDate(new Date())
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoSomeInfoNull6Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .brand("")
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoSomeInfoNull7Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .companyName("")
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoSomeInfoNull8Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .city("")
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertTrue(res);
    }

    @Test
    public void validateFirstFieldsContactInfoSomeInfoNull9Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .address("")
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoSomeInfoNull10Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .neighborhood("")
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoNullUdpBy() {
        ContactInformation contactInformation = ContactInformation.builder().updatedBy("").build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoNullUdpDate() {
        ContactInformation contactInformation = ContactInformation.builder()
                .updatedDate(new CoreFunctionDate().getDatetime())
                .build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFirstFieldsContactInfoNullAll() {
        ContactInformation contactInformation = ContactInformation.builder().updatedBy("")
                .updatedDate(new CoreFunctionDate().getDatetime())
                .brand("1").companyName("").address("")
                .neighborhood("").build();
        boolean res = this.useCase.validateFirstFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFieldsContactInfoNullTest() {
        ContactInformation contactInformation = ContactInformation.builder().build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertTrue(res);
    }

    @Test
    public void validateFieldsContactInfoNull1Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .city("")
                .build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFieldsContactInfoNull2Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .department("")
                .build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFieldsContactInfoNull3Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .country("")
                .build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFieldsContactInfoNull4Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .phone("")
                .build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFieldsContactInfoNull5Test() {
        ContactInformation contactInformation = ContactInformation.builder()
                .ext("")
                .build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }


    @Test
    public void validateFieldsContactInfoNullUdpBy() {
        ContactInformation contactInformation = ContactInformation.builder().updatedBy("").build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFieldsContactInfoNullCity() {
        ContactInformation contactInformation = ContactInformation.builder().city("").build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateFieldsContactInfoNullUdpByCity() {
        ContactInformation contactInformation = ContactInformation.builder().updatedBy("").city("").build();
        boolean res = this.useCase.validateFieldsContactInfoNull(contactInformation);
        Assert.assertFalse(res);
    }

    @Test
    public void validateIfRecordUpgradeableNull() {
        Optional<PersonalInformation> piOld = Optional.empty();
        Optional<ContactInformation> ci = Optional.empty();

        List<ErrorField> err = this.useCase.validateIfRecordUpgradeable(piOld, ci);

        Assert.assertEquals(err, new ArrayList<>());
    }

    @Test
    public void validateIfRecordUpgradeableEmailAndCellphone() {
        Optional<PersonalInformation> piOld = Optional.of(PersonalInformation.builder().email("aaaa@aa.a")
                .cellphone("3236984471").build());
        Optional<ContactInformation> ci = Optional.of(ContactInformation.builder().email("aaaa@aa.a")
                .cellphone("3236984471").build());

        List<ErrorField> err = this.useCase.validateIfRecordUpgradeable(piOld, ci);

        Assert.assertNotNull(err);
    }

    @Test
    public void validateIfRecordUpgradeableCellphone() {
        Optional<PersonalInformation> piOld = Optional.of(PersonalInformation.builder().email("aaaa1@aa.a")
                .cellphone("3236984471").build());
        Optional<ContactInformation> ci = Optional.of(ContactInformation.builder().email("aaaa@aa.a")
                .cellphone("3236984471").build());
        List<ErrorField> err = this.useCase.validateIfRecordUpgradeable(piOld, ci);

        Assert.assertNotNull(err);
    }

    @Test
    public void validateIfRecordUpgradeableEmail() {
        Optional<PersonalInformation> piOld = Optional.of(PersonalInformation.builder().email("aaaa@aa.a")
                .cellphone("3236984472").build());
        Optional<ContactInformation> ci = Optional.of(ContactInformation.builder().email("aaaa@aa.a")
                .cellphone("3236984471").build());
        List<ErrorField> err = this.useCase.validateIfRecordUpgradeable(piOld, ci);

        Assert.assertNotNull(err);
    }

    @Test
    public void validateForeingCountryEmptyList() {
        List<ContactInformation> cInfo = new ArrayList<>();
        List<Parameters> pa = new ArrayList<>();

        Mockito.doReturn(new ArrayList<>()).when(parametersUseCase).findByParent("countryResidence");

        this.useCase.validateForeignCountry(cInfo);
    }

    @Test
    public void validateForeingCountryNoEmptyListCountry() {
        List<ContactInformation> cInfo = Collections.singletonList(
                ContactInformation.builder().addressType("Z002").country("CO").build());
        List<Parameters> pa = Collections.singletonList(Parameters.builder().code("CO").name("Colombia").build());

        Mockito.doReturn(pa).when(parametersUseCase).findByParent("countryResidence");

        this.useCase.validateForeignCountry(cInfo);
    }

    @Test
    public void validateForeingCountryNoEmptyListAddressType() {
        List<ContactInformation> cInfo = Collections.singletonList(
                ContactInformation.builder().addressType(CO_ADDRESS_TYPE_RES).country("CO").build());
        List<Parameters> pa = Collections.singletonList(Parameters.builder().code("CO").name("Colombia").build());

        Mockito.doReturn(pa).when(parametersUseCase).findByParent("countryResidence");

        this.useCase.validateForeignCountry(cInfo);
    }

    @Test(expected = ValidationException.class)
    public void validateForeingCountryNoEmptyListAddressTypeCountry() {
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).country("AR").build());
        List<Parameters> pa = Collections.singletonList(Parameters.builder().code("CO").name("Colombia").build());

        Mockito.doReturn(pa).when(parametersUseCase).findByParent("countryResidence");

        this.useCase.validateForeignCountry(cInfo);
    }

    @Test
    public void maxRepetitionEmailCellphone() {
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        ContactInformation cInfo = ContactInformation.builder().acquisition(acquisition).build();

        this.useCase.maxRepetitionEmailCellphone(cInfo);
        assertNotNull(cInfo);

    }

    @Test
    public void maxRepetitionEmailCellphoneEmailNotNull() {
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        ContactInformation cInfo = ContactInformation.builder().acquisition(acquisition).email("aaaa@aa.a").build();

        Long aLong = new Long("1");

        List<ErrorField> err = new ArrayList<>();

        Mockito.doReturn(err).when(parametersUseCase).validationMaxRepeat(any(Long.class), any(String.class));
        this.useCase.maxRepetitionEmailCellphone(cInfo);
        assertNotNull(cInfo);
    }

    @Test
    public void maxRepetitionEmailCellphoneNotNull() {
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        ContactInformation cInfo = ContactInformation.builder().acquisition(acquisition).cellphone("3206053987")
                .build();

        Long aLong = new Long("1");

        List<ErrorField> err = new ArrayList<>();

        Mockito.doReturn(err).when(parametersUseCase).validationMaxRepeat(any(Long.class), any(String.class));
        this.useCase.maxRepetitionEmailCellphone(cInfo);
        assertNotNull(cInfo);
    }

    @Test
    public void maxRepetitionEmailCellphoneEmailNotNullCellphoneNotNull() {
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        ContactInformation cInfo = ContactInformation.builder().acquisition(acquisition).email("aaaa@aa.a")
                .cellphone("3206053987").build();

        Long aLong = new Long("1");

        List<ErrorField> err = new ArrayList<>();

        Mockito.doReturn(err).when(parametersUseCase).validationMaxRepeat(any(Long.class), any(String.class));
        this.useCase.maxRepetitionEmailCellphone(cInfo);
        assertNotNull(cInfo);

    }

    @Test
    public void existsTypeAddsessResBrandTestTrue(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).brand(CO_BRAND_TYPE_RES).build());

        List<ContactInformation> cInfoOld = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType("Z002").brand("0").build());

        Mockito.doNothing().when(this.useCase).validateBrand(null,null);

        boolean res = this.useCase.existsTypeAddressResBrand(acquisition,cInfo,cInfoOld);

        Assert.assertTrue(res);
    }

    @Test
    public void existsTypeAddsessResBrandTestTrue2(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType("").brand(CO_BRAND_TYPE_RES).build());

        List<ContactInformation> cInfoOld = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType("Z002").brand("0").build());

        Mockito.doNothing().when(this.useCase).validateBrand(null,null);
        Mockito.doReturn(true).when(this.useCase).validateFieldsContactInfoNull(null);

        boolean res = this.useCase.existsTypeAddressResBrand(acquisition,cInfo,cInfoOld);

        Assert.assertFalse(res);
    }

    @Test
    public void existsTypeAddsessResBrandTestTrue3(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType("").brand(CO_BRAND_TYPE_RES).build());

        List<ContactInformation> cInfoOld = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType("Z002").brand("0").build());

        Mockito.doNothing().when(this.useCase).validateBrand(null,null);
        Mockito.doReturn(false).when(this.useCase).validateFieldsContactInfoNull(null);

        boolean res = this.useCase.existsTypeAddressResBrand(acquisition,cInfo,cInfoOld);

        Assert.assertFalse(res);
    }

    @Test
    public void existsTypeAddsessResBrandTestFalse(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType("Z002").brand(CO_BRAND_TYPE_RES).build());

        List<ContactInformation> cInfoOld = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType("Z002").city("")
                        .brand(CO_BRAND_TYPE_RES).build());

        Mockito.doNothing().when(this.useCase).validateBrand(null,null);

        boolean res = this.useCase.existsTypeAddressResBrand(acquisition,cInfo,cInfoOld);

        Assert.assertFalse(res);
    }

    @Test
    public void existsTypeAddsessResBrandTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType("Z002").brand("0").build());
        //cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType("Z001").country("CO").brand
        // ("1").build());

        List<ContactInformation> cInfoOld = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).city("")
                        .brand(CO_BRAND_TYPE_RES).build());

        Mockito.doNothing().when(this.useCase).validateBrand(null,null);

        boolean res = this.useCase.existsTypeAddressResBrand(acquisition,cInfo,cInfoOld);

        Assert.assertTrue(res);
    }

    @Test
    public void existsTypeAddsessResBrandTest1(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).brand(CO_BRAND_TYPE_RES).build());
        //cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType("Z001").country("CO").brand
        // ("1").build());

        List<ContactInformation> cInfoOld = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType("Z002").city("").brand("1").build());

        Mockito.doNothing().when(this.useCase).validateBrand(null,null);

        boolean res = this.useCase.existsTypeAddressResBrand(acquisition,cInfo,cInfoOld);

        Assert.assertTrue(res);
    }

    @Test
    public void existsTypeAddsessResBrandTest2(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).brand(CO_BRAND_TYPE_RES).build());
        //cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType("Z001").country("CO").brand
        // ("1").build());

        List<ContactInformation> cInfoOld = Collections.singletonList(
                ContactInformation.builder().acquisition(acquisition).addressType("Z002").brand("1").build());

        Mockito.doNothing().when(this.useCase).validateBrand(null,null);

        boolean res = this.useCase.existsTypeAddressResBrand(acquisition,cInfo,cInfoOld);

        Assert.assertTrue(res);
    }

    @Test(expected = ValidationException.class)
    public void validateAddressTypeTooMany(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).brand(CO_BRAND_TYPE_RES).build());
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).brand(CO_BRAND_TYPE_RES).build());

        this.useCase.validateAddressType(cInfo);
    }

    @Test
    public void validateAddressTypeEmail(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType("Z002").email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());

        this.useCase.validateAddressType(cInfo);
    }

    @Test
    public void validateAddressTypeEmail1(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                "0").build());

        this.useCase.validateAddressType(cInfo);
    }

    @Test
    public void validateAddressTypeEmail2(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());

        this.useCase.validateAddressType(cInfo);
    }

    @Test
    public void validateBrand(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());

        this.useCase.validateBrand(cInfo,new ArrayList<>());
        assertNotNull(cInfo);
    }

    @Test
    public void validateBrand0(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                "0").build());

        List<ContactInformation> cInfoOld = new ArrayList<>();
        cInfoOld.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());
        cInfoOld.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());

        this.useCase.validateBrand(cInfo,cInfoOld);
        assertNotNull(cInfo);
    }

    @Test(expected = ValidationException.class)
    public void validateBrand1(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());

        this.useCase.validateBrand(cInfo,new ArrayList<>());
    }

    @Test(expected = ValidationException.class)
    public void validateBrand2(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());

        List<ContactInformation> cInfoOld = new ArrayList<>();
        cInfoOld.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());
        cInfoOld.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());

        this.useCase.validateBrand(cInfo,cInfoOld);
    }

    @Test(expected = ValidationException.class)
    public void validateBrand3(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                "0").build());
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                "0").build());

        this.useCase.validateBrand(cInfo,new ArrayList<>());
    }

    @Test
    public void validateBrand4(){
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .build();
        List<ContactInformation> cInfo = new ArrayList<>();
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                "0").build());
        cInfo.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                "0").build());

        List<ContactInformation> cInfoOld = new ArrayList<>();
        cInfoOld.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());
        cInfoOld.add(ContactInformation.builder().acquisition(acquisition).addressType(CO_ADDRESS_TYPE_RES).email("aaaa@aa.a").brand(
                CO_BRAND_TYPE_RES).build());

        this.useCase.validateBrand(cInfo,cInfoOld);
        assertNotNull(cInfo);
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


        Mockito.doReturn(acquisitionIdList).when(repository).findAcquisitionListByEmail(anyString(),any(UUID.class),anyString());
        Mockito.doReturn(parameters).when(parametersUseCase).findByNameAndParent(anyString(),anyString());
        Mockito.doReturn(1L).when(vinculationUpdateUseCase).countAcquisitionByState(anyString(),anyList());
        Mockito.doReturn(errorFields).when(parametersUseCase).validationMaxRepeat(any(Long.class),any(String.class));

        assertNotNull(useCase.validationRepetitionMaxEmail(email,acquisitionId));
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
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().name("").build());
        Optional<Parameters> parameters = Optional.ofNullable(Parameters.builder().code("3").build());

        Mockito.doReturn(acquisitionIdList).when(repository).findAcquisitionListByCellphone(anyString(),any(UUID.class),anyString());
        Mockito.doReturn(parameters).when(parametersUseCase).findByNameAndParent(anyString(),anyString());
        Mockito.doReturn(1L).when(vinculationUpdateUseCase).countAcquisitionByState(anyString(),anyList());
        Mockito.doReturn(errorFields).when(parametersUseCase).validationMaxRepeat(any(Long.class),any(String.class));

        assertNotNull(useCase.validationRepetitionMaxCellphone(cellphone,acquisitionId));
    }

    @Test(expected = ValidationException.class)
    public void maxRepetitionEmailCellphoneTest(){
        Acquisition acq = Acquisition.builder().id(UUID.randomUUID()).build();
        ContactInformation contactInformation = ContactInformation.builder().acquisition(acq).cellphone("").email("").build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().name("").build());
        Mockito.doReturn(errorFields).when(this.useCase).validationRepetitionMaxEmail(anyString(),any(UUID.class));
        Mockito.doReturn(errorFields).when(this.useCase).validationRepetitionMaxCellphone(anyString(),any(UUID.class));

        this.useCase.maxRepetitionEmailCellphone(contactInformation);
        verify(useCase,times(1)).maxRepetitionEmailCellphone(contactInformation);
    }

    @Test
    public void validationRepetitionMaxEmailNullTest(){
        UUID acquisitionId= UUID.randomUUID();
        assertNotNull(useCase.validationRepetitionMaxEmail(null,acquisitionId));
    }

    @Test
    public void validationRepetitionMaxCellphoneNullTest(){
        UUID acquisitionId= UUID.randomUUID();
        assertNotNull(useCase.validationRepetitionMaxCellphone(null,acquisitionId));
    }

}
