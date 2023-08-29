package co.com.bancolombia.util.catalog;

import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;

public interface CatalogUtilUseCase {

    public void validateCatalogs(AcquisitionRequestModel acquisitionRequestModel);

    public AcquisitionStartObjectModel transformObjectCatalog(AcquisitionRequestModel acquisitionRequestModel);
}
