package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;

import java.util.UUID;

public interface AcquisitionUseCase {
    /**
     * this function searches for the existence of the acquisition
     *
     * @param acquisitionStartObjectModel
     * @return Acquisition
     */
    Acquisition findAcquisition(AcquisitionStartObjectModel acquisitionStartObjectModel);

    /**
     * this function the matrix is assigned to the acquisition sent
     *
     * @param acquisition
     */
    void setAcquisitionMatrices(Acquisition acquisition);

    /**
     * this function obtains the acquisition by its id
     *
     * @param idAcquisition
     * @return Acquisition
     */
    Acquisition findById(UUID idAcquisition);

    /**
     * Returns an Acquisition if exists, otherwise throws an
     * AcquisitionNotFoundException
     * @param idAcquisition
     * @return Acquisition
     */
    Acquisition findAndValidateById(UUID idAcquisition);

    /**
     * Returns an Acquisition if exists for id, document and document number, otherwise throws
     * @param id
     * @param documentType
     * @param documentNumber
     * @return Acquisition
     */
    Acquisition findByIdAndDocumentTypeAndDocumentNumber(UUID id, String documentType, String documentNumber);

    /**
     * Returns the first Acquisition if exists for document and document number, otherwise throws
     * @param documentType
     * @param documentNumber
     * @return Acquisition
     */
    Acquisition findByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);

    /**
     * Returns an Acquisition if exists, otherwise throws
     * @param idAcquisition
     * @return Acquisition
     */
    Acquisition findByIdWitOutState(UUID idAcquisition);
}
