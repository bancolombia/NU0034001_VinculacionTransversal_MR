package co.com.bancolombia.model.clause.gateways;

import co.com.bancolombia.commonsvnt.model.clause.Clause;

import java.util.Optional;

public interface ClauseRepository {
	public Optional<Clause> findByCode(String code);
	public Clause save(Clause clause);

}
