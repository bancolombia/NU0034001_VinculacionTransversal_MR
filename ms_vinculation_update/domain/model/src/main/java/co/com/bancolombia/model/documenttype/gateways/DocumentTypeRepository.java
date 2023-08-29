package co.com.bancolombia.model.documenttype.gateways;

import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;

import java.util.Optional;

public interface DocumentTypeRepository {

    public Optional<DocumentType> findByCode(String code);
}
