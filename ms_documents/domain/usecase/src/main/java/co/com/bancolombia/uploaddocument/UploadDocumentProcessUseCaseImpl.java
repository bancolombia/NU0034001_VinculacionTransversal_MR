package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_CEDULA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_DIGITALIZATION;

@RequiredArgsConstructor
public class UploadDocumentProcessUseCaseImpl implements UploadDocumentProcessUseCase {

    private final ProcessDocumentUseCase processDocumentUseCase;
    private final UploadDocumentCcRulesUseCase uploadDocumentCcRulesUseCase;
    private final UploadDocumentRutRulesUseCase uploadDocumentRutRulesUseCase;

    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_DIGITALIZATION, OPER_UPLOAD_DOCUMENT);

    @Override
    public UploadDocumentApiResponse processDocument(ProcessDocument processDocument, String userTransaction) {
        adapter.info("INICIANDO PROCESAMIENTO DEL DOCUMENTO");

        UploadDocumentWithLog uploadDocumentWithLog = processDocumentUseCase.consumeKofax(
                processDocument, userTransaction);

        if (uploadDocumentWithLog.getUploadDocumentTotal().getUploadDocumentResponse() != null) {
            String processCode = processDocument.getProcessCode();
            adapter.info("INICIANDO PROCESAMIENTO DEL DOCUMENTO: " + processCode);

            if (processCode.equals(PROCESS_CODE_CEDULA)) {
                adapter.info("PROCESANDO CEDULA");
                ProcessDocumentKofaxTotal processDocumentKofaxTotal = uploadDocumentCcRulesUseCase.validateCcDocument(
                        uploadDocumentWithLog.getUploadDocumentTotal().getUploadDocumentResponse(),
                        processDocument.getAcquisition());
                uploadDocumentWithLog.setProcessDocumentKofaxTotal(processDocumentKofaxTotal);
            } else if (processCode.equals(PROCESS_CODE_RUT)) {
                adapter.info("PROCESANDO RUT");
                ProcessDocumentKofaxTotal processDocumentKofaxTotal = uploadDocumentRutRulesUseCase.validateRutDocument(
                        uploadDocumentWithLog.getUploadDocumentTotal().getUploadDocumentResponse(),
                        processDocument.getAcquisition());
                uploadDocumentWithLog.setProcessDocumentKofaxTotal(processDocumentKofaxTotal);
            }
        }

        adapter.info("FINALIZADO PROCESAMIENTO DEL DOCUMENTO");

        return UploadDocumentApiResponse.builder().data(uploadDocumentWithLog).build();
    }
}
