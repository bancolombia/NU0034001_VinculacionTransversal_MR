package co.com.bancolombia.model.validateidentityrules.gateways;

import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.model.validateidentityrules.ValidateIdentityRules;

import java.util.List;
import java.util.Optional;

public interface ValidateIdentityRulesRepository {

    public Optional<ValidateIdentityRules> findByRules(String rule);

    public List<ValidateIdentityRules> findRulesActive();

    public List<ValidateIdentityRules> findByTypeAcquisitionRulesActive(TypeAcquisition typeAcquisition);
    
    public List<ValidateIdentityRules> findByTypeAcquisition(TypeAcquisition typeAcquisition);
}
