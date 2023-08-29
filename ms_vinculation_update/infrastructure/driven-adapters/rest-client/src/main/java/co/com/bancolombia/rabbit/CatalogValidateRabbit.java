package co.com.bancolombia.rabbit;

import co.com.bancolombia.catalog.CatalogUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ValidateCatalogQuery;
import co.com.bancolombia.geographiccatalog.GeographicCatalogUseCase;
import co.com.bancolombia.model.catalog.gateways.CatalogValidateRabbitRepository;
import co.com.bancolombia.model.geographiccatalog.GeographicCatalog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class CatalogValidateRabbit extends ErrorHandleRabbit implements CatalogValidateRabbitRepository {

    private final CatalogUseCase catalogUseCase;
    private final GeographicCatalogUseCase geographicCatalogUseCase;

    @Override
    public EmptyReply validateCatalog(ValidateCatalogQuery query) {
        try {
            validateMandatory(query);

            HashMap<String, List<ErrorField>> errors = new HashMap<>();

            if (query.getCatalog() != null) {
                query.getCatalog().keySet().forEach(key -> {
                    List<ErrorField> errorFields = new ArrayList<>();
                    List<CatalogQuery> catalogList = query.getCatalog().get(key);
                    catalogList.forEach(c -> errorFields.addAll(
                            catalogUseCase.validate(c.getCode(), c.getParents(), c.getField(), c.getNameList()))
                    );
                    if (!errorFields.isEmpty()) {
                        addErrorList(errors, errorFields, key);
                    }
                });
            }

            if (query.getGeographic() != null) {
                query.getGeographic().keySet().forEach(key -> {
                    List<ErrorField> errorFields = new ArrayList<>();
                    List<GeographicQuery> geographicList = query.getGeographic().get(key);
                    geographicList.forEach(g -> {
                        if (g.getCodeDepartment() == null) {
                            errorFields.addAll(geographicCatalogUseCase.validateCountryCity(
                                    g.getCodeCountry(), g.getCodeCity(), g.getNameCountry(), g.getNameCity()));
                        } else {
                            GeographicCatalog gc = GeographicCatalog.builder()
                                    .codeCity(g.getCodeCity()).nameCity(g.getNameCity())
                                    .codeDepartment(g.getCodeDepartment()).nameDepartment(g.getNameDepartment())
                                    .codeCountry(g.getCodeCountry()).nameCountry(g.getNameCountry())
                                    .build();

                            errorFields.addAll(
                                    geographicCatalogUseCase.validateCityDepartmentCountry(g.getNameList(), gc));
                        }
                    });
                    if (!errorFields.isEmpty()) {
                        addErrorList(errors, errorFields, key);
                    }
                });
            }

            EmptyReply reply = EmptyReply.builder()
                    .valid(errors.isEmpty())
                    .errorList(errors.isEmpty() ? null : errors)
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            EmptyReply reply = EmptyReply.builder()
                    .valid(false)
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(ValidateCatalogQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getCatalog() != null) {
                query.getCatalog().keySet().forEach(key -> {
                    List<CatalogQuery> catalog = query.getCatalog().get(key);
                    catalog.forEach(c -> {
                        if (c.getCode() == null || c.getCode().isEmpty()) {
                            String field = "catalog." + key + ".code[" + catalog.indexOf(c) + "]";
                            fieldList.add(field);
                        }
                        if (c.getParents() == null || c.getParents().isEmpty()) {
                            String field = "catalog." + key + ".parents[" + catalog.indexOf(c) + "]";
                            fieldList.add(field);
                        }
                    });
                });
            }

            if (query.getGeographic() != null) {
                query.getGeographic().keySet().forEach(key -> {
                    List<GeographicQuery> geographic = query.getGeographic().get(key);
                    geographic.forEach(c -> {
                        if (c.getCodeCountry() == null || c.getCodeCountry().isEmpty()) {
                            String field = "geographic." + key + ".countryCode[" + geographic.indexOf(c) + "]";
                            fieldList.add(field);
                        }
                        if (c.getCodeCity() == null || c.getCodeCity().isEmpty()) {
                            String field = "geographic." + key + ".cityCode[" + geographic.indexOf(c) + "]";
                            fieldList.add(field);
                        }
                    });
                });
            }
        }
        errorMandatory(fieldList);
    }

    private void addErrorList(HashMap<String, List<ErrorField>> errors, List<ErrorField> errorFields, String key) {
        if (errors.get(key) != null) {
            List<ErrorField> ef = errors.get(key);
            ef.addAll(errorFields);
            errors.put(key, ef);
        } else {
            errors.put(key, errorFields);
        }
    }
}
