package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MetaRequestTest {

    @Test
    public void validateMetaRequestTest() {
        MetaRequest metaRequest1 = new MetaRequest("","");
        MetaRequest metaRequest2 = new MetaRequest();
        MetaRequest metaRequest3 = new MetaRequest("","","","","","","","");

        metaRequest2.setIp("");
        metaRequest2.setUsrMod("");

        metaRequest1.getIp();
        metaRequest1.getUsrMod();

        assertNotNull(metaRequest3);
        assertNotNull(metaRequest1);
        assertNotNull(metaRequest1.toString());
        assertNotNull(metaRequest1.hashCode());
        Assert.assertEquals(metaRequest1,metaRequest2);
    }

}