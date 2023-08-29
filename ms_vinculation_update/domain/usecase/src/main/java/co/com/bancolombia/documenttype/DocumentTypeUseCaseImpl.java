package co.com.bancolombia.documenttype;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.model.documenttype.gateways.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_TYPE;

@RequiredArgsConstructor
public class DocumentTypeUseCaseImpl implements DocumentTypeUseCase {

    private final DocumentTypeRepository documentTypeRepository;

    public Optional<DocumentType> findByCode(String code) {
        return documentTypeRepository.findByCode(code);
    }

    @Override
    public List<ErrorField> validate(String code, Optional<DocumentType> oTyDoc) {
        List<ErrorField> error = new ArrayList<>();
        if (!oTyDoc.isPresent()) {
            error.add(ErrorField.builder().name(code).complement(DOCUMENT_TYPE).build());
        }
        return error;
    }

    @Override
    public List<ErrorField> validateActive(Optional<DocumentType> oTyDoc) {
        List<ErrorField> error = new ArrayList<>();
        if (oTyDoc.isPresent() && !oTyDoc.get().isActive()) {
            error.add(ErrorField.builder().name(oTyDoc.get().getCode()).complement(DOCUMENT_TYPE).build());
        }
        return error;
    }
}