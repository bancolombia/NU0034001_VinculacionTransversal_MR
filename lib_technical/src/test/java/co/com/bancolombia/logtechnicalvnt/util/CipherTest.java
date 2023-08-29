package co.com.bancolombia.logtechnicalvnt.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CipherTest {

    @Test
    public void cipherMessageTest() throws IOException {
        List<String> fieldsToHide = new ArrayList<String>();
        String jsonString = "{}";

        Assert.assertNotNull(Cipher.cipherMessage(jsonString, fieldsToHide));
    }

    @Test
    public void cipherMessagefieldstohidenotemptyTest() throws IOException {
        List<String> fieldsToHide = new ArrayList<String>();
        fieldsToHide.add("aaaa");
        fieldsToHide.add("cccc");
        String jsonString = "{\"cccc\":\"dddd\"}";

        Assert.assertNotNull(Cipher.cipherMessage(jsonString, fieldsToHide));
    }
}