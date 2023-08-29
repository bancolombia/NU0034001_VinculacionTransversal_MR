package co.com.bancolombia.bussinesline;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.model.businessline.gateways.BusinessLineRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.BUSINESS_LINE;

@RequiredArgsConstructor
public class BusinessLineUseCaseImpl implements BusinessLineUseCase {

    private final BusinessLineRepository businessLineRepository;

    @Override
    public Optional<BusinessLine> findByCode(String code) {
        return this.businessLineRepository.findByCode(code);
    }

    @Override
    public List<ErrorField> validate(String code, Optional<BusinessLine> oBuLin) {
        List<ErrorField> error = new ArrayList<>();
        if (!oBuLin.isPresent()) {
            error.add(ErrorField.builder().name(code).complement(BUSINESS_LINE).build());
        }
        return error;
    }

    @Override
    public List<ErrorField> validateActive(Optional<BusinessLine> oBuLin) {
        List<ErrorField> error = new ArrayList<>();
        if (oBuLin.isPresent() && !oBuLin.get().isActive()) {
            error.add(ErrorField.builder().name(oBuLin.get().getCode()).complement(BUSINESS_LINE).build());
        }
        return error;
    }
}
