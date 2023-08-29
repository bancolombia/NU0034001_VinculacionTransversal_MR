package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.usecase.contactinformation.generic.ArrayErrors;

import java.util.List;
import java.util.Optional;

public interface ContactInformationUseCase {
    /**
     * This function creates the contact information.
     * The acquisition is validated with the given id, but before calling this function, the docType parameters,
     * docNumber and acqId must be validated with AcquisitionUseCase.validateInfoSearchAndGet.
     *
     * @param contactInformation
     */
    ContactInformation save(ContactInformation contactInformation);

    List<ContactInformation> save(List<ContactInformation> contactInformationList);

    ContactInformation constructContactInformation(ContactInformation cI);

    String concatErrorTypeDirection(String tpDirection);

    List<ContactInformation> firstStepStartProcess(
            Acquisition a, List<ContactInformation> ciRe, ArrayErrors<ContactInformation> errorsList);

    List<ExecFieldReply> mandatoryExecFList(Acquisition acquisition);

    Optional<ContactInformation> filterAddress(List<ContactInformation> ciList, ContactInformation ad);

    ContactInformation changeUpdateOrCreateBy(ContactInformation ci, ContactInformation oldNotUpdate);

    List<ContactInformation> startProcessContactInformation(List<ContactInformation> contactInformationList);
}
