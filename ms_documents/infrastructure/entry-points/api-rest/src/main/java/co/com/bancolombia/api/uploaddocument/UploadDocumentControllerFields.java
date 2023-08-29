package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationFileExtencion;
import co.com.bancolombia.commonsvnt.api.validations.ValidationInvalidInputList;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatoryInputList;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.common.exception.InvalidOptionalArgumentException;
import co.com.bancolombia.model.commons.OptionalMandatoryArguments;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FILE_EXTENCION_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANDATORY_INVALID_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANDATORY_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_MANDATORY;

@AllArgsConstructor
public class UploadDocumentControllerFields {

    public void validateOptionalList(UploadDocumentRequestData data) {
        List<InvalidOptionalArgumentException> listException = new ArrayList<>();
        List<Integer> index = new ArrayList<>();

        data.getFilesList().forEach(list -> {
            String compl = "[" + index.size() + "]";

            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    list, new Class[]{ValidationMandatoryInputList.class},
                    ERROR_CODE_MANDATORY_LIST, compl, EMPTY));
            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    list, new Class[]{ValidationInvalidInputList.class},
                    ERROR_CODE_MANDATORY_INVALID_LIST, compl, EMPTY));
            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    list, new Class[]{ValidationOptional.class},
                    ERROR_CODE_OPTIONAL_MANDATORY, compl, EMPTY));
            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    list, new Class[]{ValidationFileExtencion.class},
                    ERROR_CODE_FILE_EXTENCION_LIST, compl, EMPTY));

            index.add(1);
        });

        OptionalMandatoryArguments.validateException(listException);
    }
}
