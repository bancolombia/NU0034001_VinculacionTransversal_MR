package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.DigitalizationIdentitySave;
import co.com.bancolombia.model.uploaddocument.DigitalizationRutSave;
import co.com.bancolombia.model.uploaddocument.ProcessedFields;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.gateways.DigitalizationIdentityRepository;
import co.com.bancolombia.model.uploaddocument.gateways.DigitalizationRutRepository;
import co.com.bancolombia.util.constants.Constants;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.util.constants.Constants.ASSESSEE_TYPE;
import static co.com.bancolombia.util.constants.Constants.CORPORATE_NAME;
import static co.com.bancolombia.util.constants.Constants.DIFFERENCE_DOC_NR;
import static co.com.bancolombia.util.constants.Constants.EMISSION_RUT_DATE;
import static co.com.bancolombia.util.constants.Constants.FIRST_NAME;
import static co.com.bancolombia.util.constants.Constants.FIRST_SURNAME;
import static co.com.bancolombia.util.constants.Constants.IDENTIFICATION_TYPE;
import static co.com.bancolombia.util.constants.Constants.MAIN_ACTIVITY;
import static co.com.bancolombia.util.constants.Constants.SECONDARY_ACTIVITY;
import static co.com.bancolombia.util.constants.Constants.SECOND_NAME;
import static co.com.bancolombia.util.constants.Constants.SECOND_SURNAME;
import static co.com.bancolombia.util.constants.Constants.TAX_IDENTIFICATION_NUMBER;

@RequiredArgsConstructor
public class ProcessDocumentSaveUseCaseImpl implements ProcessDocumentSaveUseCase {

    private final DigitalizationIdentityRepository digitalizationIdentityRepository;
    private final DigitalizationRutRepository digitalizationRutRepository;
    private final CoreFunctionDate coreFunctionDate;

    @Override
    public void saveDigitalizationIdentity(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction) {

        digitalizationIdentityRepository.save(
                transDigitalizationIdentitySave(uploadDocumentResponse, acquisition, userTransaction));
    }

    @Override
    public void saveDigitalizationRut(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction) {

        digitalizationRutRepository.save(
                transDigitalizationRutSave(uploadDocumentResponse, acquisition, userTransaction));
    }

    @Override
    public DigitalizationIdentitySave transDigitalizationIdentitySave(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction) {

        DigitalizationIdentitySave digitalizationIdentitySave = transDigitalizationIdentitySaveBasic
                (uploadDocumentResponse, acquisition, userTransaction);
        List<ProcessedFields> processedFieldsList = uploadDocumentResponse.getData()
                .getProcessedDocument().get(0).getProcessedFields();
        transDigitalizationIdentitySaveOCR(digitalizationIdentitySave, processedFieldsList);
        transDigitalizationIdentitySaveBCR(digitalizationIdentitySave, processedFieldsList);

        return digitalizationIdentitySave;
    }

    @Override
    public DigitalizationRutSave transDigitalizationRutSave(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction) {

        DigitalizationRutSave digitalizationRutSave = transDigitalizationRutSaveBasic
                (uploadDocumentResponse, acquisition, userTransaction);
        List<ProcessedFields> processedFieldsList = uploadDocumentResponse.getData()
                .getProcessedDocument().get(0).getProcessedFields();
        transDigitalizationRutSaveEconomicInfo(digitalizationRutSave, processedFieldsList);
        transDigitalizationRutSavePersonalInfo(digitalizationRutSave, processedFieldsList);

        return digitalizationRutSave;
    }

    @Override
    public Optional<DigitalizationRutSave> findByAcquisition(UUID acquisitionId) {
        Acquisition acquisition = Acquisition.builder().id(acquisitionId).build();
        return Optional.ofNullable(digitalizationRutRepository.findByAcquisition(acquisition));
    }

    private DigitalizationIdentitySave transDigitalizationIdentitySaveBasic(
            UploadDocumentResponse uploadDocumentResponse, AcquisitionProcessDocument acquisition,
            String userTransaction) {

        return DigitalizationIdentitySave.builder()
                .messageId(uploadDocumentResponse.getMeta().getMessageId())
                .acquisition(Acquisition.builder().id(acquisition.getId()).build())
                .createdBy(userTransaction)
                .createdDate(coreFunctionDate.getDatetime())
                .build();
    }

    private DigitalizationRutSave transDigitalizationRutSaveBasic(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction) {

        return DigitalizationRutSave.builder()
                .messageId(uploadDocumentResponse.getMeta().getMessageId())
                .acquisition(Acquisition.builder().id(acquisition.getId()).build())
                .createdBy(userTransaction)
                .createdDate(coreFunctionDate.getDatetime())
                .build();
    }

    private void transDigitalizationIdentitySaveOCR(
            DigitalizationIdentitySave digitalizationIdentitySave, List<ProcessedFields> processedFieldsList) {

        processedFieldsList.forEach(a -> {
            if (a.getFieldName().equals(Constants.OCR_FIRST_NAMES)) {
                digitalizationIdentitySave.setFirstNamesOCR(a.getFieldValue());
                digitalizationIdentitySave.setFnConfidenceOCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.OCR_LAST_NAMES)) {
                digitalizationIdentitySave.setLastNamesOCR(a.getFieldValue());
                digitalizationIdentitySave.setLnConfidenceOCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.OCR_DOC_NR)) {
                digitalizationIdentitySave.setDocNumberOCR(a.getFieldValue());
                digitalizationIdentitySave.setDnConfidenceOCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.OCR_BIRTH_DATE)) {
                digitalizationIdentitySave.setBirthDateOCR(a.getFieldValue());
                digitalizationIdentitySave.setBdConfidenceOCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.OCR_BIRTH_PLACE)) {
                digitalizationIdentitySave.setBirthPlaceOCR(a.getFieldValue());
                digitalizationIdentitySave.setBpConfidenceOCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.OCR_EMISSION_DATE)) {
                digitalizationIdentitySave.setEmissionDateOCR(a.getFieldValue());
                digitalizationIdentitySave.setEdConfidenceOCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.OCR_EMISSION_PLACE)) {
                digitalizationIdentitySave.setEmissionPlaceOCR(a.getFieldValue());
                digitalizationIdentitySave.setEpConfidenceOCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.OCR_GENDER)) {
                digitalizationIdentitySave.setGenderOCR(a.getFieldValue());
                digitalizationIdentitySave.setGdConfidenceOCR(a.getConfidencePercentage());
            }
        });
    }

    private void transDigitalizationIdentitySaveBCR(
            DigitalizationIdentitySave digitalizationIdentitySave, List<ProcessedFields> processedFieldsList) {

        processedFieldsList.forEach(a -> {
            if (a.getFieldName().equals(Constants.BCR_DOC_NR)) {
                digitalizationIdentitySave.setDocNumberBCR(a.getFieldValue());
                digitalizationIdentitySave.setDnConfidenceBCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.BCR_FIRST_NAME)) {
                digitalizationIdentitySave.setFirstNamesBCR(a.getFieldValue());
                digitalizationIdentitySave.setFnConfidenceBCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.BCR_LAST_NAME)) {
                digitalizationIdentitySave.setLastNamesBCR(a.getFieldValue());
                digitalizationIdentitySave.setLnConfidenceBCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.BCR_GENDER)) {
                digitalizationIdentitySave.setGenderBCR(a.getFieldValue());
                digitalizationIdentitySave.setGdConfidenceBCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(Constants.BCR_BIRTH_DATE)) {
                digitalizationIdentitySave.setBirthDateBCR(a.getFieldValue());
                digitalizationIdentitySave.setBdConfidenceBCR(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(DIFFERENCE_DOC_NR)) {
                digitalizationIdentitySave.setCoincidenceDocNumber(a.getFieldValue());
            }
        });
    }

    private void transDigitalizationRutSaveEconomicInfo(
            DigitalizationRutSave digitalizationRutSave, List<ProcessedFields> processedFieldsList) {

        processedFieldsList.forEach(a -> {
            if (a.getFieldName().equals(TAX_IDENTIFICATION_NUMBER)) {
                digitalizationRutSave.setTaxIdentificationNumber(a.getFieldValue());
                digitalizationRutSave.setTinConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(CORPORATE_NAME)) {
                digitalizationRutSave.setCorporateName(a.getFieldValue());
                digitalizationRutSave.setCnConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(MAIN_ACTIVITY)) {
                digitalizationRutSave.setMainActivity(a.getFieldValue());
                digitalizationRutSave.setMaConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(SECONDARY_ACTIVITY)) {
                digitalizationRutSave.setSecondaryActivity(a.getFieldValue());
                digitalizationRutSave.setSaConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(EMISSION_RUT_DATE)) {
                digitalizationRutSave.setEmissionDate(a.getFieldValue());
                digitalizationRutSave.setEdConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(ASSESSEE_TYPE)) {
                digitalizationRutSave.setAssesseeType(a.getFieldValue());
                digitalizationRutSave.setAtConfidence(a.getConfidencePercentage());
            }
        });
    }

    private void transDigitalizationRutSavePersonalInfo(
            DigitalizationRutSave digitalizationRutSave, List<ProcessedFields> processedFieldsList) {

        processedFieldsList.forEach(a -> {
            if (a.getFieldName().equals(FIRST_NAME)) {
                digitalizationRutSave.setFirstName(a.getFieldValue());
                digitalizationRutSave.setFnConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(SECOND_NAME)) {
                digitalizationRutSave.setSecondName(a.getFieldValue());
                digitalizationRutSave.setSnConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(FIRST_SURNAME)) {
                digitalizationRutSave.setFirstSurname(a.getFieldValue());
                digitalizationRutSave.setFsConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(SECOND_SURNAME)) {
                digitalizationRutSave.setSecondSurname(a.getFieldValue());
                digitalizationRutSave.setSsConfidence(a.getConfidencePercentage());
            } else if (a.getFieldName().equals(IDENTIFICATION_TYPE)) {
                digitalizationRutSave.setIdentificationType(a.getFieldValue());
                digitalizationRutSave.setItConfidence(a.getConfidencePercentage());
            }
        });
    }
}
