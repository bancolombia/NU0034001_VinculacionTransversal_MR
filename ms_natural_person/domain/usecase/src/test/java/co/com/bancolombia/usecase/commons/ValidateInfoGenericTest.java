package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.common.validateinfogeneric.ErrorFieldObject;
import co.com.bancolombia.common.validateinfogeneric.ObjectValidation;
import co.com.bancolombia.common.validateinfogeneric.ParamStreamDeclaratedField;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.dependentfield.DependentField;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentArr;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.dependentfield.MyClassDependent;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CONTACT_INF;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class ValidateInfoGenericTest {

    @InjectMocks
    @Spy
    private ValidateInfoGeneric<ContactInformation, ExecFieldReply, DependentFieldUseCase> validateInfoGeneric;

    @Mock
    DependentFieldUseCase dependentFieldUseCase;

    private Acquisition acq;

    private ContactInformation info;

    private PropertyDescriptor[] props;

    private MyClassDependent myClassDependent;

    private DependentArr listDependencia;

    private List<ErrorField> errorFields;
    
    private List<ExecFieldReply> mandatoryExecFList;

    @Before
    public void setUp() {
        validateInfoGeneric = new ValidateInfoGeneric
                <ContactInformation, ExecFieldReply, DependentFieldUseCase>(dependentFieldUseCase);
        MockitoAnnotations.initMocks(this);
        
        acq = Acquisition.builder().id(UUID.randomUUID()).build();
        info = ContactInformation.builder().acquisition(acq).brand("1").build();
        myClassDependent = MyClassDependent.builder().myField("brand").mandatory(true)
                .currentOperation(OPER_CONTACT_INF).currentField("brand")
                .dependentOperation(OPER_CONTACT_INF).dependentField("brand").build();
        try {
            props = Introspector.getBeanInfo(info.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        List<MyClassDependent> listMyClassDependentSame = new ArrayList<>();
        List<MyClassDependent> listMyClassDependentOther = new ArrayList<>();
        listMyClassDependentOther.add(myClassDependent);
        listDependencia = DependentArr.builder().dependentSameOperatation(listMyClassDependentSame)
                .dependentOtherOperatation(listMyClassDependentOther).build();
        errorFields = new ArrayList<>();
        errorFields.add(ErrorField.builder().build());
        mandatoryExecFList = new ArrayList<>();
        mandatoryExecFList.add(ExecFieldReply.builder().build());
    }

    @Test
    public void validateDependencyTest() throws IntrospectionException {
        List<MyClassDependent> list = Collections.singletonList(MyClassDependent.builder()
                .myField("currentField").mandatory(true).build());
        body(list);
        list = Collections.singletonList(MyClassDependent.builder()
                .myField("asd").mandatory(true).build());
        body(list);
    }

    public void body(List<MyClassDependent> myClassDependents) throws IntrospectionException {
        PropertyDescriptor propertyDescriptor =
                new PropertyDescriptor("currentField", DependentField.class);
        propertyDescriptor.setName("currentField");
        List<ErrorField> list2 = validateInfoGeneric
                .validateDependency(myClassDependents, propertyDescriptor, ErrorField.builder().build());
        assertNotNull(list2);
    }

    @Test
    public void validaIfGetValue() throws NoSuchFieldException, IntrospectionException {
        bodyValidaIfGetValue(true);
        bodyValidaIfGetValue(false);
    }

    public void bodyValidaIfGetValue(boolean otherOpe) throws NoSuchFieldException, IntrospectionException {
        Field field = info.getClass().getDeclaredField("addressType");
        ContactInformation economic = ContactInformation.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(economic.getClass()).getPropertyDescriptors();
        List<ExecFieldReply> execFieldReplies = Collections.singletonList(ExecFieldReply.builder().name("addressType").build());

        List<MyClassDependent> myClassDependents = Collections.singletonList(MyClassDependent.builder()
                .myField("addressType").mandatory(true).build());
        ObjectValidation objectValidation = ObjectValidation.builder().aField(field).props(props)
                .listDependenciaSame(myClassDependents).isOtherOperation(otherOpe).build();
        List<ErrorField> errorFields = new ArrayList<>();
        doReturn(errorFields).when(validateInfoGeneric).validateDependency(
                anyList(), any(PropertyDescriptor.class), any(ErrorField.class));
        doReturn(ErrorField.builder().build()).when(validateInfoGeneric).constructErrorField(
                any(ObjectValidation.class), any(MyClassDependent.class), anyString());
        List<ErrorField> errorFields1 = validateInfoGeneric.validaIfGetValue(info, execFieldReplies, objectValidation);
        assertNotNull(errorFields1);
    }

    @Test
    public void validateMandatoryFieldsErrorFiledNullTest() {
        List<ExecFieldReply> mandatoryExecFList = new ArrayList<>();
        String nameList = OPER_CONTACT_INF;
        DependentFieldParamValidator depFieldParamVal = DependentFieldParamValidator.builder().build();
        this.validateInfoGeneric.validateMandatoryFields(info, mandatoryExecFList, nameList, depFieldParamVal);
    }

    @Test(expected = ValidationException.class)
    public void validateMandatoryFieldsErrorFiledNotNullTest() {
        List<ExecFieldReply> mandatoryExecFList = new ArrayList<>();
        String nameList = OPER_CONTACT_INF;
        DependentFieldParamValidator depFieldParamVal = DependentFieldParamValidator.builder().build();
        Mockito.doReturn(errorFields).when(validateInfoGeneric).validateMandatory(info, mandatoryExecFList, nameList, depFieldParamVal);
        this.validateInfoGeneric.validateMandatoryFields(info, mandatoryExecFList, nameList, depFieldParamVal);
    }

    @Test
    public void validateMandatoryTestDepFieldParamValIsNull() {
        List<ExecFieldReply> mandatoryExecFList = new ArrayList<>();
        String nameList = OPER_CONTACT_INF;
        DependentFieldParamValidator depFieldParamVal = DependentFieldParamValidator.builder()
                .acquisition(acq).operation(nameList).dependentObject(info).build();
        DependentArr dependentArr = DependentArr.builder()
                .dependentSameOperatation(Collections.singletonList(MyClassDependent.builder().build()))
                .dependentOtherOperatation(Collections.singletonList(MyClassDependent.builder().build())).build();
        doReturn(dependentArr).when(dependentFieldUseCase).principal(
                anyObject(), any(Acquisition.class), anyString(), anyObject());
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        ErrorFieldObject errorFieldObject = ErrorFieldObject.builder()
                .retListOther(errorFields)
                .retListSame(errorFields).build();
        doReturn(errorFieldObject).when(validateInfoGeneric)
                .streamDeclaratedField(any(ContactInformation.class), anyList(), any(ParamStreamDeclaratedField.class));
        doNothing().when(validateInfoGeneric).validateIsErrorDependentOtherOperation(anyList());


        List<ErrorField> result = this.validateInfoGeneric.validateMandatory(
                info, mandatoryExecFList, nameList, depFieldParamVal);
        assertNotNull(result);
    }

    @Test
    public void streamDeclaratedField() throws IntrospectionException {
        PropertyDescriptor[] props = Introspector.getBeanInfo(info.getClass()).getPropertyDescriptors();
        List<MyClassDependent> myClassDependents = Collections.singletonList(MyClassDependent.builder().build());

        ParamStreamDeclaratedField streamDeclaratedField = ParamStreamDeclaratedField.builder()
                .nameList(OPER_CONTACT_INF).props(props).listDependenciaSame(myClassDependents)
                .listDependenciaOther(myClassDependents).build();
        doReturn(new ArrayList<>()).when(validateInfoGeneric)
                .validaIfGetValue(any(ContactInformation.class), anyList(), anyObject());
        ErrorFieldObject errorFieldObject = validateInfoGeneric
                .streamDeclaratedField(info, new ArrayList<>(), streamDeclaratedField);
        assertNotNull(errorFieldObject);
    }

    @Test
    public void constructErrorField(){
        ObjectValidation objectValidation = ObjectValidation.builder().nameList("asd").build();
        MyClassDependent myClassDependent = MyClassDependent.builder()
                .currentOperation("asd").dependentOperation("asd").currentField("asd").dependentField("asd").build();
        ErrorField errorField = validateInfoGeneric
                .constructErrorField(objectValidation, myClassDependent, null);
        assertNotNull(errorField);
    }

    @Test
    public void constructErrorField2(){
        ObjectValidation objectValidation = ObjectValidation.builder().nameList("asd").build();
        ErrorField errorField = validateInfoGeneric
                .constructErrorField(objectValidation, null, "asd");
        assertNotNull(errorField);
    }


    @Test(expected = ValidationException.class)
    public void validateIsErrorDependentOtherOperation() {
        validateInfoGeneric.validateIsErrorDependentOtherOperation(
                Collections.singletonList(ErrorField.builder().build()));
    }
}