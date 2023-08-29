package co.com.bancolombia.model.businessline.gateways;

import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;

import java.util.Optional;

public interface BusinessLineRepository {

    public Optional<BusinessLine> findByCode(String code);
}
