package co.com.bancolombia.commonsvnt.model;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InfoReuseCommonTest {

    @Test
    public void modelInfoReuseTest() {
        InfoReuseCommon irc = InfoReuseCommon.builder().build();
        irc.setRequestReuse("");
        irc.setDateRequestReuse(new Date());
        irc.setResponseReuse("");
        irc.setDateResponseReuse(new Date());
        irc.setMapFields(new HashMap<>());
        irc.getRequestReuse();
        irc.getDateRequestReuse();
        irc.getResponseReuse();
        irc.getDateResponseReuse();
        irc.getMapFields();
        assertNotNull(irc);


    }

    @Test
    public void modelEqualsInfoReuseTest(){
        Date dateRequestTest = new Date();
        Date dateResponseTest = new Date();
        InfoReuseCommon irc = new InfoReuseCommon();
        irc.setRequestReuse("");
        irc.setDateRequestReuse(dateRequestTest);
        irc.setResponseReuse("");
        irc.setDateResponseReuse(dateResponseTest);
        irc.setMapFields(new HashMap<>());

        InfoReuseCommon irc2 = new InfoReuseCommon("",dateRequestTest,"",dateResponseTest,new HashMap<>());

        irc.toString();
        irc2.hashCode();
        assertNotNull(irc.toString());
        assertNotNull(irc.hashCode());
        assertEquals(irc,irc2);
    }

    @Test
    public void modelAnotherInfoReuseTest(){
        InfoReuseCommon irc = new InfoReuseCommon().toBuilder()
                .responseReuse("")
                .dateResponseReuse(new Date())
                .dateRequestReuse(new Date())
                .requestReuse("")
                .mapFields(new HashMap<>()).build();

        assertNotNull(irc.toString());
    }




}