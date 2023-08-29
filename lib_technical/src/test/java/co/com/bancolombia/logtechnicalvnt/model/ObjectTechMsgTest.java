package co.com.bancolombia.logtechnicalvnt.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ObjectTechMsgTest {

    @Test
    public void testHasAppName(){
        ObjectTechMsg objectTechMsg = new ObjectTechMsg("","", "", "", ""
                , new ArrayList<String>(), new Object());

        Assert.assertTrue(objectTechMsg.hasAppName());
    }

    @Test
    public void testHasNotAppName(){
        ObjectTechMsg objectTechMsg = new ObjectTechMsg("", "", "", ""
                , new ArrayList<String>(), new Object());

        Assert.assertFalse(objectTechMsg.hasAppName());
    }

    @Test
    public void testObject(){
        ObjectTechMsg objectTechMsg = new ObjectTechMsg("", "", "", ""
                , new ArrayList<String>(), new Object());

        Assert.assertNotNull(objectTechMsg);
    }

}