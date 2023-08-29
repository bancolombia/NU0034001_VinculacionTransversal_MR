package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.GeographicReply;
import co.com.bancolombia.model.geographiccatalog.GeographicCatalog;
import co.com.bancolombia.model.geographiccatalog.gateways.GeographicCatalogRepository;
import co.com.bancolombia.model.geographiccatalog.gateways.GeographicFindRabbitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;

@Component
@AllArgsConstructor
public class GeographicFindRabbit extends ErrorHandleRabbit implements GeographicFindRabbitRepository {

    private final GeographicCatalogRepository geographicCatalogRepository;

    @Override
    public GeographicReply findGeographicCatalog(GeographicQuery query) {
        try {
            validateMandatory(query);
            replaceEmpty(query);

            Optional<GeographicCatalog> catalog = Optional.empty();

            if (query.getCodeCity() != null && query.getCodeDepartment() == null && query.getCodeCountry() == null) {
                catalog = geographicCatalogRepository.findByCodeCity(query.getCodeCity());
            }

            if (query.getCodeDepartment() != null && query.getCodeCity() == null && query.getCodeCountry() == null) {
                catalog = geographicCatalogRepository.findByDepartment(query.getCodeDepartment());
            }

            if (query.getCodeCountry() != null && query.getCodeCity() == null && query.getCodeDepartment() == null) {
                catalog = geographicCatalogRepository.findByCountry(query.getCodeCountry());
            }

            if (query.getCodeCity() != null && query.getCodeCountry() != null && query.getCodeDepartment() == null) {
                catalog = geographicCatalogRepository.findByCodeCountryAndCodeCity(
                        query.getCodeCountry(), query.getCodeCity());
            }

            if (query.getCodeCity() != null && query.getCodeDepartment() != null && query.getCodeCountry() == null) {
                catalog = geographicCatalogRepository.findByCodeDepartmentAndCodeCity(
                        query.getCodeDepartment(), query.getCodeCity());
            }

            if (query.getCodeDepartment() != null && query.getCodeCountry() != null && query.getCodeCity() == null) {
                catalog = geographicCatalogRepository.findByCountryDepartment(
                        query.getCodeCountry(), query.getCodeDepartment());
            }

            if (query.getCodeCity() != null && query.getCodeDepartment() != null && query.getCodeCountry() != null) {
                catalog = geographicCatalogRepository.findByCodeCountryAndCodeDepartmentAndCodeCity(
                        query.getCodeCountry(), query.getCodeDepartment(), query.getCodeCity());
            }

            GeographicCatalog c = catalog.isPresent() ? catalog.get() : null;

            GeographicReply reply = GeographicReply.builder()
                    .valid(true)
                    .codeCountry(c == null ? EMPTY : c.getCodeCountry())
                    .nameCountry(c == null ? EMPTY : c.getNameCountry())
                    .codeDepartment(c == null ? EMPTY : c.getCodeDepartment())
                    .nameDepartment(c == null ? EMPTY : c.getNameDepartment())
                    .codeCity(c == null ? EMPTY : c.getCodeCity())
                    .nameCity(c == null ? EMPTY : c.getNameCity())
                    .active(c == null ? false : c.isActive())
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            GeographicReply reply = GeographicReply.builder()
                    .valid(false)
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(GeographicQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if ((query.getCodeCity() == null || query.getCodeCity().isEmpty())
                    && (query.getCodeDepartment() == null || query.getCodeDepartment().isEmpty())
                    && (query.getCodeCountry() == null || query.getCodeCountry().isEmpty())) {
                fieldList.add("codeCity");
                fieldList.add("codeDepartment");
                fieldList.add("codeCountry");
            }
        }
        errorMandatory(fieldList);
    }

    private void replaceEmpty(GeographicQuery query) {
        if (query.getCodeCity() != null && query.getCodeCity().isEmpty()) {
            query.setCodeCity(null);
        }

        if (query.getCodeDepartment() != null && query.getCodeDepartment().isEmpty()) {
            query.setCodeDepartment(null);
        }

        if (query.getCodeCountry() != null && query.getCodeCountry().isEmpty()) {
            query.setCodeCountry(null);
        }
    }
}
