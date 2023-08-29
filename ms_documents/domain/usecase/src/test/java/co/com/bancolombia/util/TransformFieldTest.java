package co.com.bancolombia.util;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.LAST_TWO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MARK_CORR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NUMBER_SN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ZETA_SN;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class TransformFieldTest {

    @InjectMocks
    private TransformFields transformFields;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void transformNullTest() {
        String asd = transformFields.transform(null, null);
        assertNull(asd);
    }

    @Test
    public void transformEmptyTest() {
        String asd = transformFields.transform(EMPTY, null);
        assertNull(asd);
    }

    @Test
    public void transformNumberSTest() {
        String asd = transformFields.transform("asd_S", NUMBER_SN);
        assertNotNull(asd);
    }

    @Test
    public void transformNumberNTest() {
        String asd = transformFields.transform("asd_N", NUMBER_SN);
        assertNotNull(asd);
    }

    @Test
    public void transformZetaTest() {
        String asd = transformFields.transform("asd", ZETA_SN);
        assertNotNull(asd);
    }

    @Test
    public void transformMarkCorrEmptyTest() {
        String asd = transformFields.transform("asd_Vacio", MARK_CORR);
        assertNotNull(asd);
    }

    @Test
    public void transformMarkCorrValueTest() {
        String asd = transformFields.transform("asd", MARK_CORR);
        assertNotNull(asd);
    }

    @Test
    public void transformLastTwoTest() {
        String asd = transformFields.transform("asds", LAST_TWO);
        assertNotNull(asd);
    }

    @Test
    public void transformDefaultTest() {
        String asd = transformFields.transform("asds", "OTHER");
        assertNotNull(asd);
    }

    @Test
    public void transformNullFormatTest() {
        String asd = transformFields.transform("asds", null);
        assertNotNull(asd);
    }

    @Test
    public void modDNNTest() {
        doReturn("asd").when(coreFunctionDate).toFormatDate(any(Date.class));
        String asd = transformFields.modDNN(new Date());
        assertNotNull(asd);
    }

    @Test
    public void modDNNNullTest() {
        String asd = transformFields.modDNN(null);
        assertNull(asd);
    }
}