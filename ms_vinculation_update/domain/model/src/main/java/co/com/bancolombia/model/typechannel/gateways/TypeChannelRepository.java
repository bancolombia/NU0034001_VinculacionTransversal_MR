package co.com.bancolombia.model.typechannel.gateways;

import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;

import java.util.Optional;

public interface TypeChannelRepository {

    public Optional<TypeChannel> findByCode(String code);
}
