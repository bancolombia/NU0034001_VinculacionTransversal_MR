package co.com.bancolombia.bussinesline;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;

import java.util.List;
import java.util.Optional;

public interface BusinessLineUseCase {

    /**
     * This function searches the business line by its code
     *
     * @param code
     * @return Optional<BusinessLine>
     */
    public Optional<BusinessLine> findByCode(String code);

    /**
     * This function validates that the code exists
     *
     * @param code,oBuLin
     * @return List<ErrorField>
     */
    public List<ErrorField> validate(String code, Optional<BusinessLine> oBuLin);

    /**
     * This function validates that the code is active
     *
     * @param oBuLin
     * @return List<ErrorField>
     */
    public List<ErrorField> validateActive(Optional<BusinessLine> oBuLin);
}
