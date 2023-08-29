package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessParameterGetRequest;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.KofaxInformation;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_PENDIENTE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_CEDULA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_NAME_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_ID_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SESSION_ID_NOT_EQUAL;

@RequiredArgsConstructor
public class UploadDocumentUseCaseImpl implements UploadDocumentUseCase {

	private final VinculationUpdateUseCase vinculationUpdateUseCase;
	private final UploadDocumentSaveUseCase uploadDocumentSaveUseCase;
	private final UploadDocumentExcepUseCase uplDocExcepUseCase;
	private final UploadDocumentSyncUseCase uploadDocumentSyncUseCase;
	private final UploadDocumentValidateErrors uploadDocumentValidateErrors;
	private final UploadDocumentProcessUseCase uploadDocumentProcessUseCase;

	@Override
	public UploadDocumentWithLog processDocument(UploadDocumentParameter uplDocPar, SqsMessObjUploadDoc obj) {
		String messageId = uploadDocumentSaveUseCase.getMessageId();
		if (uploadDocumentSyncUseCase.validateAsynchronousProcess(uplDocPar.getListGet())) {
			return uploadDocumentSyncUseCase.asynchronousProcess(uplDocPar, messageId);
		} else {
			SqsMessageParamAllObject sqsMPAOb = SqsMessageParamAllObject.builder()
					.acquisition(uplDocPar.getAcquisition())
					.uploadDocumentApiResponse(callRestClient(uplDocPar, messageId))
					.sqsMessObjUploadDoc(obj).sqsMessage(uplDocPar.getSqsMessage())
					.build();

			if(sqsMPAOb.getUploadDocumentApiResponse() != null) {
				if (!uploadDocumentSaveUseCase.validateKofaxMessageId(
						sqsMPAOb.getUploadDocumentApiResponse().getData().getUploadDocumentTotal(), messageId)) {
					vinculationUpdateUseCase.markOperation(
							uplDocPar.getAcquisition().getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_PENDIENTE);
					uplDocExcepUseCase.validateException(ERROR_CODE_SESSION_ID_NOT_EQUAL, EMPTY, EMPTY, sqsMPAOb);
				}

				if (sqsMPAOb.getUploadDocumentApiResponse().getData().getProcessDocumentKofaxTotal() != null) {
					validProcessKofax(sqsMPAOb, uplDocPar);
				} else if (sqsMPAOb.getUploadDocumentApiResponse().getData().getUploadDocumentTotal()
						.getUploadDocumentErrorResponse() != null) {
					uplDocExcepUseCase.validateBackEndException(sqsMPAOb.getUploadDocumentApiResponse(), sqsMPAOb);
				}
				return sqsMPAOb.getUploadDocumentApiResponse().getData();
			}
			return null;
		}
	}

	@Override
	public UploadDocumentApiResponse callRestClient(
			UploadDocumentParameter uploadDocumentParameter, String messageId) {

		ProcessDocument processDocument = getRequest(
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

	@Override
	public ProcessDocument getRequest(SqsMessParameterGetRequest sqspgr) {
		return ProcessDocument.builder()
				.userCode(Numbers.ONE.getIntNumber())
				.processName(PROCESS_NAME_DIGITALIZATION)
				.processCode(
						sqspgr.getDocumentSubtype().equals(CEDULA_SUBTYPE) ? PROCESS_CODE_CEDULA : PROCESS_CODE_RUT)
				.files(sqspgr.getProcessDocumentFiles())
				.acquisition(
						AcquisitionProcessDocument.builder()
								.documentNumber(sqspgr.getAcquisition().getDocumentNumber())
								.id(sqspgr.getAcquisition().getId())
								.uploadDocumentRetries(sqspgr.getAcquisition().getUploadDocumentRetries())
								.uploadRutRetries(sqspgr.getAcquisition().getUploadRutRetries())
								.build())
				.messageId(sqspgr.getMessageId())
				.sqsMeta(sqspgr.getSqsMeta())
				.build();
	}

	private void validProcessKofax(
			SqsMessageParamAllObject sqsMessageParamAllObject, UploadDocumentParameter uplDocPar) {

		if (sqsMessageParamAllObject.getUploadDocumentApiResponse().getData().getProcessDocumentKofaxTotal()
				.getKofaxInformation() != null) {
			validateAndSaveInformation(sqsMessageParamAllObject.getUploadDocumentApiResponse(),
					sqsMessageParamAllObject, uplDocPar);
		} else {
			ProcessDocumentKofaxError procDocKofaxError = sqsMessageParamAllObject
					.getUploadDocumentApiResponse().getData()
					.getProcessDocumentKofaxTotal().getProcessDocumentKofaxError();
			sqsMessageParamAllObject.setUploadDocumentParameter(uplDocPar);
			uplDocExcepUseCase.validateBusinessException(procDocKofaxError, uplDocPar.getAcquisition(),
					sqsMessageParamAllObject);
		}

	}

	private void validateAndSaveInformation(UploadDocumentApiResponse uploadDocumentApiResponse,
			SqsMessageParamAllObject sqsMessageParamAllObject, UploadDocumentParameter uplDocPar) {

		KofaxInformation kofaxInformation = uploadDocumentApiResponse.getData().getProcessDocumentKofaxTotal()
				.getKofaxInformation();
		if (!uploadDocumentSaveUseCase.validateQualityFields(kofaxInformation)) {
			uploadDocumentValidateErrors.validateExceptionRetries(
					uplDocPar.getAcquisition(), TYPE_ID_CC, sqsMessageParamAllObject);
			/* AKI VA UN ERROR DE NEGOCIO */
		}

		String basInfGender = uploadDocumentSaveUseCase.transformKofaxGenderField(kofaxInformation.getGender());
		if (basInfGender.isEmpty()) {
			uploadDocumentValidateErrors.validateExceptionRetries(
					uplDocPar.getAcquisition(), TYPE_ID_CC, sqsMessageParamAllObject);
			/* AKI VA UN ERROR DE NEGOCIO */
		}

		uploadDocumentSaveUseCase.savePersonalInfo(
				kofaxInformation, uplDocPar.getAcquisition(), uplDocPar.getUsrTransaction());
		uploadDocumentSaveUseCase.saveBasicInfo(
				kofaxInformation, uplDocPar.getAcquisition(), basInfGender, uplDocPar.getUsrTransaction());

		if (uploadDocumentApiResponse.getData().getProcessDocumentKofaxTotal()
				.getProcessDocumentKofaxError() != null) {
			ProcessDocumentKofaxError processDocumentKofaxError = uploadDocumentApiResponse.getData()
					.getProcessDocumentKofaxTotal().getProcessDocumentKofaxError();
			sqsMessageParamAllObject.setUploadDocumentParameter(uplDocPar);
			uplDocExcepUseCase.validateBusinessException(
					processDocumentKofaxError, uplDocPar.getAcquisition(), sqsMessageParamAllObject);
		}
	}
}
