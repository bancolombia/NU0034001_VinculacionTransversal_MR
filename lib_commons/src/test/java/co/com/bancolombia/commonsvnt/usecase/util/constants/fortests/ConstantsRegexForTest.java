package co.com.bancolombia.commonsvnt.usecase.util.constants.fortests;

import org.junit.Assert;
import org.junit.Test;

public class ConstantsRegexForTest {

    @Test
    public void validateConstantsRegexForTrueTest(){
        String msg = "^(001|002)$";
        ConstantsRegexFor constantsRegexFor = new ConstantsRegexFor();

        boolean result = constantsRegexFor.validateConstantsRegex(msg);

        Assert.assertTrue(result);
    }

    @Test
    public void validateConstantsTwoForFalseTest() {
        String msg = "";
        ConstantsRegexFor constantsRegexFor = new ConstantsRegexFor();

        boolean result = constantsRegexFor.validateConstantsRegex(msg);

        Assert.assertFalse(result);
    }

}