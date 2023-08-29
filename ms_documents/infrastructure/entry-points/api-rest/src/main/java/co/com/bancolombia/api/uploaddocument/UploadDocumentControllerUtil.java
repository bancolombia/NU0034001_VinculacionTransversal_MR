package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequest;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.model.documentretries.DocumentRetries;
import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentRutUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentUseCase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;

@AllArgsConstructor
public class UploadDocumentControllerUtil {

    @Autowired
    private UploadDocumentUseCase uploadDocumentUseCase;

    @Autowired
    private UploadDocumentRutUseCase uploadDocumentRutUseCase;

    @Autowired
    private UploadDocumentControllerEco uploadDocumentControllerEco;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private DocumentRetriesUseCase documentRetriesUseCase;

    @Autowired
    private UploadDocumentControllerFields uploadDocumentControllerFields;

    public void finallyStep(GenericStep genericStep, UploadDocumentRequestData data,
            Acquisition acquisition, InfoReuseCommon infoReuseCommon) {

        infoReuseCommon.setMapFields(uploadDocumentControllerEco.findCoincidenceCiiu(acquisition, data));
        genericStep.finallyStep(data.getAcquisitionId(), infoReuseCommon, CODE_PROCESS_DOCUMENTS);
    }

    public String getDocumentSubtype(UploadDocumentRequest uploadDocumentRequest) {
        return uploadDocumentRequest.getData().getFilesList().get(0).getDocumentalSubTypeCode();
    }

    public UploadDocumentWithLog getUploadDocumentWithLog(
            Acquisition acquisition, List<ProcessDocumentFiles> processDocumentFiles,
            UploadDocumentRequest uploadDocumentRequest, List<UploadDocumentFile> listGet) {

        String documentSubtype = getDocumentSubtype(uploadDocumentRequest);
        UploadDocumentWithLog uploadDocumentWithLog;
        UploadDocumentParameter uploadDocumentParameter = UploadDocumentParameter.builder()
                .acquisition(acquisition)
                .processDocumentFiles(processDocumentFiles)
                .documentSubtype(documentSubtype)
                .usrTransaction(uploadDocumentRequest.getMeta().getUsrMod())
                .listGet(listGet)
                .meta(SqsMetaUploadDocument.builder()
                        .usrMod(uploadDocumentRequest.getMeta().getUsrMod())
                        .ip(uploadDocumentRequest.getMeta().getIp())
                        .systemId(uploadDocumentRequest.getMeta().getSystemId())
                        .messageId(uploadDocumentRequest.getMeta().getMessageId())
                        .version(uploadDocumentRequest.getMeta().getVersion())
                        .requestDate(uploadDocumentRequest.getMeta().getRequestDate())
                        .service(uploadDocumentRequest.getMeta().getService())
                        .operation(uploadDocumentRequest.getMeta().getOperation())
                        .build())
                .build();

        if (documentSubtype.equals(CEDULA_SUBTYPE)) {
            uploadDocumentWithLog = uploadDocumentUseCase.processDocument(uploadDocumentParameter, null);
        } else {
            uploadDocumentWithLog = uploadDocumentRutUseCase.processDocument(uploadDocumentParameter, null);
        }

        uploadDocumentControllerEco.saveProcessedDocumentsSuccessCase(
                uploadDocumentParameter, uploadDocumentWithLog, listGet);

        return uploadDocumentWithLog;
    }

    public Acquisition getAcquisition(UploadDocumentRequestData data) {
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_PROCESS_DOCUMENTS);

        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build())
                .typeAcquisition(TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .uploadDocumentRetries(0).uploadRutRetries(0).build();

        Optional<DocumentRetries> documentRetries = documentRetriesUseCase.findByAcquisition(acquisition);
        if (documentRetries.isPresent()) {
            acquisition.setUploadDocumentRetries(documentRetries.get().getUploadDocumentRetries());
            acquisition.setUploadRutRetries(documentRetries.get().getUploadRutRetries());
        }

        return acquisition;
    }

    public void validateOptionalList(UploadDocumentRequestData data) {
        uploadDocumentControllerFields.validateOptionalList(data);
    }
}
