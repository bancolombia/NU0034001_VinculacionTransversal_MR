package co.com.bancolombia.model.acquisition.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcquisitionRepository {
    public Acquisition save(Acquisition acquisition);

    public List<Acquisition> findAll();

    public List<Acquisition> findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    public List<Acquisition> findByIdAndDocumentTypeAndDocumentNumber(UUID id, DocumentType documentType,
        String documentNumber);

    public Optional<Acquisition> findAcquisition(AcquisitionStartObjectModel acquisitionStartObjectModel);

    public Acquisition findById(UUID id);

    long countAcquisitionByState(String state, List<UUID> acquisitionId);
}
