package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcquisitionValidateUseCase {
    /**
     * This function validates the acquisition information and if everything is correct, it returns the acquisition
     *
     * @param idAcq
     * @param documentType
     * @param documentNumber
     * @param operation
     * @return Optional<Acquisition>
     */
    public Optional<Acquisition> validateInfoSearchAndGet(
            String idAcq, String documentType, String documentNumber, String operation);

    /**
     * This function validates the validity of the acquisition according to the time set by the business
     *
     * @param acquisition
     * @return String
     */
    public String validityAcquisition(Acquisition acquisition);

    /**
     * This function performs the search for acquisitions according to the type of document and document number
     *
     * @param documentType
     * @param documentNumber
     * @return List<Acquisition>
     */
    public List<Acquisition> findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    /**
     * This function performs the search for the acquisition and makes its respective validations of these
     *
     * @param acqId
     * @param docType
     * @param docNumber
     * @param operation
     * @return List<Acquisition>
     */
    public List<Acquisition> getAllByOpAcqIdDocTypeAndDocNum(
            UUID acqId, String docType, String docNumber, String operation);
}
