package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
public class AcquisitionPdf {
	private UUID acquisitionId;	
    private boolean upgrade;
    private Date completionDate;
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private String documentType;
    private String documentNumber;
    private String expeditionPlace;
    private Date expeditionDate;
    private String birthPlace;
    private Date birthDate;
    private String gender;
    private String matiralStatus;
    private String nationality;
    private String typeAcquisition;

    private ContactInformationPdf contactInformationPdf;
    private EconomicInformationPdf economicInformationPdf;
    private CompanyInformationPdf companyInformationPdf;
    private FinancialInformationPdf financialInformationPdf;
    private TributaryInformationPdf tributaryInformationPdf;
    private InternationalOperationPdf internationalOperationPdf;

    private String allInfo;
}
