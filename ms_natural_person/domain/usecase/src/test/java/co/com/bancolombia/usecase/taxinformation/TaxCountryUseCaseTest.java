package co.com.bancolombia.usecase.taxinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.model.taxcountry.gateways.TaxCountryRepository;
import co.com.bancolombia.taxinformation.TaxInformation;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.taxcountry.TaxCountryUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class TaxCountryUseCaseTest {
    @InjectMocks
    @Spy
    TaxCountryUseCaseImpl taxCountryUseCase;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    TaxCountryRepository taxCountryRepository;

    @Mock
    MergeUseCase mergeUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllByAcquisition(){
        List<TaxCountry> taxCountry = new ArrayList<>();
        Mockito.doReturn(taxCountry).when(taxCountryRepository).findAllByAcquisition(any(Acquisition.class));
        taxCountry = taxCountryUseCase.findAllByAcquisition(Acquisition.builder().build());
        assertNotNull(taxCountry);
    }

    @Test
    public void saveAllTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());
        Mockito.doReturn(taxCountryList).when(taxCountryRepository).saveAll(anyList());
        List<TaxCountry> taxCountryListReturn=this.taxCountryUseCase.saveAll(taxCountryList);
        assertNotNull(taxCountryListReturn);
    }

    @Test
    public void valMandatoryFieldsAndMergeTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());
        TaxInformation dependentObject = TaxInformation.builder().acquisition(acquisition).build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        List<ErrorField> errorFieldsMandatory = new ArrayList<>();
        Optional<TaxCountry> taxCountryOld2 = Optional.empty();
        List<TaxCountry> taxCountryListFound = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());

        ChecklistReply mandatoryExecFList = ChecklistReply.builder().execFieldList(
                Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        Mockito.doReturn(mandatoryExecFList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        Mockito.doReturn(taxCountryListFound).when(taxCountryRepository).findAllByAcquisition(any(Acquisition.class));
        Mockito.doReturn(taxCountryOld2).when(taxCountryUseCase).filterTaxCountry(anyList(),any(TaxCountry.class));
        Mockito.doReturn(errorFields).when(mergeUseCase).merge(any(TaxCountry.class),any(TaxCountry.class),any(MergeAttrib.class));
        Mockito.doReturn(errorFieldsMandatory).when(taxCountryUseCase).validateMandatory(any(TaxCountry.class),anyList(),anyString(),any(DependentFieldParamValidator.class));
        assertNotNull(this.taxCountryUseCase.valMandatoryFieldsAndMerge(acquisition,taxCountryList,dependentObject));
    }

    @Test
    public void valMandatoryFieldsAndMergeTestWithoutMergeError(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());
        TaxInformation dependentObject = TaxInformation.builder().acquisition(acquisition).build();
        List<ErrorField> errorFields = new ArrayList<>();
        Optional<TaxCountry> taxCountryOld2 =  Optional.of(TaxCountry.builder().acquisition(acquisition).build());
        List<TaxCountry> taxCountryListFound = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());

        ChecklistReply mandatoryExecFList = ChecklistReply.builder().execFieldList(
                Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        Mockito.doReturn(mandatoryExecFList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        Mockito.doReturn(taxCountryListFound).when(taxCountryRepository).findAllByAcquisition(any(Acquisition.class));
        Mockito.doReturn(taxCountryOld2).when(taxCountryUseCase).filterTaxCountry(anyList(),any(TaxCountry.class));
        Mockito.doReturn(errorFields).when(mergeUseCase).merge(any(TaxCountry.class),any(TaxCountry.class),any(MergeAttrib.class));

        assertNotNull(this.taxCountryUseCase.valMandatoryFieldsAndMerge(acquisition,taxCountryList,dependentObject));
    }



    @Test(expected = ValidationException.class)
    public void valMandatoryFieldsAndMergeExceptionMandatoryTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());
        TaxInformation dependentObject = TaxInformation.builder().acquisition(acquisition).build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        Optional<TaxCountry> taxCountryOld2 = Optional.of(TaxCountry.builder().acquisition(acquisition).build());
        List<TaxCountry> taxCountryListFound = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());

        ChecklistReply mandatoryExecFList = ChecklistReply.builder().execFieldList(
                Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        Mockito.doReturn(mandatoryExecFList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        Mockito.doReturn(taxCountryListFound).when(taxCountryRepository).findAllByAcquisition(any(Acquisition.class));
        Mockito.doReturn(taxCountryOld2).when(taxCountryUseCase).filterTaxCountry(anyList(),any(TaxCountry.class));
        Mockito.doReturn(errorFields).when(mergeUseCase).merge(any(TaxCountry.class),any(TaxCountry.class),any(MergeAttrib.class));

        assertNotNull(this.taxCountryUseCase.valMandatoryFieldsAndMerge(acquisition,taxCountryList,dependentObject));
    }

    @Test
    public void valMandatoryFieldsAndMergeTestNewRecord(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());
        TaxInformation dependentObject = TaxInformation.builder().acquisition(acquisition).build();
        List<ErrorField> errorFields = new ArrayList<>();
        ChecklistReply mandatoryExecFList = ChecklistReply.builder().execFieldList(
                Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        Optional<TaxCountry> taxCountryOld2 = Optional.empty();
        List<TaxCountry> taxCountryListFound = new ArrayList<>();

        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        Mockito.doReturn(taxCountryListFound).when(taxCountryRepository).findAllByAcquisition(any(Acquisition.class));
        Mockito.doReturn(taxCountryOld2).when(taxCountryUseCase).filterTaxCountry(anyList(),any(TaxCountry.class));
        Mockito.doReturn(mandatoryExecFList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class),any(String.class));
        Mockito.doReturn(errorFields).when(taxCountryUseCase).validateMandatory(any(TaxCountry.class),anyList(),anyString(),any(DependentFieldParamValidator.class));
        Mockito.doNothing().when(taxCountryUseCase).validateIfErrorField(anyList(),anyList());

        assertNotNull(this.taxCountryUseCase.valMandatoryFieldsAndMerge(acquisition,taxCountryList,dependentObject));
    }

    @Test
    public void validateIfErrorFieldTest(){
        List<ErrorField> efMandatory = new ArrayList<>();
        List<ErrorField> efMerge = new ArrayList<>();
        this.taxCountryUseCase.validateIfErrorField(efMandatory,efMerge);
        Mockito.verify(this.taxCountryUseCase,Mockito.times(1)).validateIfErrorField(efMandatory,efMerge);
    }

    @Test(expected = ValidationException.class)
    public void validateIfErrorFieldMandatoryTest(){
        List<ErrorField> efMandatory = Collections.singletonList(ErrorField.builder().build());
        List<ErrorField> efMerge = new ArrayList<>();
        this.taxCountryUseCase.validateIfErrorField(efMandatory,efMerge);
    }


    @Test(expected = ValidationException.class)
    public void validateIfErrorFieldMergeTest(){
        List<ErrorField> efMandatory = new ArrayList<>();
        List<ErrorField> efMerge =   Collections.singletonList(ErrorField.builder().build());
        this.taxCountryUseCase.validateIfErrorField(efMandatory,efMerge);
    }


    @Test
    public void filterTaxCountryTest(){
        List<TaxCountry> taxCountryListSaved = Collections.singletonList(TaxCountry.builder().identifier(1).build());
        TaxCountry taxCountryReceived = TaxCountry.builder().identifier(1).build();
        assertNotNull(this.taxCountryUseCase.filterTaxCountry(taxCountryListSaved,taxCountryReceived));
    }

    @Test
    public void validateIdentifierTest(){
        List<TaxCountry> taxCountryList = new ArrayList<>();
        TaxCountry taxCountry1 = TaxCountry.builder().identifier(1).build();
        TaxCountry taxCountry2 = TaxCountry.builder().identifier(2).build();
        taxCountryList.add(taxCountry1);
        taxCountryList.add(taxCountry2);
        assertNotNull(this.taxCountryUseCase.validateIdentifier(taxCountryList));
    }

    @Test(expected = ValidationException.class)
    public void validateIdentifierErrorTest(){
        List<TaxCountry> taxCountryList = new ArrayList<>();
        TaxCountry taxCountry1 = TaxCountry.builder().identifier(1).build();
        TaxCountry taxCountry2 = TaxCountry.builder().identifier(1).build();
        taxCountryList.add(taxCountry1);
        taxCountryList.add(taxCountry2);
       this.taxCountryUseCase.validateIdentifier(taxCountryList);
    }
}