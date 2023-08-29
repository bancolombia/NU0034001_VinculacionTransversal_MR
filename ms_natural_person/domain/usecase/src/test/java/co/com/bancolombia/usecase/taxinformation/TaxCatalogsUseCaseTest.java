package co.com.bancolombia.usecase.taxinformation;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.taxinformation.TaxInformation;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.util.UtilCatalogs;
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

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class TaxCatalogsUseCaseTest {

    @InjectMocks
    @Spy
    ValidateCatalogsTaxUseCase validateCatalogsTaxUseCase;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    UtilCatalogs utilCatalogs;

    @Mock
    CoreFunctionString coreFunctionString;

    TaxInformation taxInformation;
    @Before
    public void setUp() {
        taxInformation = TaxInformation.builder().declaringIncome("S").withHoldingAgent("01")
                .vatRegime("01").originAssetComeFrom("Prueba Campo").sourceCountryResource("CO1")
                .sourceCityResource("5001000").requiredToTaxUsTax("1").taxId("12345").country("CO2")
                .businessTaxPayment("ZN").socialSecurityPayment("ZS").declareTaxInAnotherCountry("1").build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateTaxInfoCountryCatalogs(){
        List<CatalogQuery> listCatalog = Collections.singletonList(CatalogQuery.builder().build());
        List<GeographicQuery> geographicQueries = Collections.singletonList(GeographicQuery.builder().build());

        doReturn(listCatalog).when(validateCatalogsTaxUseCase).retCatalog1(any(TaxInformation.class));
        doReturn(listCatalog).when(validateCatalogsTaxUseCase).retCatalog2(anyList());
        doReturn(geographicQueries).when(validateCatalogsTaxUseCase).retCatalogGeo(any(TaxInformation.class));
        doReturn(EmptyReply.builder().build()).when(vinculationUpdateUseCase).validateCatalog(anyMap(), anyMap());
        validateCatalogsTaxUseCase.validateTaxInfoCountryCatalogs(
                taxInformation, Collections.singletonList(TaxCountry.builder().build()));
        verify(validateCatalogsTaxUseCase, times(1)).validateTaxInfoCountryCatalogs(
                any(TaxInformation.class), anyList());
    }

   @Test
    public void retCatalog1(){
       doReturn(true).when(utilCatalogs).valid(anyString());
       List<CatalogQuery> catalogQueries = Collections.singletonList(CatalogQuery.builder().build());
       doReturn(catalogQueries).when(validateCatalogsTaxUseCase).retCatalog11(any(TaxInformation.class), anyList());
       catalogQueries = validateCatalogsTaxUseCase.retCatalog1(taxInformation);
       assertNotNull(catalogQueries);
   }

    @Test
    public void retCatalog11(){
        doReturn(true).when(utilCatalogs).valid(anyString());
        List<CatalogQuery> catalogQueries = validateCatalogsTaxUseCase.retCatalog11(taxInformation, new ArrayList<>());
        assertNotNull(catalogQueries);
    }

    @Test
    public void retCatalog2(){
        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder()
                .identifier(132).taxId("123456")
                .country("CO2").build());
        doReturn(true).when(utilCatalogs).valid(anyString());
        doReturn("asd").when(coreFunctionString).integerToString(anyInt());
        List<CatalogQuery> geographicEntries = validateCatalogsTaxUseCase
                .retCatalog2(taxCountryList);
        assertNotNull(geographicEntries);
    }

    @Test
    public void retCatalogGeo(){
        doReturn(true).when(utilCatalogs).valid(anyString());
        List<GeographicQuery> geographicQueries = validateCatalogsTaxUseCase.retCatalogGeo(taxInformation);
        assertNotNull(geographicQueries);
    }
}