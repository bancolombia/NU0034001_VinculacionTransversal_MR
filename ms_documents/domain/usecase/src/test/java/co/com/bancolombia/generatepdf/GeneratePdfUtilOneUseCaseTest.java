package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.GeographicReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GeneratePdfUtilOneUseCaseTest {

    @InjectMocks
    @Spy
    private GeneratePdfUtilOneUseCaseImpl generatePdfUtilOneUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCatalogNameValidTest() {
        CatalogReply r = CatalogReply.builder().valid(true).name("NAME").build();
        doReturn(r).when(vinculationUpdateUseCase).findCatalog(anyString(), anyString());

        String name = generatePdfUtilOneUseCase.getCatalogName("PARENT", "CODE");
        assertEquals("NAME", name);
    }

    @Test
    public void getCatalogNameNotValidTest() {
        CatalogReply r = CatalogReply.builder().valid(false).build();
        doReturn(r).when(vinculationUpdateUseCase).findCatalog(anyString(), anyString());

        String name = generatePdfUtilOneUseCase.getCatalogName("PARENT", "CODE");
        assertEquals(EMPTY, name);
    }

    @Test
    public void getCatalogCodeValidTest() {
        CatalogReply r = CatalogReply.builder().valid(true).code("CODE").build();
        doReturn(r).when(vinculationUpdateUseCase).findCatalog(anyString(), anyString());

        String code = generatePdfUtilOneUseCase.getCatalogCode("PARENT", "CODE");
        assertEquals("CODE", code);
    }

    @Test
    public void getCatalogCodeNotValidTest() {
        CatalogReply r = CatalogReply.builder().valid(false).build();
        doReturn(r).when(vinculationUpdateUseCase).findCatalog(anyString(), anyString());

        String code = generatePdfUtilOneUseCase.getCatalogCode("PARENT", "CODE");
        assertEquals(EMPTY, code);
    }

    @Test
    public void getGeographicNameOneTest() {
        GeographicReply r = GeographicReply.builder().nameCity("NAME CITY").build();
        doReturn(r).when(vinculationUpdateUseCase).findGeographic(any(), any(), any());

        String name = generatePdfUtilOneUseCase.getGeographicName("CODE", "DIVISION", Numbers.ONE.getIntNumber());
        assertEquals("NAME CITY", name);
    }

    @Test
    public void getGeographicNameTwoTest() {
        GeographicReply r = GeographicReply.builder().nameDepartment("NAME DEPARTMENT").build();
        doReturn(r).when(vinculationUpdateUseCase).findGeographic(any(), any(), any());

        String name = generatePdfUtilOneUseCase.getGeographicName("CODE", null, Numbers.TWO.getIntNumber());
        assertEquals("NAME DEPARTMENT", name);
    }

    @Test
    public void getGeographicNameThreeTest() {
        GeographicReply r = GeographicReply.builder().nameCountry("NAME COUNTRY").build();
        doReturn(r).when(vinculationUpdateUseCase).findGeographic(any(), any(), any());

        String name = generatePdfUtilOneUseCase.getGeographicName("CODE", null, Numbers.THREE.getIntNumber());
        assertEquals("NAME COUNTRY", name);
    }

    @Test
    public void getGeographicNameFourTest() {
        GeographicReply r = GeographicReply.builder().nameCity("NAME CITY").build();
        doReturn(r).when(vinculationUpdateUseCase).findGeographic(any(), any(), any());

        String name = generatePdfUtilOneUseCase.getGeographicName("CODE", "DIVISION", Numbers.FOUR.getIntNumber());
        assertEquals("NAME CITY", name);
    }

    @Test
    public void getGeographicNameDefaultTest() {
        String name = generatePdfUtilOneUseCase.getGeographicName("CODE", "DIVISION", Numbers.FIVE.getIntNumber());
        assertEquals("", name);
    }

    @Test
    public void getGeographicNameNullCodeTest() {
        String name = generatePdfUtilOneUseCase.getGeographicName(null, null, Numbers.FIVE.getIntNumber());
        assertEquals("", name);
    }

    @Test
    public void getGeographicNameEmptyCodeTest() {
        String name = generatePdfUtilOneUseCase.getGeographicName("", null, Numbers.FIVE.getIntNumber());
        assertEquals("", name);
    }

    @Test
    public void getGeographicNameNullTest() {
        GeographicReply r = GeographicReply.builder().build();
        doReturn(r).when(vinculationUpdateUseCase).findGeographic(any(), any(), any());

        String name = generatePdfUtilOneUseCase.getGeographicName("CODE", "DIVISION", Numbers.ONE.getIntNumber());
        assertEquals("", name);
    }

    @Test
    public void checkListFindByOperationTest() {
        ChecklistReply r = ChecklistReply.builder().stateOperation("1").build();
        doReturn(r).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        CheckList checkList = generatePdfUtilOneUseCase.checkListFindByOperation(acquisition, CODE_PROCESS_DOCUMENTS);
        assertNotNull(checkList);
    }
}
