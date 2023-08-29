package co.com.bancolombia.model.foreigninformationcurrency.gateways;

import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;

import java.util.List;
import java.util.Optional;

public interface ForeignInformationCurrencyRepository {
    List<ForeignInformationCurrency> findByForeignInformation(ForeignInformation information);
    Optional<ForeignInformationCurrency> findByForeignInformationAndForeignCurrencyTransactionType(
            ForeignInformation information, String foreignCurrencyTransactionType);
    ForeignInformationCurrency save(ForeignInformationCurrency instruction);
    List<ForeignInformationCurrency> saveAll(List<ForeignInformationCurrency> instruction);
}