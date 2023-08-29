package co.com.bancolombia.model.uploaddocument;

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
public class ProcessedDocument {
    private String fileId;
    private String codeAnswerDocument;
    private String answerDocument;
    private String reason;
    private List<ProcessedFields> processedFields;
}
