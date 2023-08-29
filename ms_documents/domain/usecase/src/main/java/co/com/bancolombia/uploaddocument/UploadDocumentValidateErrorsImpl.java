package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_RECHAZADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_UPLOAD_DOCUMENT_RETRIES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_UPLOAD_RUT_RETRIES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_RUT_RETRIES_FLAG_EMISSION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_RUT_RETRIES_FLAG_PERSONAL_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_EXPIRED_RUT_EMISSION_DATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_INCONSISTENCIES_PERSONAL_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PROCESS_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY;

@RequiredArgsConstructor
public class UploadDocumentValidateErrorsImpl implements UploadDocumentValidateErrors {

	private final VinculationUpdateUseCase vinculationUpdateUseCase;
	private final ParametersUseCase parametersUseCase;
	private final UploadDocumentExcepUseCase uplDocExcepUseCase;
	private final DocumentRetriesUseCase documentRetriesUseCase;
	
	@Override
	public void validateExceptionRetries(
			Acquisition acquisition, String detail, SqsMessageParamAllObject sqsMessageParamAllObject) {

		String parameterRetries = getParameter(CODE_UPLOAD_DOCUMENT_RETRIES, PARENT_UPLOAD_DOCUMENT);
		Integer retriesParameter = Integer.parseInt(parameterRetries);

		if (acquisition.getUploadDocumentRetries() < retriesParameter) {
			Integer retries = acquisition.getUploadDocumentRetries() + 1;
			acquisition.setUploadDocumentRetries(retries);

			documentRetriesUseCase.save(acquisition);
			vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.ONE.getNumber());
			vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_RECHAZADO);
			uplDocExcepUseCase.validateException(
					ERROR_CODE_UPLOAD_DOCUMENT_RETRY, detail, EMPTY, sqsMessageParamAllObject);
		} else {
			vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_RECHAZADO);
			vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.TWO.getNumber());
			uplDocExcepUseCase.validateException(ERROR_CODE_PROCESS_DOCUMENT, detail, EMPTY, sqsMessageParamAllObject);
		}
	}
	
	@Override
	public void validateExceptionRutRetries(
			Acquisition acquisition, String detail, String flag, SqsMessageParamAllObject sqsMessageParamAllObject) {

		if (flag.equals(VALIDATE_RUT_RETRIES_FLAG_EMISSION)) {
			vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_RECHAZADO);
			vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.TWO.getNumber());
			uplDocExcepUseCase.validateException(
					ERROR_CODE_EXPIRED_RUT_EMISSION_DATE, detail, EMPTY, sqsMessageParamAllObject);
		} else {
			validateRetriesOrPersonalInfoFlag(acquisition, detail, flag, sqsMessageParamAllObject);
		}
	}

	private void validateRetriesOrPersonalInfoFlag(
			Acquisition acquisition, String detail, String flag, SqsMessageParamAllObject sqsMessageParamAllObject) {

		String parameterRetries = getParameter(CODE_UPLOAD_RUT_RETRIES, PARENT_UPLOAD_DOCUMENT);
		Integer retriesParameter = Integer.parseInt(parameterRetries);

		if (acquisition.getUploadRutRetries() < retriesParameter) {
			Integer retries = acquisition.getUploadRutRetries() + 1;
			acquisition.setUploadRutRetries(retries);

			documentRetriesUseCase.save(acquisition);
			vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.ONE.getNumber());
			vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_RECHAZADO);
			uplDocExcepUseCase.validateException(
					ERROR_CODE_UPLOAD_DOCUMENT_RETRY, detail, EMPTY, sqsMessageParamAllObject);
		} else {
			vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_RECHAZADO);
			vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.TWO.getNumber());

			if (flag.equals(VALIDATE_RUT_RETRIES_FLAG_PERSONAL_INFO)) {
				uplDocExcepUseCase.validateException(
						ERROR_CODE_INCONSISTENCIES_PERSONAL_INFO, detail, EMPTY, sqsMessageParamAllObject);
			} else {
				uplDocExcepUseCase.validateException(
						ERROR_CODE_PROCESS_DOCUMENT, detail, EMPTY, sqsMessageParamAllObject);
			}
		}
	}

	private String getParameter(String name, String parent) {
		Optional<Parameters> parameter = parametersUseCase.findByNameAndParent(name, parent);
		return parameter.isPresent() ? parameter.get().getCode() : "0";
	}
}
