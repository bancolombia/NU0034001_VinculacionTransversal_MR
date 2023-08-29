package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequestData;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentProcessedDocumentsUseCase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COINCIDENCE_CIIU_FIELD;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT_APPLY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;

@AllArgsConstructor
public class UploadDocumentControllerEco {

    @Autowired
    private NaturalPersonUseCase naturalPersonUseCase;

    @Autowired
    private UploadDocumentProcessedDocumentsUseCase uploadDocumentProcessedDocumentsUseCase;

    public Map<String, String> findCoincidenceCiiu(Acquisition acquisition, UploadDocumentRequestData data) {
        String value = NOT_APPLY;
        Map<String, String> map = new HashMap<>();
        if (data.getFilesList().get(0).getDocumentalSubTypeCode().equals(RUT_SUBTYPE)) {
            InfoRutReply reply = naturalPersonUseCase.getRequiredRut(acquisition.getId());
            if (reply.isValid() && reply.getCiiu() != null) {
                value = reply.getFlagCiiu();
            }
        }
        map.put(COINCIDENCE_CIIU_FIELD, value);
        return map;
    }

    public void saveProcessedDocumentsSuccessCase(UploadDocumentParameter uploadDocumentParameter,
            UploadDocumentWithLog uploadDocumentWithLog, List<UploadDocumentFile> listGet) {
        if (!uploadDocumentProcessedDocumentsUseCase.validateAsynchronousProcess(listGet)) {
            uploadDocumentProcessedDocumentsUseCase.saveProcessedDocuments(
                    uploadDocumentParameter, uploadDocumentWithLog.getInfoReuseCommon().getDateResponseReuse(),
                    SqsMessageParamAllObject.builder().build(), new HashMap<>());
        }
    }
}
