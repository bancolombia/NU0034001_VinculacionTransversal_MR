package co.com.bancolombia.typechannel;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import co.com.bancolombia.model.typechannel.gateways.TypeChannelRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CHANNEL_TYPE;

@RequiredArgsConstructor
public class TypeChannelUseCaseImpl implements TypeChannelUseCase {

    private final TypeChannelRepository typeChannelRepository;

    public Optional<TypeChannel> findByCode(String code) {
        return this.typeChannelRepository.findByCode(code);
    }

    @Override
    public List<ErrorField> validate(String code, Optional<TypeChannel> oTyCha) {
        List<ErrorField> error = new ArrayList<>();
        if (!oTyCha.isPresent()) {
            error.add(ErrorField.builder().name(code).complement(CHANNEL_TYPE).build());
        }
        return error;
    }

    @Override
    public List<ErrorField> validateActive(Optional<TypeChannel> oTyCha){
        List<ErrorField> error = new ArrayList<>();
        if (oTyCha.isPresent() && !oTyCha.get().isActive()) {
            error.add(ErrorField.builder().name(oTyCha.get().getCode()).complement(CHANNEL_TYPE).build());
        }
        return error;
    }
}