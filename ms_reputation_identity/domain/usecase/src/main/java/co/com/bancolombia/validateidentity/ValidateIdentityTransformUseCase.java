package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.Addresses;
import co.com.bancolombia.model.validateidentity.Alerts;
import co.com.bancolombia.model.validateidentity.Emails;
import co.com.bancolombia.model.validateidentity.Mobiles;
import co.com.bancolombia.model.validateidentity.NaturalLegalPerson;
import co.com.bancolombia.model.validateidentity.Phones;
import co.com.bancolombia.model.validateidentity.ValidateIdentityAddresses;
import co.com.bancolombia.model.validateidentity.ValidateIdentityAlerts;
import co.com.bancolombia.model.validateidentity.ValidateIdentityEmails;
import co.com.bancolombia.model.validateidentity.ValidateIdentityMobiles;
import co.com.bancolombia.model.validateidentity.ValidateIdentityPhones;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;

import java.util.List;

public interface ValidateIdentityTransformUseCase {

    public List<ValidateIdentityAddresses> transformValidateAddress(List<Addresses> addresses);

    public List<ValidateIdentityEmails> transformValidateEmails(List<Emails> emails);

    public List<ValidateIdentityPhones> transformValidatePhones(List<Phones> phones);

    public List<ValidateIdentityMobiles> transformValidateMobiles(List<Mobiles> mobiles);

    public List<ValidateIdentityAlerts> transformValidateAlerts(List<Alerts> alerts);

    public ValidateIdentitySave setVariablesValidateIdentity(ValidateIdentitySave validateIdentitySave,
                                                             NaturalLegalPerson naturalLegalPerson);

    public ValidateIdentitySave transformValidateIdentitySave(ValidateIdentityResponse validateIdentityResponse,
                                                              AcquisitionReply acquisition,
                                                              BasicAcquisitionRequest bAcqR);
}
