package co.com.bancolombia.usecase.economicinformation;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CIIU;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_CIIU;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_OCCUPATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_POSITION_TRADE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_PROFESION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OCCUPATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.POSITION_TRADE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROFESSION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_SIN;

@RequiredArgsConstructor
public class ValidateCatalogsEconomicUseCase {

    private final UtilCatalogs utilCatalogs;

    public EmptyReply validateEconomicInfoCatalogs(EconomicInformation economicInformation) {
        List<CatalogQuery> listCatalog = new ArrayList<>();
        String profession = economicInformation.getProfession();
        if (utilCatalogs.valid(profession)){
            listCatalog.add(CatalogQuery.builder().code(economicInformation.getProfession())
                    .parents(PROFESSION).field(F_PROFESION).nameList(null).build());
        }
        String position = economicInformation.getPositionTrade();
        if (utilCatalogs.valid(position)){
            listCatalog.add(CatalogQuery.builder().code(economicInformation.getPositionTrade())
                    .parents(POSITION_TRADE).field(F_POSITION_TRADE).nameList(null).build());
        }
        String occupation = economicInformation.getOccupation();
        if (utilCatalogs.valid(occupation)){
            listCatalog.add(CatalogQuery.builder().code(economicInformation.getOccupation())
                    .parents(OCCUPATION).field(F_OCCUPATION).nameList(null).build());
        }
        return validateEconomicInfoCatalogsTwo(economicInformation, listCatalog);
    }

    public EmptyReply validateEconomicInfoCatalogsTwo(
            EconomicInformation economicInformation, List<CatalogQuery> listCatalog) {
        String ciiu = economicInformation.getCiiu();
        if (utilCatalogs.valid(ciiu)){
            listCatalog.add(CatalogQuery.builder().code(economicInformation.getCiiu())
                    .parents(CIIU).field(F_CIIU).nameList(null).build());
        }
        String currency = economicInformation.getCurrency();
        if (utilCatalogs.valid(currency)){
            listCatalog.add(CatalogQuery.builder().code(economicInformation.getCurrency())
                    .parents(CURRENCY).field(F_CURRENCY).nameList(null).build());
        }
        String rut = economicInformation.getRut();
        if (utilCatalogs.valid(rut)){
            listCatalog.add(CatalogQuery.builder().code(economicInformation.getRut())
                    .parents(RUT).field(F_RUT).nameList(null).build());
        }
        return utilCatalogs.callValidateCatalog(listCatalog, null,
                ERROR_CODE_CATALOG_SIN, ERROR_CODE_CATALOG_SIN);
    }
}