package co.com.bancolombia.usecase.economicinformation;


import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class EconomicCatalogsUseCaseTest {

    @InjectMocks
    @Spy
    ValidateCatalogsEconomicUseCase validateCatalogsEconomicUseCase;

    @Mock
    UtilCatalogs utilCatalogs;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateEconomicInfoCatalogs() {
        EconomicInformation economicInformation = EconomicInformation.builder()
                .profession("asd").positionTrade("asd").occupation("asd").ciiu("asd")
                .currency("asd").rut("asd").build();
        doReturn(true).when(utilCatalogs).valid(anyString());
        doReturn(EmptyReply.builder().build()).when(utilCatalogs)
                .callValidateCatalog(anyList(), anyList(), anyString(), anyString());
        validateCatalogsEconomicUseCase.validateEconomicInfoCatalogs(economicInformation);
        verify(this.validateCatalogsEconomicUseCase, times(1)).validateEconomicInfoCatalogs(economicInformation);
    }

    @Test
    public void validateEconomicInfoValidCatalogs() {
        EconomicInformation economicInformation = EconomicInformation.builder().build();
        doReturn(true).when(utilCatalogs).valid(anyString());
        doReturn(EmptyReply.builder().build()).when(utilCatalogs)
                .callValidateCatalog(anyList(), anyList(), anyString(), anyString());
        validateCatalogsEconomicUseCase.validateEconomicInfoCatalogs(economicInformation);
        verify(this.validateCatalogsEconomicUseCase, times(1)).validateEconomicInfoCatalogs(economicInformation);
    }

    @Test
    public void validateEconomicInfoCatalogsTwo() {
        EconomicInformation economicInformation = EconomicInformation.builder()
                .profession("asd").positionTrade("asd").occupation("asd").ciiu("asd")
                .currency("asd").rut("asd").build();
        doReturn(true).when(utilCatalogs).valid(anyString());
        doReturn(EmptyReply.builder().build()).when(utilCatalogs)
                .callValidateCatalog(anyList(), anyList(), anyString(), anyString());
        List<CatalogQuery> listCatalog = new ArrayList<>();
        validateCatalogsEconomicUseCase.validateEconomicInfoCatalogsTwo(economicInformation, listCatalog);
        verify(this.validateCatalogsEconomicUseCase, times(1))
                .validateEconomicInfoCatalogsTwo(economicInformation, listCatalog);
    }
}