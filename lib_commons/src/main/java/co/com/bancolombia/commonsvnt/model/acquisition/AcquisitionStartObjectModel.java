package co.com.bancolombia.commonsvnt.model.acquisition;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AcquisitionStartObjectModel {
	private String documentType;
	private String documentNumber;
	private String typePerson;
	private String typeProduct;
	private String typeChannel;
	private String businessLine;
	private String typeAcquisition;
}
