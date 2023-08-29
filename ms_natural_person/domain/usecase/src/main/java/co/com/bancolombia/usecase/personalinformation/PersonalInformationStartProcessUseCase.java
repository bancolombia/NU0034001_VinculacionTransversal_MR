package co.com.bancolombia.usecase.personalinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.personalinformation.PersonalInformation;

import java.util.Date;
import java.util.List;

public interface PersonalInformationStartProcessUseCase {


    /**
     * Saves the personal information.
     * If the record exists in DB, then a merge between the new and the old one is made.
     *
     * @param personalInformation PersonalInformation object
     * @return The saved object
     */
    PersonalInformation startProcessPersonalInformation(PersonalInformation personalInformation);

    /**
     * This function validates that the completed date is less than today
     *
     * @param dataDate
     * @param string
     */
    List<ErrorField> validationDate(Date dataDate, String string);

}