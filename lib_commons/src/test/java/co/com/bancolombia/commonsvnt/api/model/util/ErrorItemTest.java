package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ErrorItemTest {

    @Test
    public void calidateErrorItemTest() {
        ErrorItem errorItem1 = new ErrorItem("","","");
        ErrorItem errorItem2 = new ErrorItem("","","");

        errorItem1.setCode("");
        errorItem1.setDetail("");
        errorItem1.setTitle("");

        errorItem2.getCode();
        errorItem2.getDetail();
        errorItem2.getTitle();

        assertNotNull(errorItem1);
        assertNotNull(errorItem1.toString());
        assertNotNull(errorItem1.hashCode());
        Assert.assertEquals(errorItem2,errorItem1);
    }

}