package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.digitalizationprocesseddocuments.DigitalizationProcessedDocumentsUseCase;
import co.com.bancolombia.model.digitalizationprocesseddocuments.DigitalizationProcessedDocuments;
import co.com.bancolombia.model.sqs.SqsMessFileObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.URL_BUCKET_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.URL_BUCKET_DOC_PROCESSED;

@RequiredArgsConstructor
public class UploadDocumentProcessedDocumentsUseCaseImpl implements UploadDocumentProcessedDocumentsUseCase {

    private final CoreFunctionDate coreFunctionDate;
    private final DigitalizationProcessedDocumentsUseCase digitalizationProcessedDocumentsUseCase;
    private final UploadDocumentSyncUseCase uploadDocumentSyncUseCase;
    private final DataFileRepository dataFileRepository;

    @Override
    public void saveProcessedDocuments(
            UploadDocumentParameter uploadDocumentParameter, Date responseDate,
            SqsMessageParamAllObject sqsMessageParamAllObject, Map<String, String> values) {

        List<UploadDocumentFile> uploadDocumentFiles = uploadDocumentParameter.getListGet();
        List<ProcessDocumentFiles> successFiles = uploadDocumentParameter.getProcessDocumentFiles();
        if (uploadDocumentFiles != null) {
            List<String> filesNames = uploadDocumentFiles.stream()
                    .map(UploadDocumentFile::getFileName)
                    .collect(Collectors.toList());

            DigitalizationProcessedDocuments digitalizationProcessedDocuments = DigitalizationProcessedDocuments
                    .builder()
                    .createdBy(uploadDocumentParameter.getUsrTransaction())
                    .createdDate(coreFunctionDate.getDatetime())
                    .flagDataExtraction(uploadDocumentFiles.get(0).getFlagDataExtraction())
                    .flagSynchronous(uploadDocumentFiles.get(0).getFlagSynchronous())
                    .documentalTypeCode(uploadDocumentFiles.get(0).getDocumentalTypeCode())
                    .documentalSubTypeCode(uploadDocumentFiles.get(0).getDocumentalSubTypeCode())
                    .processingDate(responseDate)
                    .filesNames(filesNames)
                    .acquisition(uploadDocumentParameter.getAcquisition())
                    .build();

            digitalizationProcessedDocumentsUseCase.save(digitalizationProcessedDocuments);
            moveFileDocumentsBucket(filesNames);
        } else if(successFiles != null && !values.isEmpty()) {
            saveSuccessfulFiles(successFiles, uploadDocumentParameter, responseDate, values);
        } else {
            saveProcessedDocuments(sqsMessageParamAllObject);
        }
    }

    @Override
    public void saveProcessedDocuments(SqsMessageParamAllObject sqsMessageParamAllObject) {
        List<SqsMessFileObjUploadDoc> uploadDocumentFiles = sqsMessageParamAllObject
                .getSqsMessObjUploadDoc().getData().getFiles();

        List<String> filesNames = uploadDocumentFiles.stream()
                .map(SqsMessFileObjUploadDoc::getFileName)
                .collect(Collectors.toList());

        DigitalizationProcessedDocuments digitalizationProcessedDocuments = DigitalizationProcessedDocuments.builder()
                .createdBy(sqsMessageParamAllObject.getSqsMessObjUploadDoc().getMeta().getUsrMod())
                .createdDate(coreFunctionDate.getDatetime())
                .flagDataExtraction(uploadDocumentFiles.get(0).getFlagDataExtraction())
                .flagSynchronous(uploadDocumentFiles.get(0).getFlagSynchronous())
                .documentalTypeCode(uploadDocumentFiles.get(0).getDocumentalTypeCode())
                .documentalSubTypeCode(uploadDocumentFiles.get(0).getDocumentalSubTypeCode())
                .processingDate(sqsMessageParamAllObject.getUploadDocumentApiResponse().getData().getInfoReuseCommon()
                        .getDateResponseReuse())
                .filesNames(filesNames)
                .acquisition(sqsMessageParamAllObject.getAcquisition())
                .build();

        digitalizationProcessedDocumentsUseCase.save(digitalizationProcessedDocuments);
        moveFileDocumentsBucket(filesNames);
    }

    @Override
    public boolean validateAsynchronousProcess(List<UploadDocumentFile> listGet) {
        return uploadDocumentSyncUseCase.validateAsynchronousProcess(listGet);
    }

    @Override
    public void moveFileDocumentsBucket(List<String> fileNames){
        fileNames.forEach(s -> dataFileRepository.moveBucketObject(
                URL_BUCKET_DOCUMENTS + s, URL_BUCKET_DOC_PROCESSED + s));
    }

    private void saveSuccessfulFiles(
            List<ProcessDocumentFiles> successFiles, UploadDocumentParameter uploadDocumentParameter,
            Date responseDate, Map<String, String> values) {

        List<String> filesNames = successFiles.stream()
                .map(ProcessDocumentFiles::getFileName)
                .collect(Collectors.toList());

        DigitalizationProcessedDocuments digitalizationProcessedDocuments = DigitalizationProcessedDocuments
                .builder()
                .createdBy(uploadDocumentParameter.getUsrTransaction())
                .createdDate(coreFunctionDate.getDatetime())
                .flagDataExtraction(values.get("flagDataExtraction"))
                .flagSynchronous(values.get("flagSynchronous"))
                .documentalTypeCode(values.get("documentalTypeCode"))
                .documentalSubTypeCode(values.get("documentalSubTypeCode"))
                .processingDate(responseDate)
                .filesNames(filesNames)
                .acquisition(uploadDocumentParameter.getAcquisition())
                .build();

        digitalizationProcessedDocumentsUseCase.save(digitalizationProcessedDocuments);
        moveFileDocumentsBucket(filesNames);
    }
}
