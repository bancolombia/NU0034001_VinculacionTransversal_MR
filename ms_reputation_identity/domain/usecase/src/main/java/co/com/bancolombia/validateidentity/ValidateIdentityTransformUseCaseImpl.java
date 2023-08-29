package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.Addresses;
import co.com.bancolombia.model.validateidentity.Age;
import co.com.bancolombia.model.validateidentity.Alerts;
import co.com.bancolombia.model.validateidentity.Emails;
import co.com.bancolombia.model.validateidentity.IdDetail;
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
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATE_FORMAT;

@RequiredArgsConstructor
public class ValidateIdentityTransformUseCaseImpl implements ValidateIdentityTransformUseCase {

    private final CoreFunctionDate coreFD;

    @Override
    public List<ValidateIdentityAddresses> transformValidateAddress(List<Addresses> addresses) {
        List<ValidateIdentityAddresses> addressesList = new ArrayList<>();
        if (addresses != null) {
            addresses.forEach(
                    a -> addressesList.add(ValidateIdentityAddresses.builder().source(a.getSource()).city(a.getCity())
                            .address(a.getAddress()).addressType(a.getAddressType()).numberReports(a.getNumberReports())
                            .creationDate(a.getCreationDate()).useType(a.getUseType()).area(a.getArea())
                            .typeResidence(a.getTypeResidence()).updateDate(a.getUpdateDate()).entities(a.getEntities())
                            .deliveryProbality(a.getDeliveryProbality()).typeCorrespondence(a.getTypeCorrespondence())
                            .department(a.getDepartment()).idAddress(a.getIdAddress()).idCountry(a.getIdCountry())
                            .idDepartment(a.getIdDepartment()).socioEconomicStratum(a.getSocioEconomicStratum())
                            .lastConfirmation(a.getLastConfirmation()).build()));
        }
        return addressesList;
    }

    @Override
    public List<ValidateIdentityEmails> transformValidateEmails(List<Emails> emails) {
        List<ValidateIdentityEmails> emailsList = new ArrayList<>();
        if (emails != null) {
            emails.forEach(
                    e -> emailsList.add(ValidateIdentityEmails.builder().email(e.getEmail())
                            .entities(e.getEntities()).idEmail(e.getIdEmail()).creationDate(e.getCreationDate())
                            .lastConfirmation(e.getLastConfirmation()).numberReports(e.getNumberReports())
                            .reporting(e.getReporting()).updateDate(e.getUpdateDate()).source(e.getSource()).build()));
        }
        return emailsList;
    }

    @Override
    public List<ValidateIdentityPhones> transformValidatePhones(List<Phones> phones) {
        List<ValidateIdentityPhones> phonesList = new ArrayList<>();
        if (phones != null) {
            phones.forEach(
                    p -> phonesList.add(ValidateIdentityPhones.builder().source(p.getSource())
                            .creationDate(p.getCreationDate()).typeCorrespondence(p.getTypeCorrespondence())
                            .typeResidence(p.getTypeResidence()).updateDate(p.getUpdateDate()).useType(p.getUseType())
                            .address(p.getAddress()).city(p.getCity()).idArea(p.getIdArea()).entities(p.getEntities())
                            .department(p.getDepartment()).idAddress(p.getIdAddress())
                            .idCountry(p.getIdCountry()).phoneNumber(p.getPhoneNumber())
                            .lastConfirmation(p.getLastConfirmation()).numberReports(p.getNumberReports()).build()));
        }
        return phonesList;
    }

    @Override
    public List<ValidateIdentityMobiles> transformValidateMobiles(List<Mobiles> mobiles) {
        List<ValidateIdentityMobiles> mobilesList = new ArrayList<>();
        if (mobiles != null) {
            mobiles.forEach(
                    m -> mobilesList.add(ValidateIdentityMobiles.builder().mobile(m.getMobile()).source(m.getSource())
                            .typee(m.getType()).creationDate(m.getCreationDate()).idMobile(m.getIdMobile())
                            .updateDate(m.getUpdateDate()).entities(m.getEntities()).reporting(m.getReporting())
                            .lastConfirmation(m.getLastConfirmation()).numberReports(m.getNumberReports()).build()));
        }
        return mobilesList;
    }

    @Override
    public List<ValidateIdentityAlerts> transformValidateAlerts(List<Alerts> alerts) {
        List<ValidateIdentityAlerts> alertsList = new ArrayList<>();
        if (alerts != null) {
            alerts.forEach(
                    a -> alertsList.add(ValidateIdentityAlerts.builder().idAlert(a.getIdAlert())
                            .idCountry(a.getIdCountry()).lengthRecord(a.getLengthRecord())
                            .expirationDate(coreFD.getDateFromString(a.getExpirationDate(), DATE_FORMAT))
                            .modificationDate(coreFD.getDateFromString(a.getModificationDate(), DATE_FORMAT))
                            .recordDate(coreFD.getDateFromString(a.getRecordDate(), DATE_FORMAT))
                            .typeAlert(a.getTypeAlert()).typeRecord(a.getTypeRecord()).text(a.getText()).build()));
        }
        return alertsList;
    }

    @Override
    public ValidateIdentitySave setVariablesValidateIdentity(ValidateIdentitySave validateIdentitySave,
                                                             NaturalLegalPerson naturalLegalPerson) {
        if (naturalLegalPerson != null) {
            validateIdentitySave.setRut(naturalLegalPerson.getRut());
            validateIdentitySave.setName(naturalLegalPerson.getNames());
            validateIdentitySave.setFirstSurname(naturalLegalPerson.getFirstSurname());
            validateIdentitySave.setSecondSurname(naturalLegalPerson.getSecondSurname());
            validateIdentitySave.setGender(naturalLegalPerson.getGender());
            validateIdentitySave.setMaritalStatus(naturalLegalPerson.getMaritalStatus());
            validateIdentitySave.setCheckInformation(naturalLegalPerson.getCheckInformation());
            validateIdentitySave.setFullName(naturalLegalPerson.getFullName());
            validateIdentitySave.setIdCiiu(naturalLegalPerson.getIdCIIU());
            validateIdentitySave.setEconomicActivity(naturalLegalPerson.getEconomicActivity());
            validateIdentitySave.setAntiquity(naturalLegalPerson.getAntiquity());
            Age age = naturalLegalPerson.getAge();
            if (age != null) {
                validateIdentitySave.setAgeMax(age.getMax());
                validateIdentitySave.setAgeMin(age.getMin());
            }
            IdDetail idDetail = naturalLegalPerson.getIdDetail();
            if (idDetail != null) {
                validateIdentitySave.setStatus(idDetail.getStatus());
                validateIdentitySave.setIssueDate(coreFD.getDateFromString(idDetail.getIssueDate(), DATE_FORMAT));
                validateIdentitySave.setCity(idDetail.getCity());
                validateIdentitySave.setDepartment(idDetail.getDepartment());
                validateIdentitySave.setIdNumber(idDetail.getIdNumber());
            }
        }
        return validateIdentitySave;
    }

    @Override
    public ValidateIdentitySave transformValidateIdentitySave(ValidateIdentityResponse validateIdentityResponse,
                                                              AcquisitionReply acquisition,
                                                              BasicAcquisitionRequest bAcqR) {
        ValidateIdentitySave validateIdentitySave = ValidateIdentitySave.builder()
                .idAcquisition(acquisition.getAcquisitionId())
                .messageId(validateIdentityResponse.getMeta().getMessageId()).createdBy(bAcqR.getUserTransaction())
                .requestDate(validateIdentityResponse.getMeta().getRequestDate())
                .dateConsulted(validateIdentityResponse.getData().getDateConsulted())
                .createdDate(coreFD.getDatetime()).idType(validateIdentityResponse.getData().getIdType())
                .addresses(transformValidateAddress(validateIdentityResponse.getData().getAddresses()))
                .emails(transformValidateEmails(validateIdentityResponse.getData().getEmails()))
                .phones(transformValidatePhones(validateIdentityResponse.getData().getPhones()))
                .mobiles(transformValidateMobiles(validateIdentityResponse.getData().getMobiles()))
                .alerts(transformValidateAlerts(validateIdentityResponse.getData().getAlerts())).build();
        return setVariablesValidateIdentity(validateIdentitySave,
                validateIdentityResponse.getData().getNaturalLegalPerson());
    }
}
