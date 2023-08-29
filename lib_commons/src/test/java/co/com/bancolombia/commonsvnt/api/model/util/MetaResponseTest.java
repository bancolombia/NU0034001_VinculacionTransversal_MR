package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

public class MetaResponseTest {

    @Test
    public void fromMetaTest() {
        MetaRequest metaRequest = MetaRequest.builder().usrMod("").ip("").build();
        MetaResponse metaResponse = new MetaResponse();
        MetaResponse metaResponse1 = new MetaResponse("","","","","","");

        Assert.assertNotNull(metaResponse1);
        Assert.assertNotNull(MetaResponse.fromMeta(metaRequest));
    }
}