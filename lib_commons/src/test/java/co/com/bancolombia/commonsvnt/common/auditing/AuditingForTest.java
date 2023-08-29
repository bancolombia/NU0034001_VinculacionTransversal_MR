package co.com.bancolombia.commonsvnt.common.auditing;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class AuditingForTest {

    @Test
    public void validateAuditingForTest(){
        Date date = new Date();
        AuditingFor auditingFor1 = new AuditingFor(date,date,"","");
        AuditingFor auditingFor2 = new AuditingFor(date,date,"","");

        assertNotNull(auditingFor1);
        assertNotNull(auditingFor1.toString());
        assertNotNull(auditingFor1.hashCode());
        Assert.assertEquals(auditingFor1,auditingFor2);
    }

}