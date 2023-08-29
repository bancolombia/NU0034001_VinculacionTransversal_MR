package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MetaTest {

    @Test
    public void validateMetaTest() {
        Meta meta1 = new Meta();

        meta1.setSystemId("");
        meta1.setMessageId("");
        meta1.setVersion("");
        meta1.setRequestDate("");
        meta1.setService("");
        meta1.setOperation("");

        meta1.getSystemId();
        meta1.getMessageId();
        meta1.getVersion();
        meta1.getRequestDate();
        meta1.getService();
        meta1.getOperation();

        Meta meta2 = new Meta("","","","","","");

        assertNotNull(meta1);
        assertNotNull(meta1.toString());
        assertNotNull(meta1.hashCode());
        Assert.assertEquals(meta1,meta2);
    }

}