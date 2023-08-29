package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;

public interface GeneratePdfUtilOneUseCase {
    String getCatalogName(String parent, String code);

    String getCatalogCode(String parent, String code);

    String getGeographicName(String code, String division, int type);

    CheckList checkListFindByOperation(Acquisition acquisition, String operation);
}
