package co.com.bancolombia.usecase.util.rabbit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class DependentFieldSearchInput {
    private UUID idAcq;
    private String table;
    private String searchField;
    private String searchValue;
    private boolean active;
}
