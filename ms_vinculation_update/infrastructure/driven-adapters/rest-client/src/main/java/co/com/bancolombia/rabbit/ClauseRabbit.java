package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.clause.Clause;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ClauseQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ClauseReply;
import co.com.bancolombia.model.clause.gateways.ClauseRabbitRabbitRepository;
import co.com.bancolombia.model.clause.gateways.ClauseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_CLAUSE_CODE_NOT_EXISTS;

@Component
@AllArgsConstructor
public class ClauseRabbit extends ErrorHandleRabbit implements ClauseRabbitRabbitRepository {

    private ClauseRepository clauseRepository;

    public ClauseReply getClause(ClauseQuery query) {
        try {
            validateMandatory(query);
            Optional<Clause> clause = clauseRepository.findByCode(query.getClauseCode());
            if (!clause.isPresent()) {
               String message = ERROR_MSG_CLAUSE_CODE_NOT_EXISTS + SPACE + query.getClauseCode();
                throwExceptionRabbit(message);
            }
            Clause c = clause.get();
            ClauseReply reply = ClauseReply.builder().valid(true).name(c.getName()).createdDate(c.getCreatedDate())
                    .build();
            return reply;
        } catch (ValidationException | CustomException ex) {
            ClauseReply reply = ClauseReply.builder().valid(false).errorList(getErrorFromException(ex)).build();
            return reply;
        }
    }

    public void validateMandatory(ClauseQuery query) {
        List<String> fieldList = new ArrayList<>();
        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getClauseCode() == null || query.getClauseCode().isEmpty()) {
                fieldList.add("clauseCode");
            }
        }
        errorMandatory(fieldList);
    }
}


