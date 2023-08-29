package co.com.bancolombia.commonsvnt.model.basicinfo;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class BasicInfoRequestTest {

    @Test
    public void validateBasicInfoRequestTest() {
        MetaRequest meta = MetaRequest.builder().build();
        BasicInfoRequestData data = BasicInfoRequestData.builder().build();
        BasicInfoRequest basicInfoRequest1 = new BasicInfoRequest(MetaRequest.builder().build(),BasicInfoRequestData.builder().build());
        BasicInfoRequest basicInfoRequest2 = new BasicInfoRequest(meta,data);
        BasicInfoRequest basicInfoRequest3 = new BasicInfoRequest();

        basicInfoRequest3.setData(data);
        basicInfoRequest3.setMeta(meta);

        basicInfoRequest1.setData(BasicInfoRequestData.builder().build());
        basicInfoRequest1.setMeta(MetaRequest.builder().build());
        basicInfoRequest1.getData();
        basicInfoRequest1.getMeta();

        assertNotNull(basicInfoRequest1);
        assertNotNull(basicInfoRequest1.toString());
        assertNotNull(basicInfoRequest1.hashCode());
        Assert.assertEquals(basicInfoRequest3,basicInfoRequest2);
    }

}