package co.com.bancolombia.usecase.foreigninformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformation.ForeignInformationOperation;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import co.com.bancolombia.model.foreigninformationcurrency.gateways.ForeignInformationCurrencyRepository;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.foreigninformationcurrency.ForeignInformationCurrencyUseCaseImpl;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOTHING_PRODUCT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.N_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers.EIGHT;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class ForeignInformationCurrencyUseTest {

    @InjectMocks
    @Spy
    ForeignInformationCurrencyUseCaseImpl useCase;
    @Mock
    ValidateCatalogsForeignUseCase validateCatalogsForeignUseCase;

    @Mock
    MergeUseCase mergeUseCase;

    @Mock
    ForeignInformationCurrencyRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findTest(){
        List<ForeignInformationCurrency> foreignInformationCurrencies = new ArrayList<>();
        Mockito.doReturn(foreignInformationCurrencies).when(repository)
                .findByForeignInformation(any(ForeignInformation.class));
        List<ForeignInformationCurrency> list = useCase.
                findByForeignInformation(ForeignInformation.builder().build());
        assertNotNull(list);
    }

    @Test
    public void mandatoryMergeForeignInfoTest() {
        List<ForeignInformationCurrency> list = new ArrayList<>();
        List<ExecFieldReply> execFields = new ArrayList<>();
        ForeignInformation foreignInformation = ForeignInformation.builder().build();
        Mockito.doReturn(list).when(repository).findByForeignInformation(any(ForeignInformation.class));
        Mockito.doNothing().when(useCase).mandatoryForeignInformationCurrency(anyList(), anyList(),
                any(ForeignInformation.class));
        List<ForeignInformationCurrency> response = useCase.mandatoryMergeForeignInfo(foreignInformation, list,
                execFields, true);
        assertNotNull(response);
    }

    @Test
    public void mandatoryMergeForeignInfoNotEmptyTest() {
        List<ForeignInformationCurrency> list = Collections.singletonList(ForeignInformationCurrency.builder().build());
        List<ExecFieldReply> execFields = new ArrayList<>();
        ForeignInformation foreignInformation = ForeignInformation.builder().build();
        Mockito.doReturn(list).when(repository).findByForeignInformation(any(ForeignInformation.class));
        Mockito.doNothing().when(useCase).mandatoryForeignInformationCurrency(anyList(), anyList(),
                any(ForeignInformation.class));
        Mockito.doReturn(list).when(useCase).mergeForeignInformationCurrency(anyList(), anyList());
        List<ForeignInformationCurrency> response = useCase.mandatoryMergeForeignInfo(foreignInformation, list,
                execFields, true);
        assertNotNull(response);
    }

    @Test
    public void mandatoryMergeForeignInfoEmptyTest() {
        List<ForeignInformationCurrency> list = Collections.singletonList(ForeignInformationCurrency.builder().build());
        List<ExecFieldReply> execFields = new ArrayList<>();
        ForeignInformation foreignInformation = ForeignInformation.builder().build();
        Mockito.doReturn(new ArrayList<>()).when(repository).findByForeignInformation(any(ForeignInformation.class));
        Mockito.doNothing().when(useCase).mandatoryForeignInformationCurrency(anyList(), anyList(),
                any(ForeignInformation.class));
        Mockito.doReturn(list).when(useCase).mergeForeignInformationCurrency(anyList(), anyList());
        List<ForeignInformationCurrency> response = useCase.mandatoryMergeForeignInfo(foreignInformation, list,
                execFields, false);
        assertNotNull(response);
    }

    @Test
    public void mergeForeignInformationCurrencyTest() {
        ForeignInformationCurrency oldD = ForeignInformationCurrency.builder().foreignCurrencyTransactionType("N")
                .build();
        ForeignInformationCurrency newD = ForeignInformationCurrency.builder().build();
        List<ForeignInformationCurrency> foreignOld = Collections.singletonList(oldD);
        List<ForeignInformationCurrency> foreignNew = Collections.singletonList(newD);

        List<ForeignInformationCurrency> response = this.useCase.mergeForeignInformationCurrency(foreignNew,
                foreignOld);
        assertNotNull(response);
    }

    @Test
    public void mergeForeignInformationCurrencyMergeTest() {
        List<ErrorField> errorFields = new ArrayList<>();
        ForeignInformationCurrency oldD = ForeignInformationCurrency.builder().foreignCurrencyTransactionType("N")
                .build();
        ForeignInformationCurrency newD = ForeignInformationCurrency.builder().foreignCurrencyTransactionType("N")
                .build();
        List<ForeignInformationCurrency> foreignOld = Collections.singletonList(oldD);
        List<ForeignInformationCurrency> foreignNew = Collections.singletonList(newD);

        Mockito.doReturn(errorFields).when(mergeUseCase).merge(any(ForeignInformationCurrency.class),
                any(ForeignInformationCurrency.class), any(MergeAttrib.class));

        List<ForeignInformationCurrency> response = this.useCase.mergeForeignInformationCurrency(foreignNew,
                foreignOld);
        assertNotNull(response);
    }

    @Test(expected = ValidationException.class)
    public void mergeForeignInformationCurrencyMergeExceptionTest() {
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        ForeignInformationCurrency oldD = ForeignInformationCurrency.builder().foreignCurrencyTransactionType("N")
                .build();
        ForeignInformationCurrency newD = ForeignInformationCurrency.builder().foreignCurrencyTransactionType("N")
                .build();
        List<ForeignInformationCurrency> foreignOld = Collections.singletonList(oldD);
        List<ForeignInformationCurrency> foreignNew = Collections.singletonList(newD);

        Mockito.doReturn(errorFields).when(mergeUseCase).merge(any(ForeignInformationCurrency.class),
                any(ForeignInformationCurrency.class), any(MergeAttrib.class));

        this.useCase.mergeForeignInformationCurrency(foreignNew, foreignOld);
    }

    @Test
    public void mandatoryForeignInformationCurrencyTest() {
        Acquisition acquisition = Acquisition.builder().build();
        List<ForeignInformationCurrency> list = Collections.singletonList(ForeignInformationCurrency.builder()
                .acquisition(acquisition).foreignCurrencyTransactionType("").build());
        List<ErrorField> errorFields = new ArrayList<>();
        List<ExecFieldReply> execFields = new ArrayList<>();

        Mockito.doReturn(errorFields).when(useCase).validateMandatory(any(ForeignInformationCurrency.class), anyList(),
                anyString(), any(DependentFieldParamValidator.class));

        this.useCase.mandatoryForeignInformationCurrency(list, execFields, ForeignInformation.builder().build());
        Mockito.verify(this.useCase, Mockito.times(1)).mandatoryForeignInformationCurrency(list, execFields,
                ForeignInformation.builder().build());
    }

    @Test(expected = ValidationException.class)
    public void mandatoryForeignInformationCurrencyExceptionTest() {
        Acquisition acquisition = Acquisition.builder().build();
        List<ForeignInformationCurrency> list = Collections.singletonList(ForeignInformationCurrency.builder()
                .acquisition(acquisition).foreignCurrencyTransactionType("").build());
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        List<ExecFieldReply> execFields = new ArrayList<>();

        Mockito.doReturn(errorFields).when(useCase).validateMandatory(any(ForeignInformationCurrency.class), anyList(),
                anyString(), any(DependentFieldParamValidator.class));

        this.useCase.mandatoryForeignInformationCurrency(list, execFields, ForeignInformation.builder().build());
        Mockito.verify(this.useCase, Mockito.times(1)).mandatoryForeignInformationCurrency(list, execFields,
                ForeignInformation.builder().build());
    }

    @Test
    public void save() {
        ForeignInformation foreignInformation = ForeignInformation.builder().build();
        List<ForeignInformationCurrency> request = Collections
                .singletonList(ForeignInformationCurrency.builder().build());
        Mockito.doReturn(new ArrayList<>()).when(repository).saveAll(anyList());
        List<ForeignInformationCurrency> list = this.useCase.save(foreignInformation, request);
        assertNotNull(list);
    }

    @Test
    public void validateListNullEmptyTest() {

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<ForeignInformationCurrency> list = new ArrayList<>();
        ForeignInformation fI = ForeignInformation.builder().foreignCurrencyTransaction("N").acquisition(acquisition)
                .createdBy("user").createdDate(new CoreFunctionDate().getDatetime()).build();
        ForeignInformationOperation oper = ForeignInformationOperation.builder().foreignInformation(fI).list(list)
                .build();
        List<ForeignInformationCurrency> listCurr = this.useCase.validateListNull(oper, fI);
        assertNotNull(listCurr);

    }

    @Test
    public void validateListNullTest() {

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<ForeignInformationCurrency> list = null;
        ForeignInformation fI = ForeignInformation.builder().foreignCurrencyTransaction(N_FOREIGN_INFORMATION).acquisition(acquisition)
                .createdBy("user").createdDate(new CoreFunctionDate().getDatetime()).build();
        ForeignInformationOperation oper = ForeignInformationOperation.builder().foreignInformation(fI).list(list)
                .build();
        List<ForeignInformationCurrency> listCurr = this.useCase.validateListNull(oper, fI);
        assertEquals(1, listCurr.size());

    }

    @Test
    public void validateListNullNoEmrpyTest() {

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType("8").build());
        ForeignInformation fI = ForeignInformation.builder().foreignCurrencyTransaction("N").acquisition(acquisition)
                .createdBy("user").createdDate(new CoreFunctionDate().getDatetime()).build();
        ForeignInformationOperation oper = ForeignInformationOperation.builder().foreignInformation(fI).list(list)
                .build();
        List<ForeignInformationCurrency> res = Collections.singletonList(
                ForeignInformationCurrency.builder().foreignCurrencyTransactionType(EIGHT.getNumber())
                        .createdDate(fI.getCreatedDate()).createdBy(fI.getCreatedBy()).build());
        List<ForeignInformationCurrency> listCurr = this.useCase.validateListNull(oper, fI);
        assertEquals(res, listCurr);
    }

    @Test
    public void validateListOk() throws IntrospectionException {
        String transaction = "S";
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props).filter(f -> f.getName().equals("city"))
                .collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections.singletonList(
                ForeignInformationCurrency.builder().foreignCurrencyTransactionType("3").city("050001").build());
        this.useCase.validateList(transaction, list.get(0), props1);
    }

    @Test(expected = ValidationException.class)
    public void validateListNotOk() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props).filter(f -> f.getName().equals("city"))
                .collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections.singletonList(
                ForeignInformationCurrency.builder().foreignCurrencyTransactionType("3").city("050001").build());
        this.useCase.validateList(N_FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test(expected = ValidationException.class)
    public void validateListNotOk1() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props).filter(f -> f.getName().equals("city"))
                .collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections.singletonList(
                ForeignInformationCurrency.builder().foreignCurrencyTransactionType(NOTHING_PRODUCT).city("050001").build());
        this.useCase.validateList(FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test(expected = ValidationException.class)
    public void validateListNotOk2() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props).filter(f -> f.getName().equals("city"))
                .collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections.singletonList(
                ForeignInformationCurrency.builder().foreignCurrencyTransactionType(NOTHING_PRODUCT).city("050001").build());
        this.useCase.validateList(N_FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test
    public void validateListNotOk3() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props).filter(f -> f.getName().equals("city"))
                .collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType("3").build());
        this.useCase.validateList(FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test
    public void validateListNotOk4() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType("3").build());
        this.useCase.validateList(FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test(expected = ValidationException.class)
    public void validateListNotOk5() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType(NOTHING_PRODUCT).build());
        this.useCase.validateList(FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test
    public void validateListNotOk6() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType(NOTHING_PRODUCT).build());
        this.useCase.validateList(N_FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test(expected = ValidationException.class)
    public void validateListNotOk7() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType("3").build());
        this.useCase.validateList(N_FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test(expected = ValidationException.class)
    public void validateListNotOk8() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType(NOTHING_PRODUCT).build());
        this.useCase.validateList(FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test
    public void validateListNotOk9() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType(NOTHING_PRODUCT).build());
        this.useCase.validateList(N_FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test(expected = ValidationException.class)
    public void validateListNotOk10() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections.singletonList(
                ForeignInformationCurrency.builder().foreignCurrencyTransactionType(NOTHING_PRODUCT).city("city").build());
        this.useCase.validateList(FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test
    public void validateListNotOk11() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections.singletonList(
                ForeignInformationCurrency.builder().foreignCurrencyTransactionType("3").city("city").build());
        this.useCase.validateList(FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test(expected = ValidationException.class)
    public void validateListNotOk12() throws IntrospectionException {
        ForeignInformationCurrency fIC = ForeignInformationCurrency.builder().build();
        PropertyDescriptor[] props = Introspector.getBeanInfo(fIC.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props)
                .filter(f -> f.getName().equals("foreignCurrencyTransactionType")).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = Collections
                .singletonList(ForeignInformationCurrency.builder().build());
        this.useCase.validateList(N_FOREIGN_INFORMATION, list.get(0), props1);
    }

    @Test
    public void mergeFieldsForeingInfoTest(){
        Mockito.doReturn(new ArrayList<>()).when(mergeUseCase).merge(any(ForeignInformationCurrency.class),
                any(ForeignInformationCurrency.class),any(MergeAttrib.class));
        List<ErrorField> error = this.useCase.mergeFieldsForeingInfo(ForeignInformationCurrency.builder().foreignCurrencyTransactionType("").build(),
                ForeignInformationCurrency.builder().foreignCurrencyTransactionType("").build());
        assertNotNull(error);
    }

    @Test(expected = ValidationException.class)
    public void updateInfoObjectNewTest(){
        List<ForeignInformationCurrency> list = Collections.singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType("").build());
        Mockito.doReturn(Optional.of(ForeignInformationCurrency.builder().build())).when(repository)
                .findByForeignInformationAndForeignCurrencyTransactionType(any(ForeignInformation.class),anyString());
        Mockito.doReturn(Collections.singletonList(ErrorField.builder().build())).when(useCase)
                .mergeFieldsForeingInfo(any(ForeignInformationCurrency.class),any(ForeignInformationCurrency.class));
        this.useCase.updateInfoObjectNew(list,ForeignInformation.builder().build());
    }

    @Test
    public void updateInfoObjectNewNullTest(){
        List<ForeignInformationCurrency> list = Collections.singletonList(ForeignInformationCurrency.builder().foreignCurrencyTransactionType("").build());
        Mockito.doReturn(Optional.empty()).when(repository)
                .findByForeignInformationAndForeignCurrencyTransactionType(any(ForeignInformation.class),anyString());
        Mockito.doReturn(new ArrayList<>()).when(useCase)
                .mergeFieldsForeingInfo(any(ForeignInformationCurrency.class),any(ForeignInformationCurrency.class));
        List<ForeignInformationCurrency> error = this.useCase.updateInfoObjectNew(list,null);
        assertNotNull(error);
    }
}
