package co.com.bancolombia.usecase.dependentfield;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.dependentfield.DependentField;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.dependentfield.DependentArr;
import co.com.bancolombia.dependentfield.DependentFieldObject;
import co.com.bancolombia.dependentfield.MyClass;
import co.com.bancolombia.dependentfield.MyClassDependent;
import co.com.bancolombia.dependentfield.ObjectParamValidateInverse;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.C_NOT_NULL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.C_NULL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

public class DependentFieldUseCaseTest {

	@InjectMocks
	@Spy
	DependentFieldUseCase dependentFieldUseCase;

	@Mock
	private VinculationUpdateUseCase vinculationUpdateUseCase;

	private Acquisition acquisition;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		acquisition = Acquisition.builder().id(UUID.randomUUID())
		        .typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build()).build();
	}

	private static final String currentOpe = "contact-information";

	@Test
	public void getMapField() {
		List<MyClass> dependentFields1 = dependentFieldUseCase
				.getMapField(ForeignInformation.builder().build());
		assertNotNull(dependentFields1);
	}

	@Test
	public void getAllDependentFields() {
		List<MyClass> map = Collections.singletonList(MyClass.builder().myKey("asd").build());
		DependentFieldReply dependentFields = DependentFieldReply.builder()
				.dependentFields(Collections.singletonList(DependentField.builder().build())).build();
		doReturn(dependentFields).when(vinculationUpdateUseCase)
				.dependentFieldNormal(anyString(), anyString(), anyList(), anyBoolean());
		List<DependentField> dependentFields1 = dependentFieldUseCase
				.getAllDependentFields("asd", map, "asd");
		assertNotNull(dependentFields1);
	}

	@Test
	public void validateBigDecimal() throws IntrospectionException {
		EconomicInformation economic = EconomicInformation.builder().annualSales(BigDecimal.ONE).build();
		DependentField dependentField = DependentField.builder().dependentValue("1").build();
		PropertyDescriptor[] props = Introspector.getBeanInfo(economic.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("name").myKey("name").myMethod(props[1]).build();
		boolean b = dependentFieldUseCase.validateBigDecimal(
				BigDecimal.class, dependentField, mapped, economic);
		assertTrue(b);
	}

	@Test
	public void validateBigDecimal2() throws IntrospectionException {
		DependentField dependentField = DependentField.builder().dependentValue("name").build();
		PropertyDescriptor[] props = Introspector.getBeanInfo(dependentField.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("name").myKey("name").myMethod(props[0]).build();
		boolean b = dependentFieldUseCase.validateBigDecimal(
				dependentField.getClass(), dependentField, mapped, dependentField);
		assertFalse(b);
	}

	@Test
	public void validateValueInDependent() throws IntrospectionException {
		body(1, C_NOT_NULL, false);
		body(0, C_NULL, false);
		body(1, "asd", true);
	}

	public void body(int position, String value, boolean mock) throws IntrospectionException {
		EconomicInformation economic = EconomicInformation.builder().annualSales(new BigDecimal(10)).build();
		PropertyDescriptor[] props = Introspector.getBeanInfo(economic.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("annual").myMethod(props[position]).build();
		DependentField dependentField = DependentField.builder().dependentValue(value).build();
		if (mock){
			doReturn(true).when(dependentFieldUseCase)
					.validateBigDecimal(any(Class.class), any(DependentField.class), any(MyClass.class), anyObject());
		}
		boolean b = dependentFieldUseCase.validateValueInDependent(dependentField, mapped, economic);
		assertTrue(b);
	}

	@Test
	public void validateDependentFieldWhenSameOperation() throws IntrospectionException {
		EconomicInformation economic = EconomicInformation.builder().annualSales(new BigDecimal(10)).build();
		DependentField dependentField = DependentField.builder().fieldDependent("annual").build();
		doReturn(true).when(dependentFieldUseCase)
				.validateValueInDependent(any(DependentField.class), any(MyClass.class), anyObject());
		PropertyDescriptor[] props = Introspector.getBeanInfo(economic.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("annual").myMethod(props[1]).build();
		List<MyClass> list = Collections.singletonList(mapped);
		boolean b = dependentFieldUseCase.validateDependentFieldWhenSameOperation(dependentField, list, economic);
		assertTrue(b);
	}

	@Test
	public void valDepenFieldWhenSameOperDepenInverse() throws IntrospectionException {
		EconomicInformation economic = EconomicInformation.builder().annualSales(new BigDecimal(10)).build();
		DependentField dependentField = DependentField.builder().currentField("annual").build();

		PropertyDescriptor[] props = Introspector.getBeanInfo(economic.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("annual").myMethod(props[1]).build();
		List<MyClass> list = Collections.singletonList(mapped);
		boolean b = dependentFieldUseCase.valDepenFieldWhenSameOperDepenInverse(dependentField, list, economic);
		assertFalse(b);
	}

	@Test
	public void findValueInObjectDepedent(){
		PersonalInformation personalInformation = PersonalInformation.builder().build();
		DependentField dependentField = DependentField.builder().build();
		List<MyClass> mapeado = new ArrayList<>();
		doReturn(mapeado).when(dependentFieldUseCase).getMapField(anyObject());
		doReturn(true).when(dependentFieldUseCase)
				.validateDependentFieldWhenSameOperation(any(DependentField.class), anyList(), anyObject());
		boolean a = dependentFieldUseCase.findValueInObjectDepedent(dependentField, personalInformation);
		assertTrue(a);
	}

	@Test
	public void returnValDepenFieldWhenDiffOper(){
		DependentField dependentField = DependentField.builder().build();
		List<Object> list = Collections.singletonList(dependentField);
		bodyreturnValDepenFieldWhenDiffOper(true, list, dependentField);
		bodyreturnValDepenFieldWhenDiffOper(false, list, dependentField);
		bodyreturnValDepenFieldWhenDiffOper(true, Collections.emptyList(), dependentField);
	}

	public void bodyreturnValDepenFieldWhenDiffOper(
			boolean isDependentInverse, List<Object> list, DependentField dependentField){
		doReturn(true).when(dependentFieldUseCase).fieldIsNull(anyObject(), any(DependentField.class));
		boolean b = dependentFieldUseCase.returnValDepenFieldWhenDiffOper(isDependentInverse, list, dependentField);
		assertTrue(b);
	}

	@Test
	public void filterOnlySameCurrentField(){
		ObjectParamValidateInverse objectParamValidateInverse = ObjectParamValidateInverse.builder()
				.mapeado(new ArrayList<>()).obj(PersonalInformation.builder().build()).build();
		DependentField dependentField = DependentField.builder()
				.currentOperation("asd").dependentOperation("asd").build();
		doReturn(true).when(dependentFieldUseCase)
				.valDepenFieldWhenSameOperDepenInverse(any(DependentField.class), anyList(), anyObject());
		boolean b = dependentFieldUseCase.filterOnlySameCurrentField(objectParamValidateInverse, dependentField);
		assertTrue(b);
	}

	@Test
	public void filterOnlySameCurrentField1(){
		PersonalInformation personalInformation = PersonalInformation.builder().build();
		ObjectParamValidateInverse objectParamValidateInverse = ObjectParamValidateInverse.builder()
				.acq(acquisition).objectDependent(personalInformation).build();
		DependentField dependentField = DependentField.builder()
				.currentOperation("asd").dependentOperation("qwe").build();
		doReturn(true).when(dependentFieldUseCase).validateDependentFieldWhenDifferentOperation(
				any(DependentField.class), any(Acquisition.class), anyObject(), anyBoolean());
		boolean b = dependentFieldUseCase.filterOnlySameCurrentField(objectParamValidateInverse, dependentField);
		assertTrue(b);
	}

	@Test
	public void fieldIsNull() throws IntrospectionException {
		DependentField dependentField = DependentField.builder().fieldDependent("asd").build();
		Object o = dependentField;
		PropertyDescriptor[] props = Introspector.getBeanInfo(dependentField.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("asd").myMethod(props[1]).build();
		List<MyClass> list = Collections.singletonList(mapped);
		doReturn(list).when(dependentFieldUseCase).getMapField(dependentField);
		boolean a = dependentFieldUseCase.fieldIsNull(o, dependentField);
		assertFalse(a);
	}

	@Test
	public void validateFieldDependentInverse() throws IntrospectionException {
		PersonalInformation personalInformation = PersonalInformation.builder().build();
		PropertyDescriptor[] props = Introspector.getBeanInfo(personalInformation.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("asd").myMethod(props[1]).build();
		List<MyClass> list = Collections.singletonList(mapped);
		List<DependentField> dependentFields = new ArrayList<>();
		dependentFields.add(DependentField.builder().currentValue("asd").currentField("asd").build());
		ObjectParamValidateInverse objectParamValidateInverse = ObjectParamValidateInverse.builder()
				.obj(personalInformation)
				.mapeado(list)
				.depFields(dependentFields)
				.build();
		doReturn(true).when(dependentFieldUseCase)
				.filterOnlySameCurrentField(any(ObjectParamValidateInverse.class), any(DependentField.class));
		doReturn(MyClassDependent.builder().build()).when(dependentFieldUseCase)
				.constructMyClassDependent(any(DependentField.class));
		List<MyClassDependent> myClassDependents = dependentFieldUseCase
				.validateFieldDependentInverse(objectParamValidateInverse);
		assertNotNull(myClassDependents);
	}

	@Test
	public void constructMyClassDependent() {
		DependentField dependentField = DependentField.builder().currentField("asd").mandatory(true)
				.currentOperation("asd").fieldDependent("asd").dependentOperation("asd").build();
		MyClassDependent myClassDependent = dependentFieldUseCase.constructMyClassDependent(dependentField);
		assertNotNull(myClassDependent);
	}

	@Test
	public void principal() throws IntrospectionException {
		bodyPrincipal(DependentField.builder().build());
		bodyPrincipal(null);
	}

	public void bodyPrincipal(Object o) throws IntrospectionException {
		PersonalInformation personalInformation = PersonalInformation.builder().build();
		PropertyDescriptor[] props = Introspector.getBeanInfo(personalInformation.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("asd").myMethod(props[1]).build();
		List<MyClass> list = Collections.singletonList(mapped);
		doReturn(list).when(dependentFieldUseCase).getMapField(personalInformation);
		List<DependentField> dependentFieldList = Collections.singletonList(DependentField.builder().build());
		doReturn(dependentFieldList).when(dependentFieldUseCase)
				.getAllDependentFields(anyString(), anyList(), anyString());
		DependentFieldObject dependentFieldObject = DependentFieldObject.builder()
				.listMappeadoOtherOperation(dependentFieldList)
				.listMappeadoSameOperation(dependentFieldList).build();
		doReturn(dependentFieldObject).when(dependentFieldUseCase).filterDependentFieldObject(anyList(), anyList());
		List<MyClassDependent> myClassDependents = new ArrayList<>();
		doReturn(myClassDependents).when(dependentFieldUseCase)
				.dependentFieldListSameOperationMethod(anyList(), anyList(), anyObject());
		doReturn(true).when(dependentFieldUseCase)
				.validateDependentFieldWhenDifferentOperation(any(DependentField.class), any(Acquisition.class),
						anyObject(), anyBoolean());
		doReturn(MyClassDependent.builder().build()).when(dependentFieldUseCase)
				.constructMyClassDependent(any(DependentField.class));
		DependentArr dependentArr = dependentFieldUseCase
				.principal(personalInformation, acquisition, currentOpe, o);
		assertNotNull(dependentArr);
	}

	@Test
	public void filterDependentFieldObject() throws IntrospectionException {
		PersonalInformation personalInformation = PersonalInformation.builder().build();
		PropertyDescriptor[] props = Introspector.getBeanInfo(personalInformation.getClass()).getPropertyDescriptors();
		MyClass mapped = MyClass.builder().myKey("asd").myMethod(props[1]).build();
		List<MyClass> list = Collections.singletonList(mapped);
		List<DependentField> dependentFields = Collections
				.singletonList(DependentField.builder().currentValue("").currentField("asd")
						.currentOperation("asd").dependentOperation("asd").build());
		DependentFieldObject dependentFieldObject = dependentFieldUseCase
				.filterDependentFieldObject(list, dependentFields);
		assertNotNull(dependentFieldObject);
	}

	@Test
	public void dependentFieldListSameOperationMethod(){
		List<MyClass> myClassList = new ArrayList<>();
		List<DependentField> dependentFields = new ArrayList<>();
		doReturn(true).when(dependentFieldUseCase)
				.validateDependentFieldWhenSameOperation(any(DependentField.class), anyList(), anyObject());
		doReturn(MyClassDependent.builder().build()).when(dependentFieldUseCase)
				.constructMyClassDependent(any(DependentField.class));
		List<MyClassDependent> list = dependentFieldUseCase.dependentFieldListSameOperationMethod(
				dependentFields, myClassList, PersonalInformation.builder().build());
		assertNotNull(list);
	}

}