package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.model.generatepdf.CompanyInformationPdf;
import co.com.bancolombia.model.generatepdf.ContactInformationPdf;
import co.com.bancolombia.model.generatepdf.EconomicInformationPdf;
import co.com.bancolombia.model.generatepdf.FinancialInformationPdf;
import co.com.bancolombia.model.generatepdf.InternationalOperationPdf;
import co.com.bancolombia.model.generatepdf.TributaryInformationPdf;

public interface GeneratePdfTaxUseCase {
    ContactInformationPdf contactInformationPdf(ContactInfoCompReply contactResInfo);

    EconomicInformationPdf economicInformationPdf(EconomicInfoReply economicInfo);

    CompanyInformationPdf companyInformationPdf(ContactInfoCompReply contactWorkInfo);

    FinancialInformationPdf financialInformationPdf(EconomicInfoReply economicInfo);

    TributaryInformationPdf tributaryInformationPdf(TaxInfoReply taxInfo);

    InternationalOperationPdf internationalOperationPdf(ForeignInfoReply foreignInfo);
}
