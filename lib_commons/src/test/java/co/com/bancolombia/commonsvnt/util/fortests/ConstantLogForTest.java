package co.com.bancolombia.commonsvnt.util.fortests;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ConstantLogForTest {



    @Test
    public void validateConstantLogForTrueTest(){
        String msg = "USER";
        ConstantLogFor constantLogFor = new ConstantLogFor();

        boolean result = constantLogFor.validateConstantLog(msg);

        Assert.assertTrue(result);
    }

    @Test
    public void validateConstantLogForFalseTest(){
        String msg = "";
        ConstantLogFor constantLogFor = new ConstantLogFor();

        boolean result = constantLogFor.validateConstantLog(msg);

        Assert.assertFalse(result);
    }

    @Test
    public void voidValidateConstantLogForTest(){
        ConstantLogFor constantLogFor1 = new ConstantLogFor();
        ConstantLogFor constantLogFor2 = new ConstantLogFor();

        assertNotNull(constantLogFor1);
        assertNotNull(constantLogFor1.toString());
        Assert.assertTrue(constantLogFor1.equals(constantLogFor2));
        Assert.assertEquals(constantLogFor1,constantLogFor2);
    }
}