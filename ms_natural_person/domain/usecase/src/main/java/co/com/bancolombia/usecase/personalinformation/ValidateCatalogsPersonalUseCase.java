package co.com.bancolombia.usecase.personalinformation;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EXPEDITION_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EXPEDITION_DEPARTMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EXPEDITION_PLACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_SIN;

@RequiredArgsConstructor
public class ValidateCatalogsPersonalUseCase {

    private final UtilCatalogs utilCatalogs;

    public EmptyReply validatePersonalInfoCatalogs(PersonalInformation personalInformation) {
        GeographicQuery geographic1 = GeographicQuery.builder()
                .codeCountry(personalInformation.getExpeditionCountry()).nameCountry(EXPEDITION_COUNTRY)
                .codeDepartment(personalInformation.getExpeditionDepartment()).nameDepartment(EXPEDITION_DEPARTMENT)
                .codeCity(personalInformation.getExpeditionPlace()).nameCity(EXPEDITION_PLACE).build();
        List<GeographicQuery> listGeographic = Collections.singletonList(geographic1);
        return utilCatalogs.callValidateCatalog(
                null, listGeographic, null, ERROR_CODE_CATALOG_SIN);
    }
}