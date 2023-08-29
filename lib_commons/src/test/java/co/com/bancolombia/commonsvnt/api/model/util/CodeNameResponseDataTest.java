package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CodeNameResponseDataTest {

    @Test
    public void validateCodeNameResponseDataTest() {
        CodeNameResponseData codeNameResponseData1 = CodeNameResponseData.builder().build();
        CodeNameResponseData codeNameResponseData2 = new CodeNameResponseData("","");

        codeNameResponseData1.setCode("");
        codeNameResponseData1.setName("");

        codeNameResponseData1.getCode();
        codeNameResponseData1.getName();

        assertNotNull(codeNameResponseData1);
        assertNotNull(codeNameResponseData1.toString());
        assertNotNull(codeNameResponseData1.hashCode());
        Assert.assertEquals(codeNameResponseData1,codeNameResponseData2);
    }
}