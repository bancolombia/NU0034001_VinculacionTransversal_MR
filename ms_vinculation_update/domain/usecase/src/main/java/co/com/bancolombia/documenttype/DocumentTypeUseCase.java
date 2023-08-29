package co.com.bancolombia.documenttype;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;

import java.util.List;
import java.util.Optional;

public interface DocumentTypeUseCase {

    /**
     * This function searches the document type by its code
     *
     * @param code
     * @return Optional<DocumentType>
     */
    public Optional<DocumentType> findByCode(String code);

    /**
     * This function validates that the code exists
     *
     * @param code,oTyDoc
     * @return List<ErrorField>
     */
    public List<ErrorField> validate(String code, Optional<DocumentType> oTyDoc);

    /**
     * This function validates that the code is active
     *
     * @param oTyDoc
     * @return List<ErrorField>
     */
    public List<ErrorField> validateActive(Optional<DocumentType> oTyDoc);
}
