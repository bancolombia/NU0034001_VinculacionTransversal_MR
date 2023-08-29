package co.com.bancolombia.commonsvnt.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorField {
    private String name;
    private String title;
    private String message;
    private String complement;
    private String nameList;
}
