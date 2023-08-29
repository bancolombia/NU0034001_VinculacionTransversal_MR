package co.com.bancolombia.usecase.util;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class UtilCatalogs {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;

    public EmptyReply callValidateCatalog(
            List<CatalogQuery> listCatalog, List<GeographicQuery> listGeographic,
            String codeErrorCatalog, String codeErrorGeo){
        Map<String, List<CatalogQuery>> catalog = null;
        Map<String, List<GeographicQuery>> catalogGeo = null;
        if (listCatalog!=null){
            catalog = new HashMap<>();
            catalog.put(codeErrorCatalog, listCatalog);
        }
        if (listGeographic!=null){
            catalogGeo = new HashMap<>();
            catalogGeo.put(codeErrorGeo, listGeographic);
        }
        return vinculationUpdateUseCase.validateCatalog(catalog, catalogGeo);
    }

    public boolean valid(String value){
        return value != null && !value.isEmpty();
    }
}