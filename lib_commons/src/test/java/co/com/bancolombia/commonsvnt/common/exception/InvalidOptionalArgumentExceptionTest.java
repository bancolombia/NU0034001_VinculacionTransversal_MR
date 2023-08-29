package co.com.bancolombia.commonsvnt.common.exception;

import org.junit.Assert;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class InvalidOptionalArgumentExceptionTest {

    @Test
    public void validateInvalidOptionalArgumentExceptionTest() {
        List<ConstraintViolation> constraintViolations = new ArrayList<ConstraintViolation>();
        InvalidOptionalArgumentException ioae1 = InvalidOptionalArgumentException.builder().build();

        ioae1.setCode("");
        ioae1.setComplement("");
        ioae1.setNameList("");
        ioae1.setConstraintViolations(constraintViolations);

        InvalidOptionalArgumentException ioae2 = InvalidOptionalArgumentException.builder()
                .code("")
                .complement("")
                .nameList("")
                .constraintViolations(constraintViolations).build();

        assertNotNull(ioae1);
        assertNotNull(ioae1.toString());
        assertNotNull(ioae1.hashCode());
        Assert.assertEquals(ioae1,ioae2);
    }


}