package co.com.bancolombia.commonsvnt.common.exception;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ErrorFieldTest {

    @Test
    public void validateErrorFieldTest() {
        ErrorField errorField1 = ErrorField.builder().build();

        errorField1.setComplement("");
        errorField1.setMessage("");
        errorField1.setName("");
        errorField1.setTitle("");
        errorField1.setNameList("");

        ErrorField errorField2 = ErrorField.builder().build();

        errorField2.setComplement("");
        errorField2.setMessage("");
        errorField2.setName("");
        errorField2.setTitle("");
        errorField2.setNameList("");

        assertNotNull(errorField1);
        assertNotNull(errorField1.toString());
        assertNotNull(errorField1.hashCode());
        Assert.assertEquals(errorField1,errorField2);
    }
}