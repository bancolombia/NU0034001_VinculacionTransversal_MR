package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MyDataInitialTest {

    @Test
    public void validateMyDataInitialTest() {

        MyDataInitial myDataInitial1 = new MyDataInitial("","","");

        MyDataInitial myDataInitial2 = MyDataInitial.builder().build();
        myDataInitial2.setAcquisitionId("");
        myDataInitial2.setDocumentNumber("");
        myDataInitial2.setDocumentType("");

        myDataInitial1.getAcquisitionId();
        myDataInitial1.getDocumentNumber();
        myDataInitial1.getDocumentType();

        assertNotNull(myDataInitial1);
        assertNotNull(myDataInitial1.toString());
        assertNotNull(myDataInitial1.hashCode());
        Assert.assertEquals(myDataInitial1,myDataInitial2);
    }

}