package co.com.bancolombia.usecase.foreigninformation;


import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FOREIGN_INFORMATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ForeignCatalogsUseCaseTest {

    @InjectMocks
    @Spy
    ValidateCatalogsForeignUseCase validateCatalogsForeignUseCase;

    @Mock
    UtilCatalogs utilCatalogs;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void validateForeignInformation() {
        ForeignInformation foreignInformation = ForeignInformation.builder().foreignCurrencyTransaction("asd").build();
        doReturn(true).when(utilCatalogs).valid(anyString());
        doReturn(EmptyReply.builder().build()).when(utilCatalogs)
                .callValidateCatalog(anyList(), anyList(), anyString(), anyString());
        validateCatalogsForeignUseCase.validateForeignInformation(foreignInformation);
    }

    @Test
    public void validateForeignIInformationCurrency() {
        List<ForeignInformationCurrency> list = Collections.singletonList(
                ForeignInformationCurrency.builder().productType("asd")
                        .foreignCurrencyTransactionType("asd").currency("asd")
                        .country("asd").department("asd").city("asd").build());
        doReturn(true).when(utilCatalogs).valid(anyString());
        doNothing().when(validateCatalogsForeignUseCase).addGeographicCurrency(any(ForeignInformationCurrency.class), anyList());
        doReturn(EmptyReply.builder().build()).when(utilCatalogs)
                .callValidateCatalog(anyList(), anyList(), anyString(), anyString());
        validateCatalogsForeignUseCase.validateForeignIInformationCurrency(list, FOREIGN_INFORMATION);
    }

    @Test
    public void addGeographicCurrency(){
        ForeignInformationCurrency ficData = ForeignInformationCurrency.builder()
                .foreignCurrencyTransactionType("asd").currency("asd")
                .country("asd").department("asd").city("asd").build();
        doReturn(true).when(utilCatalogs).valid(anyString());
        List<GeographicQuery> geographicQueries = new ArrayList<>();
        validateCatalogsForeignUseCase.addGeographicCurrency(ficData, geographicQueries);
        verify(validateCatalogsForeignUseCase, times(1))
                .addGeographicCurrency(ficData, geographicQueries);
    }
}