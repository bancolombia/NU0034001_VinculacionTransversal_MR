package co.com.bancolombia.model.signdocument.gateways;

import co.com.bancolombia.model.signdocument.TxtConstruction;

import java.util.List;

public interface TxtConstructionRepository {

    List<TxtConstruction> findByAllIteration(String iteration);
}
