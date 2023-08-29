package co.com.bancolombia.commonsvnt.common.auditing;

import java.util.Date;

public class AuditingFor extends Auditing {

    public AuditingFor(Date createdDate, Date updatedDate, String createdBy, String updatedBy) {
        super(createdDate,updatedDate,createdBy,updatedBy);

    }

}
