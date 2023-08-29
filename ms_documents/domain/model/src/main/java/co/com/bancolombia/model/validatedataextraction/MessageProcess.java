package co.com.bancolombia.model.validatedataextraction;

import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageProcess {
    ProcessDocument data;
}
