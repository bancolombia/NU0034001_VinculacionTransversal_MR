package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessParameterGetRequest;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.KofaxRutInformation;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_PENDIENTE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_SAVE_ECONOMIC_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SESSION_ID_NOT_EQUAL;

@RequiredArgsConstructor
public class UploadDocumentRutUseCaseImpl implements UploadDocumentRutUseCase {

	private final VinculationUpdateUseCase vinculationUpdateUseCase;
	private final UploadDocumentSaveUseCase uplDocSaveUseCase;
	private final UploadDocumentUseCase uploadDocumentUseCase;
	private final UploadDocumentRutSaveUseCase uploadDocumentRutSaveUseCase;
	private final UploadDocumentExcepUseCase uploadDocumentExcepUseCase;
	private final UploadDocumentSyncUseCase uploadDocumentSyncUseCase;
	private final UploadDocumentProcessUseCase uploadDocumentProcessUseCase;

	private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_DIGITALIZATION, OPER_UPLOAD_DOCUMENT);

	@Override
	public UploadDocumentWithLog processDocument(UploadDocumentParameter uplDocPar, SqsMessObjUploadDoc obj) {
		String messageId = uplDocSaveUseCase.getMessageId();
		if (uploadDocumentSyncUseCase.validateAsynchronousProcess(uplDocPar.getListGet())) {
			return uploadDocumentSyncUseCase.asynchronousProcess(uplDocPar, messageId);
		} else {
			SqsMessageParamAllObject sqsMessageParamAllObject = constructSqsMessageParamAllObject(uplDocPar, obj,
					messageId);

			if (!uplDocSaveUseCase.validateKofaxMessageId(
					sqsMessageParamAllObject.getUploadDocumentApiResponse().getData().getUploadDocumentTotal(),
					messageId)) {
				vinculationUpdateUseCase.markOperation(uplDocPar.getAcquisition().getId(), CODE_PROCESS_DOCUMENTS,
						CODE_ST_OPE_PENDIENTE);
				uploadDocumentExcepUseCase.validateException(ERROR_CODE_SESSION_ID_NOT_EQUAL, EMPTY, EMPTY,
						sqsMessageParamAllObject);
			}

			if (sqsMessageParamAllObject.getUploadDocumentApiResponse().getData()
					.getProcessDocumentKofaxTotal() != null) {
				validProcessKofax(sqsMessageParamAllObject, uplDocPar);
			} else if (sqsMessageParamAllObject.getUploadDocumentApiResponse().getData().getUploadDocumentTotal()
					.getUploadDocumentErrorResponse() != null) {
				uploadDocumentExcepUseCase.validateBackEndException(
						sqsMessageParamAllObject.getUploadDocumentApiResponse(), sqsMessageParamAllObject);
			}
			return sqsMessageParamAllObject.getUploadDocumentApiResponse().getData();
		}
	}

	private UploadDocumentApiResponse callRestClient(
			UploadDocumentParameter uploadDocumentParameter, String messageId) {

		ProcessDocument processDocument = uploadDocumentUseCase.getRequest(
				SqsMessParameterGetRequest.builder()
						.acquisition(uploadDocumentParameter.getAcquisition())
						.processDocumentFiles(uploadDocumentParameter.getProcessDocumentFiles())
						.documentSubtype(uploadDocumentParameter.getDocumentSubtype())
						.messageId(messageId)
						.sqsMeta(uploadDocumentParameter.getMeta())
						.build());

		return uploadDocumentProcessUseCase.processDocument(
				processDocument, uploadDocumentParameter.getMeta().getUsrMod());
	}

	private void validProcessKofax(
			SqsMessageParamAllObject sqsMessageParamAllObject, UploadDocumentParameter uplDocPar) {

		if (sqsMessageParamAllObject.getUploadDocumentApiResponse().getData().getProcessDocumentKofaxTotal()
				.getKofaxRutInformation() != null) {
			validateAndSaveInformation(sqsMessageParamAllObject.getUploadDocumentApiResponse(),
					uplDocPar.getAcquisition(),
					uplDocPar.getUsrTransaction(), sqsMessageParamAllObject);
		} else {
			ProcessDocumentKofaxError processDocumentKofaxError = sqsMessageParamAllObject
					.getUploadDocumentApiResponse().getData()
					.getProcessDocumentKofaxTotal().getProcessDocumentKofaxError();
			uploadDocumentExcepUseCase.validateBusinessException(processDocumentKofaxError,
					uplDocPar.getAcquisition(), sqsMessageParamAllObject);
		}

	}

	public SqsMessageParamAllObject constructSqsMessageParamAllObject(
			UploadDocumentParameter uplDocPar, SqsMessObjUploadDoc obj, String messageId) {

		return SqsMessageParamAllObject.builder()
				.acquisition(uplDocPar.getAcquisition())
				.uploadDocumentApiResponse(callRestClient(uplDocPar, messageId))
				.sqsMessObjUploadDoc(obj)
				.sqsMessage(uplDocPar.getSqsMessage())
				.build();
	}

	private void validateAndSaveInformation(
			UploadDocumentApiResponse uploadDocumentApiResponse, Acquisition acquisition,
			String userTransaction, SqsMessageParamAllObject sqsMessageParamAllObject) {

		KofaxRutInformation kofaxRutInformation = uploadDocumentApiResponse.getData().getProcessDocumentKofaxTotal()
				.getKofaxRutInformation();

		if (uploadDocumentRutSaveUseCase.validateAndSaveInformation(
				kofaxRutInformation, acquisition, userTransaction, sqsMessageParamAllObject)) {
			vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_COMPLETADO);
		} else {
			adapter.error(ERROR_SAVE_ECONOMIC_INFO);
		}
	}
}
