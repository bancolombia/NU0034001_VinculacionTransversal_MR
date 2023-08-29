package co.com.bancolombia.model.generatepdf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GeneratePdfResponseData {
	private UUID acquisitionId;
	private String statusGeneracionPdf;
	private String urlFileClient;
	private String urlFileCustodie;
}
