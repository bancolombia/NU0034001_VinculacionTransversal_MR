package co.com.bancolombia.model.containeraction.gateways;

import co.com.bancolombia.commonsvnt.model.containeraction.ContainerAction;

import java.util.Optional;

public interface ContainerActionRepository {
    Optional<ContainerAction> findByCode(String code);
    ContainerAction save(ContainerAction componentAction);
}
