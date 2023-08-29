package co.com.bancolombia.rabbit;

import co.com.bancolombia.catalog.CatalogUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.model.catalog.Catalog;
import co.com.bancolombia.model.catalog.gateways.CatalogFindRabbitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;

@Component
@AllArgsConstructor
public class CatalogFindRabbit extends ErrorHandleRabbit implements CatalogFindRabbitRepository {

    private final CatalogUseCase catalogUseCase;

    @Override
    public CatalogReply findCatalogCodeAndParent(CatalogQuery query) {
        try {
            validateMandatory(query);

            Optional<Catalog> catalog = catalogUseCase.findByCodeAndParent(query.getCode(), query.getParents());
            Catalog c = catalog.isPresent() ? catalog.get() : null;

            CatalogReply reply = CatalogReply.builder()
                    .valid(true)
                    .code(c == null ? EMPTY : c.getCode())
                    .name(c == null ? EMPTY : c.getName())
                    .parent(c == null ? EMPTY : c.getParent())
                    .active(c == null ? false : c.isActive())
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            CatalogReply reply = CatalogReply.builder()
                    .valid(false)
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(CatalogQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getCode() == null || query.getCode().isEmpty()) {
                fieldList.add("code");
            }
            if (query.getParents() == null || query.getParents().isEmpty()) {
                fieldList.add("parents");
            }
        }
        errorMandatory(fieldList);
    }
}
