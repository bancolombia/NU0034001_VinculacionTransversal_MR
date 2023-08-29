package co.com.bancolombia.model.generatepdf;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class GeneratePdf extends Auditing {
	private UUID id;
	private String codeResult;
	private String urlFileClient;
	private String urlFileCustody;
	private Date dateRequest;
	private Date dateResponse;
	private Acquisition acquisition;
	private InfoReuseCommon infoReuseCommon;
}
