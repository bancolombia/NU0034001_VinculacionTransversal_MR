package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ADDRESS_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.BRAND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DEPARTMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_ADDRESS_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_BRAND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_LIST_CONTACT;

@RequiredArgsConstructor
public class ValidateCatalogsContactUseCase {

    private final UtilCatalogs utilCatalogs;

    public EmptyReply validateContactInfoCatalogs(List<ContactInformation> contactInformationList) {
        List<CatalogQuery> listCatalog = new ArrayList<>();
        List<GeographicQuery> listGeographic = new ArrayList<>();
        for (ContactInformation cI : contactInformationList) {
            String code1 = cI.getAddressType();
            if (code1!=null){
                CatalogQuery catalog1 = CatalogQuery.builder().code(cI.getAddressType()).parents(ADDRESS_TYPE)
                        .field(F_ADDRESS_TYPE).nameList(cI.getAddressType()).build();
                listCatalog.add(catalog1);
            }
            String code2 = cI.getBrand();
            if (code2!=null){
                CatalogQuery catalog2 = CatalogQuery.builder()
                        .code(cI.getBrand()).parents(BRAND).field(F_BRAND).nameList(cI.getAddressType()).build();
                listCatalog.add(catalog2);
            }

            GeographicQuery geographic = GeographicQuery.builder()
                    .codeCity(cI.getCity()).nameCity(CITY).codeDepartment(cI.getDepartment()).nameDepartment(DEPARTMENT)
                    .codeCountry(cI.getCountry()).nameCountry(COUNTRY).nameList(cI.getAddressType()).build();
            listGeographic.add(geographic);
        }
        return utilCatalogs.callValidateCatalog(
                listCatalog, listGeographic, ERROR_CODE_CATALOG_LIST_CONTACT, ERROR_CODE_CATALOG_LIST_CONTACT);
    }
}