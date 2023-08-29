package co.com.bancolombia.api.response;

import co.com.bancolombia.api.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentApiResponseData;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentFilesListResponse;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequest;
import co.com.bancolombia.api.model.validatedataextraction.ValidateDataExtractionResponse;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.validatedataextraction.ValidateDataExtraction;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT_RUT_EXTRACTION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT_RUT_EXTRACTION_REASON;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_CODE_THREE;

public class ResponseFactoryDigitization {

    private ResponseFactoryDigitization() {
    }

    public static UploadDocumentApiResponse buildUploadDocumentResponse(
            UploadDocumentRequest request, UploadDocumentResponse uploadDocumentResponse) {

        List<UploadDocumentFilesListResponse> processedDocumentsList = new ArrayList<>();
        uploadDocumentResponse.getData().getProcessedDocument().forEach(processedDocument ->
                request.getData().getFilesList().forEach(uploadDocumentFilesListRequest ->
                        processedDocumentsList.add(UploadDocumentFilesListResponse.builder()
                                .documentalTypeCode(uploadDocumentFilesListRequest.getDocumentalTypeCode())
                                .documentalSubTypeCode(uploadDocumentFilesListRequest.getDocumentalSubTypeCode())
                                .fileName(uploadDocumentFilesListRequest.getFileName())
                                .outComeCode(processedDocument.getCodeAnswerDocument())
                                .outComeName(processedDocument.getAnswerDocument())
                                .reason(processedDocument.getReason() != null ? processedDocument.getReason() : EMPTY)
                                .build())
                )
        );

        UploadDocumentApiResponseData uploadDocumentApiResponseData = UploadDocumentApiResponseData.builder()
                .processedDocuments(processedDocumentsList)
                .build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());

        return UploadDocumentApiResponse.builder().data(uploadDocumentApiResponseData).meta(metaResponse).build();
    }

    public static UploadDocumentApiResponse buildUploadDocumentNotRutExtractionResponse(UploadDocumentRequest request) {
        List<UploadDocumentFilesListResponse> processedDocumentsList = new ArrayList<>();
        request.getData().getFilesList().forEach(uploadDocument ->
                processedDocumentsList.add(UploadDocumentFilesListResponse.builder()
                        .documentalTypeCode(uploadDocument.getDocumentalTypeCode())
                        .documentalSubTypeCode(uploadDocument.getDocumentalSubTypeCode())
                        .fileName(uploadDocument.getFileName())
                        .outComeCode(OUT_COME_CODE_THREE)
                        .outComeName(NOT_RUT_EXTRACTION)
                        .reason(NOT_RUT_EXTRACTION_REASON)
                        .build())
        );

        UploadDocumentApiResponseData uploadDocumentApiResponseData = UploadDocumentApiResponseData.builder()
                .processedDocuments(processedDocumentsList)
                .build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());

        return UploadDocumentApiResponse.builder().data(uploadDocumentApiResponseData).meta(metaResponse).build();
    }

    public static ValidateDataExtractionResponse buildValidateDataExtractionResponse(
            MetaRequest metaRequest, ValidateDataExtraction validateDataExtraction) {
        MetaResponse metaResponse = MetaResponse.fromMeta(metaRequest);
        return ValidateDataExtractionResponse.builder().meta(metaResponse)
                .data(validateDataExtraction.getUploadDocumentApiResponseData()).build();
    }
}
