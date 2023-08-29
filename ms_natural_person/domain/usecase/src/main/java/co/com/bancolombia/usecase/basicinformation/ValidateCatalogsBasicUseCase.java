package co.com.bancolombia.usecase.basicinformation;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_CIVIL_STATUS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_CONTRACT_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_EDUCATION_LEVEL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_GENDER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_HOUSING_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_NATIONALITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_PEP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_SOCIAL_STRATUM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENDER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.HOUSING_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NATIONALITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RESPONSE_SN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SOCIAL_STRATUM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_SIN;

@RequiredArgsConstructor
public class ValidateCatalogsBasicUseCase {

    private final UtilCatalogs utilCatalogs;

    public EmptyReply validateBasicInfoCatalogs(BasicInformation basicInformation) {
        List<CatalogQuery> listCatalog = new ArrayList<>();
        String gender = basicInformation.getGender();
        if (utilCatalogs.valid(gender)){
            listCatalog.add(CatalogQuery.builder().code(gender).parents(GENDER).field(F_GENDER).build());
        }
        String civil = basicInformation.getCivilStatus();
        if (utilCatalogs.valid(civil)){
            listCatalog.add(CatalogQuery.builder().code(civil).parents(Constants.CIVIL_STATUS).field(F_CIVIL_STATUS)
                    .build());
        }
        String education = basicInformation.getEducationLevel();
        if (utilCatalogs.valid(education)){
            listCatalog.add(CatalogQuery.builder().code(education)
                .parents(Constants.EDUCATION_LEVEL).field(F_EDUCATION_LEVEL).build());
        }
        String socialStratum = basicInformation.getSocialStratum();
        if (utilCatalogs.valid(socialStratum)){
            listCatalog.add(CatalogQuery.builder().code(socialStratum)
                .parents(SOCIAL_STRATUM).field(F_SOCIAL_STRATUM).build());
        }
        String housingType = basicInformation.getHousingType();
        if (utilCatalogs.valid(housingType)){
            listCatalog.add(CatalogQuery.builder().code(housingType)
                .parents(HOUSING_TYPE).field(F_HOUSING_TYPE).build());
        }
        List<GeographicQuery> listGeographic = validateBasicInfoCatalogs2(listCatalog, basicInformation);
        return utilCatalogs.callValidateCatalog(
                listCatalog, listGeographic, ERROR_CODE_CATALOG_SIN, ERROR_CODE_CATALOG_SIN);
    }

    public List<GeographicQuery> validateBasicInfoCatalogs2(
            List<CatalogQuery> listCatalog, BasicInformation basicInformation){
        String contractType = basicInformation.getContractType();
        if (utilCatalogs.valid(contractType)){
            listCatalog.add(CatalogQuery.builder().code(contractType)
                    .parents(Constants.CONTRACT_TYPE).field(F_CONTRACT_TYPE).build());
        }
        String pep = basicInformation.getPep();
        if (utilCatalogs.valid(pep)){
            listCatalog.add(CatalogQuery.builder().code(pep)
                    .parents(RESPONSE_SN).field(F_PEP).build());
        }
        String nationality = basicInformation.getNationality();
        if (utilCatalogs.valid(nationality)){
            listCatalog.add(CatalogQuery.builder().code(nationality)
                    .parents(NATIONALITY).field(F_NATIONALITY).build());
        }
        List<GeographicQuery> listGeographic = new ArrayList<>();
        String country = basicInformation.getCountry();
        String city = basicInformation.getBirthCity();
        if (utilCatalogs.valid(country) && utilCatalogs.valid(city)){
            listGeographic.add(GeographicQuery.builder()
                    .codeCountry(country).nameCountry(Constants.COUNTRY)
                    .codeDepartment(basicInformation.getBirthDepartment())
                    .nameDepartment(Constants.BIRTH_DEPARTMENT).codeCity(city).nameCity(Constants.BIRTH_CITY)
                    .nameList(EMPTY).build());
        }
        return listGeographic;
    }
}