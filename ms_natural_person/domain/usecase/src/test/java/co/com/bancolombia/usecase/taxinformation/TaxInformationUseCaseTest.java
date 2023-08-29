package co.com.bancolombia.usecase.taxinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.taxinformation.TaxInformation;
import co.com.bancolombia.taxinformation.gateways.TaxInformationRepository;
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
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class TaxInformationUseCaseTest {

    @InjectMocks
    @Spy
    TaxInformationUseCaseImpl taxInformationUseCase;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    ValidateCatalogsTaxUseCase validateCatalogsTaxUseCase;

    @Mock
    TaxInformationRepository taxInformationRepository;

    @Mock
    TaxCountryUseCaseImpl taxCountryUseCase;

    @Mock
    MergeUseCase mergeUseCase;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void find(){
        TaxInformation taxInformation = TaxInformation.builder().build();
        Mockito.doReturn(taxInformation).when(taxInformationRepository).findByAcquisition(any(Acquisition.class));
        taxInformation = taxInformationUseCase.findByAcquisition(Acquisition.builder().build());
        assertNotNull(taxInformation);
    }

    @Test
    public void startProcessTaxInformationTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        TaxInformation taxInformation = TaxInformation.builder().acquisition(acquisition).build();
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());

        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        Mockito.doNothing().when(validateCatalogsTaxUseCase).validateTaxInfoCountryCatalogs(any(TaxInformation.class),anyList());
        Mockito.doReturn(taxInformation).when(taxInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doNothing().when(taxInformationUseCase).mergeFieldsTaxInfo(any(TaxInformation.class),any(TaxInformation.class));
        Mockito.doNothing().when(taxInformationUseCase).mandatoryFieldsTaxInfo(any(TaxInformation.class));
        Mockito.doNothing().when(taxInformationUseCase).validateRepeatedCountries(any(), any(List.class));
        Mockito.doReturn(taxCountryList).when(taxCountryUseCase).valMandatoryFieldsAndMerge(any(Acquisition.class),anyList(),any(TaxInformation.class));
        Mockito.doReturn(taxCountryList).when(taxCountryUseCase).validateIdentifier(anyList());
        Mockito.doReturn(taxInformation).when(taxInformationRepository).save(any(TaxInformation.class));
        Mockito.doReturn(taxCountryList).when(taxCountryUseCase).saveAll(anyList());

        taxInformationUseCase.startProcessTaxInformation(taxInformation,taxCountryList);
        Mockito.verify(this.taxInformationUseCase,Mockito.times(1)).startProcessTaxInformation(taxInformation,taxCountryList);
    }

    @Test
    public void startProcessTaxInformationNewRecordTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        TaxInformation taxInformation = TaxInformation.builder().acquisition(acquisition).build();
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());

        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        Mockito.doNothing().when(validateCatalogsTaxUseCase).validateTaxInfoCountryCatalogs(any(TaxInformation.class),anyList());
        Mockito.doReturn(null).when(taxInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doNothing().when(taxInformationUseCase).mergeFieldsTaxInfo(any(TaxInformation.class),any(TaxInformation.class));
        Mockito.doNothing().when(taxInformationUseCase).mandatoryFieldsTaxInfo(any(TaxInformation.class));
        Mockito.doNothing().when(taxInformationUseCase).validateRepeatedCountries(any(), any(List.class));
        Mockito.doReturn(taxCountryList).when(taxCountryUseCase).valMandatoryFieldsAndMerge(any(Acquisition.class),anyList(),any(TaxInformation.class));
        Mockito.doReturn(taxCountryList).when(taxCountryUseCase).validateIdentifier(anyList());
        Mockito.doReturn(taxInformation).when(taxInformationRepository).save(any(TaxInformation.class));
        Mockito.doReturn(taxCountryList).when(taxCountryUseCase).saveAll(anyList());

        taxInformationUseCase.startProcessTaxInformation(taxInformation,taxCountryList);
        Mockito.verify(this.taxInformationUseCase,Mockito.times(1)).startProcessTaxInformation(taxInformation,taxCountryList);
    }

    @Test
    public void startProcessTaxInformationListEmptyTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        TaxInformation taxInformation = TaxInformation.builder().acquisition(acquisition).build();
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().acquisition(acquisition).build());
        List<TaxCountry> taxCountryListEmpty = new ArrayList<>();

        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        Mockito.doNothing().when(validateCatalogsTaxUseCase).validateTaxInfoCountryCatalogs(any(TaxInformation.class),anyList());
        Mockito.doReturn(taxInformation).when(taxInformationRepository).findByAcquisition(any(Acquisition.class));
        Mockito.doNothing().when(taxInformationUseCase).mergeFieldsTaxInfo(any(TaxInformation.class),any(TaxInformation.class));
        Mockito.doNothing().when(taxInformationUseCase).mandatoryFieldsTaxInfo(any(TaxInformation.class));
        Mockito.doNothing().when(taxInformationUseCase).validateRepeatedCountries(any(), any(List.class));
        Mockito.doReturn(taxInformation).when(taxInformationRepository).save(any(TaxInformation.class));
        Mockito.doReturn(taxCountryList).when(taxCountryUseCase).saveAll(anyList());

        taxInformationUseCase.startProcessTaxInformation(taxInformation,taxCountryListEmpty);
        Mockito.verify(this.taxInformationUseCase,Mockito.times(1)).startProcessTaxInformation(taxInformation,taxCountryListEmpty);
    }

    @Test
    public void mandatoryFieldsTaxInfoTest(){
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        TaxInformation taxInformation = TaxInformation.builder().acquisition(acquisition).build();
        ChecklistReply mandatoryExecFList = ChecklistReply.builder().execFieldList(
                Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        Mockito.doReturn(mandatoryExecFList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());
        Mockito.doNothing().when(taxInformationUseCase).validateMandatoryFields(any(TaxInformation.class),anyList(),anyString(),any(DependentFieldParamValidator.class));
        taxInformationUseCase.mandatoryFieldsTaxInfo(taxInformation);
        Mockito.verify(this.taxInformationUseCase,Mockito.times(1)).mandatoryFieldsTaxInfo(taxInformation);
    }

    @Test
    public void mergeFieldsTaxInfoTest(){
        TaxInformation taxInformationInDB = TaxInformation.builder().build();
        TaxInformation taxInformationNew = TaxInformation.builder().build();
        List<ErrorField> errorFields = new ArrayList<>();
        Mockito.doReturn(errorFields).when(mergeUseCase).merge(any(TaxInformation.class),any(TaxInformation.class),any(MergeAttrib.class));
        this.taxInformationUseCase.mergeFieldsTaxInfo(taxInformationInDB,taxInformationNew);
        Mockito.verify(this.taxInformationUseCase,Mockito.times(1)).mergeFieldsTaxInfo(taxInformationInDB,taxInformationNew);
    }

    @Test(expected = ValidationException.class)
    public void mergeFieldsTaxInfoExceptionTest(){
        TaxInformation taxInformationInDB = TaxInformation.builder().build();
        TaxInformation taxInformationNew = TaxInformation.builder().build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        Mockito.doReturn(errorFields).when(mergeUseCase).merge(any(TaxInformation.class),any(TaxInformation.class),any(MergeAttrib.class));
        this.taxInformationUseCase.mergeFieldsTaxInfo(taxInformationInDB,taxInformationNew);
    }

    @Test
    public void validateCountriesNoRepeatedTest() {
        String country = "US";
        List<TaxCountry> taxCountryList = new ArrayList<>();
        taxCountryList.add(TaxCountry.builder().country("CO").build());
        taxInformationUseCase.validateRepeatedCountries(country, taxCountryList);
        Mockito.verify(taxInformationUseCase, Mockito.times(1))
                .validateRepeatedCountries(anyString(), any(List.class));
    }

    @Test
    public void validateCountriesNoRepeatedMultiTest() {
        String country = "US";
        List<TaxCountry> taxCountryList = new ArrayList<>();
        taxCountryList.add(TaxCountry.builder().country("CO").build());
        taxCountryList.add(TaxCountry.builder().country("ES").build());
        taxCountryList.add(TaxCountry.builder().country("UK").build());

        taxInformationUseCase.validateRepeatedCountries(country, taxCountryList);
        Mockito.verify(taxInformationUseCase, Mockito.times(1))
                .validateRepeatedCountries(anyString(), any(List.class));
    }

    @Test(expected = ValidationException.class)
    public void validateCountriesRepeatedTest() {
        String country = "CO";
        List<TaxCountry> taxCountryList = new ArrayList<>();
        taxCountryList.add(TaxCountry.builder().country("CO").build());
        taxInformationUseCase.validateRepeatedCountries(country, taxCountryList);
    }

    @Test(expected = ValidationException.class)
    public void validateCountriesRepeatedMultiTest() {
        String country = "US";
        List<TaxCountry> taxCountryList = new ArrayList<>();
        taxCountryList.add(TaxCountry.builder().country("CO").build());
        taxCountryList.add(TaxCountry.builder().country("ES").build());
        taxCountryList.add(TaxCountry.builder().country("CO").build());
        taxInformationUseCase.validateRepeatedCountries(country, taxCountryList);
    }
}