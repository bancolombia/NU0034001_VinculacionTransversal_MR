package co.com.bancolombia.usecase.merge;

import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class MergeUseCaseTest {

    protected static final String CODE_BASIC_INFO = "BASCINFO";

    @InjectMocks
    @Spy
    MergeUseCaseImpl mergeUseCase;

    @Mock
    VinculationUpdateUseCase vinUpdate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mergeTest() throws IntrospectionException {
        Acquisition acq = Acquisition.builder().id(UUID.randomUUID()).build();
        Mergeable oldData = BasicInformation.builder().acquisition(acq).gender("M").build();
        Mergeable newData = BasicInformation.builder().acquisition(acq).gender("F").build();

        List<ExecFieldReply> noUpFields = Collections
                .singletonList(ExecFieldReply.builder().name("gender").build());
        ChecklistReply reply = ChecklistReply.builder().execFieldList(noUpFields).build();

        MergeAttrib attrib = MergeAttrib.builder().stepCode(CODE_BASIC_INFO).nameList(null).isRecordUpgradeable(false)
                                        .build();

        PropertyDescriptor[] props = Introspector.getBeanInfo(oldData.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props).filter(f -> f.getName().equals("gender"))
                                                       .collect(Collectors.toList());


        Mockito.doReturn(reply).when(vinUpdate).checkListStatus(any(UUID.class), any(String.class));
        Mockito.doReturn(props1).when(mergeUseCase).getAnnotatedFieldsProperties(any(Mergeable.class));

        List<ErrorField> errorFields = mergeUseCase.merge(oldData, newData, attrib);
        assertNotNull(errorFields);
    }

    @Test
    public void mergeUpdateNullTest() throws IntrospectionException {

        Acquisition acq = Acquisition.builder().id(UUID.randomUUID()).build();
        Mergeable oldData = BasicInformation.builder().acquisition(acq).gender(null).build();
        Mergeable newData = BasicInformation.builder().acquisition(acq).gender("F").build();

        List<ExecFieldReply> noUpFields = new ArrayList<>();
        ChecklistReply reply = ChecklistReply.builder().execFieldList(noUpFields).build();

        MergeAttrib attrib = MergeAttrib.builder().stepCode(CODE_BASIC_INFO).nameList(null).isRecordUpgradeable(false)
                                        .build();

        PropertyDescriptor[] props = Introspector.getBeanInfo(oldData.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props).filter(f -> f.getName().equals("gender"))
                                                       .collect(Collectors.toList());


        Mockito.doReturn(reply).when(vinUpdate).checkListStatus(any(UUID.class), any(String.class));
        Mockito.doReturn(props1).when(mergeUseCase).getAnnotatedFieldsProperties(any(Mergeable.class));

        List<ErrorField> errorFields = mergeUseCase.merge(oldData, newData,attrib);
        assertNotNull(errorFields);
    }

    @Test
    public void mergeIlegalTest() throws IntrospectionException {

        Acquisition acq = Acquisition.builder().id(UUID.randomUUID()).build();
        Mergeable oldData = BasicInformation.builder().acquisition(acq).gender("M").build();
        Mergeable newData = BasicInformation.builder().acquisition(acq).build();

        List<ExecFieldReply> noUpFields = asList(ExecFieldReply.builder().name("gender").build());
        ChecklistReply reply = ChecklistReply.builder().execFieldList(noUpFields).build();

        MergeAttrib attrib = MergeAttrib.builder().stepCode(CODE_BASIC_INFO).nameList(null).isRecordUpgradeable(false)
                                        .build();

        PropertyDescriptor[] props = Introspector.getBeanInfo(oldData.getClass()).getPropertyDescriptors();
        List<PropertyDescriptor> props1 = stream(props).filter(f -> f.getName().equals("Apellido"))
                                                       .collect(Collectors.toList());

        Mockito.doReturn(reply).when(vinUpdate).checkListStatus(any(UUID.class), any(String.class));
        Mockito.doReturn(props1).when(mergeUseCase).getAnnotatedFieldsProperties(any(Mergeable.class));

        List<ErrorField> errorFields = mergeUseCase.merge(oldData, newData, attrib);
        assertNotNull(errorFields);
    }


    @Test
    public void getAnnotatedFieldsPropertiesTest() throws IntrospectionException {
        Acquisition acq = Acquisition.builder().id(UUID.randomUUID()).build();
        Mergeable oldData = BasicInformation.builder().acquisition(acq).gender("M").build();
        Mergeable newData = BasicInformation.builder().acquisition(acq).gender("F").build();

        List<ExecFieldReply> noUpFields = asList(ExecFieldReply.builder().name("gender").build());
        List<ErrorField> errorFields = new ArrayList<>();
        List<PropertyDescriptor> propertyDescriptors = mergeUseCase.getAnnotatedFieldsProperties(oldData);
        assertNotNull(propertyDescriptors);
    }
}
