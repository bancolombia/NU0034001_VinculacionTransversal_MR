package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO_AUTO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTS_FOLDER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_ECONOMIC_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PDF_TEXT_GUION_BAJO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_ID_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PENDING_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY;

@RequiredArgsConstructor
public class UploadDocumentValidateUseCaseImpl implements UploadDocumentValidateUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final NaturalPersonUseCase naturalPersonUseCase;
    private final DataFileRepository dataFileRepository;
    private final UploadDocumentExcepUseCase uploadDocumentExcepUseCase;

    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_DIGITALIZATION, OPER_UPLOAD_DOCUMENT);

    @Override
    public List<ProcessDocumentFiles> getProcessDocumentFiles(String documentSubtype,
            List<UploadDocumentFile> listGetDocument, SqsMessageParamAllObject sqsMessageParamAllObject) {

        List<UploadDocumentFile> listGet = homologueAllFlagDocuments(listGetDocument);
        List<ProcessDocumentFiles> processDocumentFiles = new ArrayList<>();

        listGet.stream().forEach(item -> {
        	Optional<String> ext = getExtension(item.getFileName());
            String fileName = item.getDocumentalSubTypeCode() + PDF_TEXT_GUION_BAJO
                    + item.getDocumentType() + PDF_TEXT_GUION_BAJO + item.getDocumentnumber();
            String base64 = EMPTY;
            if (item.getFileName().contains(fileName)) {
                try {
                    base64 = dataFileRepository.getBase64File(item.getFileName(), DOCUMENTS_FOLDER);
                } catch (IOException e) {
                    adapter.error(Constants.BASE_64_CONVERT_ERROR, e);
                    uploadDocumentExcepUseCase.validateException(ERROR_CODE_UPLOAD_DOCUMENT_RETRY,
                            documentSubtype.equals(CEDULA_SUBTYPE) ? TYPE_ID_CC : TYPE_RUT, EMPTY,
                            sqsMessageParamAllObject);
                }
            } else {
                uploadDocumentExcepUseCase.validateException(ERROR_CODE_UPLOAD_DOCUMENT_RETRY,
                        documentSubtype.equals(CEDULA_SUBTYPE) ? TYPE_ID_CC : TYPE_RUT, EMPTY,
                        sqsMessageParamAllObject);
            }
            processDocumentFiles.add(ProcessDocumentFiles.builder()
                    .fileName(item.getFileName()).base64(base64)
                    .extension(transformExtToMymeType(ext.get()))
                    .build());
        });
        return processDocumentFiles;
    }

    @Override
    public boolean validateRutDataExtraction(
            Acquisition acquisition, SqsMessageParamAllObject sqsMessageParamAllObject) {

        boolean rutDataExtraction = false;
        InfoRutReply infoRut = naturalPersonUseCase.getRequiredRut(acquisition.getId());

        if (infoRut.isValid()) {
            rutDataExtraction = infoRut.isRequiredRut();
            if (!rutDataExtraction) {
                vinculationUpdateUseCase.markOperation(
                        acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_COMPLETADO_AUTO);
            }
        } else {
            uploadDocumentExcepUseCase.validateException(ERROR_CODE_PENDING_OPERATION,
                    OPER_ECONOMIC_INF, EMPTY, sqsMessageParamAllObject);
        }
        return rutDataExtraction;
    }

    private Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf('.') + Numbers.ONE.getIntNumber()));
    }

    private String transformExtToMymeType(String ext) {
        String myme;
        if (ext.equals(Constants.PDF_EXT)) {
            myme = Constants.MYME_PDF;
        } else if (ext.equals(Constants.JPEG_EXT) || ext.equals(Constants.JPG_EXT)) {
            myme = Constants.MYME_JPEG;
        } else if (ext.equals(Constants.TIF_EXT) || ext.equals(Constants.TIFF_EXT)) {
            myme = Constants.MYME_TIFF;
        } else {
            myme = Constants.MYME_PNG;
        }

        return myme;
    }

    private List<UploadDocumentFile> homologueAllFlagDocuments(List<UploadDocumentFile> listGet) {
        List<UploadDocumentFile> list = listGet;
        boolean getFlagSynchronous = listGet.stream()
                .anyMatch(pre -> FOREIGN_INFORMATION.equals(pre.getFlagSynchronous()));
        boolean getFlagDataExtraction = listGet.stream()
                .anyMatch(pre -> FOREIGN_INFORMATION.equals(pre.getFlagDataExtraction()));

        if (getFlagDataExtraction || getFlagSynchronous) {
            list.forEach(item -> {
                if (getFlagSynchronous) {
                    item.setFlagSynchronous(FOREIGN_INFORMATION);
                }
                if (getFlagDataExtraction) {
                    item.setFlagDataExtraction(FOREIGN_INFORMATION);
                }
            });
        }
        return list;
    }
}