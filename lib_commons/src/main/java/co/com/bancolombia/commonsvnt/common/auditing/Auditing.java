package co.com.bancolombia.commonsvnt.common.auditing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public abstract class Auditing {
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;

}
