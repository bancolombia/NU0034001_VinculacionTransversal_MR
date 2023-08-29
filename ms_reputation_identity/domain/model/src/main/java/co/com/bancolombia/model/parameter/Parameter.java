package co.com.bancolombia.model.parameter;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class Parameter {

    private String id;
    private String code;
    private String name;
    private String parent;
    private String typeAcquisition;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
}
