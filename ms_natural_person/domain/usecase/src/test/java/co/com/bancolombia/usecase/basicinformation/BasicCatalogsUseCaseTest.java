package co.com.bancolombia.usecase.basicinformation;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.model.basicinformation.BasicInformation;
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

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class BasicCatalogsUseCaseTest {

    @InjectMocks
    @Spy
    ValidateCatalogsBasicUseCase validateCatalogsBasicUseCase;

    @Mock
    UtilCatalogs utilCatalogs;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateBasicInfoCatalogs() {
        BasicInformation basicInformation = BasicInformation.builder()
                .gender("asd").civilStatus("asd").educationLevel("asd").socialStratum("asd")
                .housingType("asd").contractType("asd").pep("asd").nationality("asd")
                .country("asd").birthDepartment("asd").birthCity("asd").build();
        doReturn(true).when(utilCatalogs).valid(anyString());
        doReturn(new ArrayList<>()).when(validateCatalogsBasicUseCase)
                .validateBasicInfoCatalogs2(anyList(), any(BasicInformation.class));
        doReturn(EmptyReply.builder().build()).when(utilCatalogs)
                .callValidateCatalog(anyList(), anyList(), anyString(), anyString());
        EmptyReply reply = validateCatalogsBasicUseCase.validateBasicInfoCatalogs(basicInformation);
        assertNotNull(reply);
    }

    @Test
    public void validateBasicInfoCatalogs2() {
        BasicInformation basicInformation = BasicInformation.builder()
                .gender("asd").civilStatus("asd").educationLevel("asd").socialStratum("asd")
                .housingType("asd").contractType("asd").pep("asd").nationality("asd")
                .country("asd").birthDepartment("asd").birthCity("asd").build();
        doReturn(true).when(utilCatalogs).valid(anyString());
        doReturn(EmptyReply.builder().build()).when(utilCatalogs)
                .callValidateCatalog(anyList(), anyList(), anyString(), anyString());
        List<CatalogQuery> catalogEntries = new ArrayList<>();
        List<GeographicQuery> reply = validateCatalogsBasicUseCase
                .validateBasicInfoCatalogs2(catalogEntries, basicInformation);
        assertNotNull(reply);
    }
}