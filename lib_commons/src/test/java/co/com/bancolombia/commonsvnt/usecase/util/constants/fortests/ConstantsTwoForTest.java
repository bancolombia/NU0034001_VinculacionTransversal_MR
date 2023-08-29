package co.com.bancolombia.commonsvnt.usecase.util.constants.fortests;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ConstantsTwoForTest {

    @Test
    public void validateConstantsTwoForTrueTest(){
        String msg = "01";
        ConstantsTwoFor constantsTwoFor = new ConstantsTwoFor();

        boolean result = constantsTwoFor.validateConstantsTwo(msg);

        Assert.assertTrue(result);
    }

    @Test
    public void validateConstantsTwoForFalseTest() {
        String msg = "";
        ConstantsTwoFor constantsTwoFor = new ConstantsTwoFor();

        boolean result = constantsTwoFor.validateConstantsTwo(msg);

        Assert.assertFalse(result);
    }

    @Test
    public void validateConstantsTwoForTrueArrayTest(){
        String[] array = {"SP500", "SP502", "SP504"};
        ConstantsTwoFor constantsTwoFor = new ConstantsTwoFor();

        boolean result = constantsTwoFor.validateConstantsTwoArray(array);

        Assert.assertTrue(result);
    }

    @Test
    public void validateConstantsTwoForFalseArrayTest() {
        String[] array = {""};
        ConstantsTwoFor constantsTwoFor = new ConstantsTwoFor();

        boolean result = constantsTwoFor.validateConstantsTwoArray(array);

        Assert.assertFalse(result);
    }



    @Test
    public void voidValidateConstantsTwoForTest(){
        ConstantsTwoFor constantsTwoFor1 = new ConstantsTwoFor();
        ConstantsTwoFor constantsTwoFor2 = new ConstantsTwoFor();

        assertNotNull(constantsTwoFor1);
        assertNotNull(constantsTwoFor1.toString());
        assertNotNull(constantsTwoFor1.hashCode());
        Assert.assertEquals(constantsTwoFor1,constantsTwoFor2);
    }

}