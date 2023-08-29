package co.com.bancolombia.commonsvnt.api.model.util;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MyDataInitial {
    private String documentType;
    private String documentNumber;
    private String acquisitionId;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getAcquisitionId() {
        return acquisitionId;
    }

    public void setAcquisitionId(String acquisitionId) {
        this.acquisitionId = acquisitionId;
    }

    

}
