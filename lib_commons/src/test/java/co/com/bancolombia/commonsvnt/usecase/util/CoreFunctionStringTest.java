package co.com.bancolombia.commonsvnt.usecase.util;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RequiredArgsConstructor
public class CoreFunctionStringTest {

    @InjectMocks
    CoreFunctionString coreFunctionString;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void upperCaseStringTest() {
        String dataResult = this.coreFunctionString.upperCaseString("PRUEBA");
        assertNotNull(dataResult);
    }

    @Test
    public void upperCaseStringIsNullTest() {
        String dataResult = this.coreFunctionString.upperCaseString(null);
        assertNull(dataResult);
    }

    @Test
    public void lowerCaseStringTest() {
        String dataResult = this.coreFunctionString.lowerCaseString("prueba");
        assertNotNull(dataResult);
    }

    @Test
    public void lowerCaseStringIsNullTest() {
        String dataResult = this.coreFunctionString.lowerCaseString(null);
        assertNull(dataResult);
    }

    @Test
    public void integerToStringTest() {
        String dataResult = this.coreFunctionString.integerToString(1234);
        assertNotNull(dataResult);
    }

    @Test
    public void integerToStringIsNullTest() {
        String dataResult = this.coreFunctionString.integerToString(null);
        assertNull(dataResult);
    }

    @Test
    public void stringToDecimalTest() {
        BigDecimal dataResult = this.coreFunctionString.stringToDecimal("1234");
        assertNotNull(dataResult);
    }

    @Test
    public void stringToDecimalIsNullTest() {
        BigDecimal dataResult = this.coreFunctionString.stringToDecimal(null);
        assertNull(dataResult);
    }

    @Test
    public void stringToIntegerTest() {
        Integer dataResult = this.coreFunctionString.stringToInteger("1234");
        assertNotNull(dataResult);
    }

    @Test
    public void stringToIntegerIsNullTest() {
        Integer dataResult = this.coreFunctionString.stringToInteger(null);
        assertNull(dataResult);
    }

    @Test
    public void concatSecondStringOptionalTest() {
        String string = this.coreFunctionString.concatSecondStringOptional("", "");
        assertNotNull(string);
    }

    @Test
    public void concatSecondStringOptionalNullTest() {
        String string = this.coreFunctionString.concatSecondStringOptional("", null);
        assertNotNull(string);
    }

}
