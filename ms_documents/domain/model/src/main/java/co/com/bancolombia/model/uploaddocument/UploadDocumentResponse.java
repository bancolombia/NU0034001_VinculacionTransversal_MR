package co.com.bancolombia.model.uploaddocument;

import co.com.bancolombia.commonsvnt.api.model.util.Meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UploadDocumentResponse {
    private Meta meta;
    private UploadDocument data;
}
