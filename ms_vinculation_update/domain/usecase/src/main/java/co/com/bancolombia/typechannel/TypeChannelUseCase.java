package co.com.bancolombia.typechannel;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;

import java.util.List;
import java.util.Optional;

public interface TypeChannelUseCase {

    /**
     * This function searches the channel by its code
     *
     * @param code
     * @return Optional<TypeChannel>
     */
    public Optional<TypeChannel> findByCode(String code);

    /**
     * This function validates that the code exists
     *
     * @param code,oTyCha
     * @return List<ErrorField>
     */
    public List<ErrorField> validate(String code, Optional<TypeChannel> oTyCha);

    /**
     * This function validates that the code is active
     *
     * @param oTyCha
     * @return List<ErrorField>
     */
    public List<ErrorField> validateActive(Optional<TypeChannel> oTyCha);
}
