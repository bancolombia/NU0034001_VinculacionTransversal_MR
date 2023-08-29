package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.uploaddocument.KofaxInformation;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;

import java.util.List;

public interface UploadDocumentSaveUseCase  {
    void savePersonalInfo(KofaxInformation kofaxInformation, Acquisition acquisition, String usrTransaction);

    void saveBasicInfo(
            KofaxInformation kofaxInformation, Acquisition acquisition, String basicInfGender, String usrTransaction);

    boolean validateQualityFields(KofaxInformation information);

    List<ErrorField> isValidField(String fieldValue, String regex);

    String transformKofaxGenderField (String kofaxGender);

    boolean validateKofaxMessageId (UploadDocumentTotal uploadDocumentTotal, String messageId);

    String getMessageId();
}
