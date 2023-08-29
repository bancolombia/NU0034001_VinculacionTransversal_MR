package co.com.bancolombia.common;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder(toBuilder = true)
public abstract class Auditing {
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;	
}
