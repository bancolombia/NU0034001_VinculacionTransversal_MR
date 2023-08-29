package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.GenExposeReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NATIONALITY;

@RequiredArgsConstructor
public class GeneratePdfUtilTwoUseCaseImpl implements GeneratePdfUtilTwoUseCase {

    private final NaturalPersonUseCase naturalPersonUseCase;
    private final GeneratePdfUtilOneUseCase generatePdfUtilOneUseCase;
    private final GeneratePdfTaxUseCase generatePdfTaxUseCase;
    private final GeneratePdfConvertJson generatePdfConvertJson;

    @Override
    public Optional<AcquisitionPdf> validateAndFindInfo(Acquisition acquisition) {
        CheckList checkList = generatePdfUtilOneUseCase.checkListFindByOperation(acquisition, CODE_PROCESS_DOCUMENTS);

        boolean continueProcess = true;
        if (!checkList.getState().getCode().equals(CODE_ST_OPE_COMPLETADO)) {
            continueProcess = false;
        }

        AcquisitionPdf acquisitionPdf = null;
        if (continueProcess) {
            acquisitionPdf = findPersonalInformation(acquisition);
        }

        return Optional.ofNullable(acquisitionPdf);
    }

    private AcquisitionPdf findPersonalInformation(Acquisition acquisition) {
        GenExposeReply reply = naturalPersonUseCase.getGenerateExposeInfo(acquisition.getId());

        AcquisitionPdf acquisitionPdf = AcquisitionPdf.builder()
                .gender(reply.getBasicInfo().getGender())
                .documentType(acquisition.getDocumentType().getCode())
                .documentNumber(acquisition.getDocumentNumber())
                .acquisitionId(acquisition.getId())
                .upgrade(false)
                .completionDate(acquisition.getCreatedDate())
                .firstName(getValueNotNull(reply.getPersonalInfo().getFirstName()))
                .secondName(getValueNotNull(reply.getPersonalInfo().getSecondName()))
                .firstSurname(getValueNotNull(reply.getPersonalInfo().getFirstSurname()))
                .secondSurname(getValueNotNull(reply.getPersonalInfo().getSecondSurname()))
                .matiralStatus(getValueNotNull(reply.getBasicInfo().getCivilStatus()))
                .expeditionDate(reply.getPersonalInfo().getExpeditionDate())
                .expeditionPlace(generatePdfUtilOneUseCase.getGeographicName(
                        reply.getPersonalInfo().getExpeditionPlace(), reply.getPersonalInfo().getExpeditionCountry(),
                        Numbers.ONE.getIntNumber()))
                .birthDate(reply.getPersonalInfo().getBirthdate())
                .birthPlace(generatePdfUtilOneUseCase.getGeographicName(
                        reply.getBasicInfo().getBirthCity(), reply.getBasicInfo().getBirthDepartment(),
                        Numbers.FOUR.getIntNumber()))
                .nationality(generatePdfUtilOneUseCase.getCatalogName(
                        NATIONALITY, reply.getBasicInfo().getNationality()))
                .typeAcquisition(acquisition.getTypeAcquisition().getCode())
                .build();

        setPersonalInformation(acquisitionPdf, reply);
        acquisitionPdf.setAllInfo(generatePdfConvertJson.getInfo(acquisitionPdf).toString());

        return acquisitionPdf;
    }

    private void setPersonalInformation(AcquisitionPdf acquisitionPdf, GenExposeReply reply) {
        acquisitionPdf.setContactInformationPdf(generatePdfTaxUseCase.contactInformationPdf(reply.getContactResInfo()));
        acquisitionPdf.setEconomicInformationPdf(generatePdfTaxUseCase.economicInformationPdf(reply.getEconomicInfo()));
        acquisitionPdf.setCompanyInformationPdf(
                generatePdfTaxUseCase.companyInformationPdf(reply.getContactWorkInfo()));
        acquisitionPdf.setFinancialInformationPdf(
                generatePdfTaxUseCase.financialInformationPdf(reply.getEconomicInfo()));
        acquisitionPdf.setTributaryInformationPdf(generatePdfTaxUseCase.tributaryInformationPdf(reply.getTaxInfo()));
        acquisitionPdf.setInternationalOperationPdf(
                generatePdfTaxUseCase.internationalOperationPdf(reply.getForeignInfo()));
    }

    private String getValueNotNull(String value) {
        if (value == null) {
            return EMPTY;
        }
        return value;
    }
}
