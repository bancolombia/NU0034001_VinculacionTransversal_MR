package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CodeNameResponseTest {

    @Test
    public void validateCodeNameResponseTest() {
        CodeNameResponse codeNameResponse1 = CodeNameResponse.builder().build();
        CodeNameResponseData codeNameResponseData = CodeNameResponseData.builder().build();
        MetaResponse metaResponse = new MetaResponse();

        codeNameResponse1.setData(codeNameResponseData);
        codeNameResponse1.setMeta(metaResponse);

        CodeNameResponse codeNameResponse2 = new CodeNameResponse(metaResponse, codeNameResponseData);

        codeNameResponse2.getData();
        codeNameResponse2.getMeta();

        assertNotNull(codeNameResponse1);
        assertNotNull(codeNameResponse1.toString());
        assertNotNull(codeNameResponse1.hashCode());
        Assert.assertEquals(codeNameResponse1,codeNameResponse2);
    }
}