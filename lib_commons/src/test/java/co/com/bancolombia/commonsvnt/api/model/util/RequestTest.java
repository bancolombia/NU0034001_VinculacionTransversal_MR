package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RequestTest {

    @Test
    public void validateRequestTest() {
        MetaRequest meta = MetaRequest.builder().build();
        Request request1 = new Request(meta);
        Request request2 = new Request(meta);

        assertNotNull(request1);
        assertNotNull(request1.toString());
        assertNotNull(request1.hashCode());
        Assert.assertEquals(request1,request2);
    }

}