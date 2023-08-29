package co.com.bancolombia.model.uploaddocument;

import co.com.bancolombia.commonsvnt.api.model.util.Meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UploadDocumentErrorResponse {
    private Meta meta;
    private String status;
    private String title;
    private List<UploadDocumentError> errors;
}
