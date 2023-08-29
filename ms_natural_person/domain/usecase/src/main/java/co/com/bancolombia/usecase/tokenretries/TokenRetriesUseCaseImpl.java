package co.com.bancolombia.usecase.tokenretries;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.model.tokenretries.gateways.TokenRetriesRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_G_TOKEN_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ZERO;

@RequiredArgsConstructor
public class TokenRetriesUseCaseImpl implements TokenRetriesUseCase{

    private final TokenRetriesRepository tokenRetriesRepository;
    private final CoreFunctionDate coreFunctionDate;
    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_INFORMATION, OPER_G_TOKEN_OPERATION);

    @Override
    public Optional<TokenRetries> findByAcquisition(Acquisition acquisition) {
        return Optional.ofNullable(tokenRetriesRepository.findByAcquisition(acquisition));
    }

    @Override
    public TokenRetries save(TokenRetries tokenRetries) {
        return this.tokenRetriesRepository.save(tokenRetries);
    }

    @Override
    public TokenRetries initTokenRetries (Acquisition acquisition, String userTransaction) {
        Optional<TokenRetries> tokenRetries = Optional.ofNullable(tokenRetriesRepository
                .findByAcquisition(acquisition));

        if(!tokenRetries.isPresent()) {
            TokenRetries initToken = TokenRetries.builder().acquisition(acquisition)
                    .generateTokenRetries(ZERO).validateTokenRetries(ZERO)
                    .createdBy(userTransaction).createdDate(coreFunctionDate.getDatetime()).build();

            return this.save(initToken);
        } else {
            return tokenRetries.get();
        }
    }
}
