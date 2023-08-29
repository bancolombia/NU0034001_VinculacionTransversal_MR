package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.Addresses;
import co.com.bancolombia.model.validateidentity.Age;
import co.com.bancolombia.model.validateidentity.Alerts;
import co.com.bancolombia.model.validateidentity.Emails;
import co.com.bancolombia.model.validateidentity.IdDetail;
import co.com.bancolombia.model.validateidentity.Meta;
import co.com.bancolombia.model.validateidentity.Mobiles;
import co.com.bancolombia.model.validateidentity.NaturalLegalPerson;
import co.com.bancolombia.model.validateidentity.Phones;
import co.com.bancolombia.model.validateidentity.ValidateIdentityAddresses;
import co.com.bancolombia.model.validateidentity.ValidateIdentityAlerts;
import co.com.bancolombia.model.validateidentity.ValidateIdentityEmails;
import co.com.bancolombia.model.validateidentity.ValidateIdentityItemsResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentityMobiles;
import co.com.bancolombia.model.validateidentity.ValidateIdentityPhones;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class ValidateIdentityTransformUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateIdentityTransformUseCaseImpl vITransUC;

    @Mock
    private CoreFunctionDate coreFD;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void transformValidateAddressTest() {
        List<Addresses> addresses = Collections.singletonList(Addresses.builder().build());
        List<ValidateIdentityAddresses> addressesList = vITransUC.transformValidateAddress(addresses);
        assertNotNull(addressesList);
    }

    @Test
    public void transformValidateAddressTestNull() {
        List<ValidateIdentityAddresses> addressesList = vITransUC.transformValidateAddress(null);
        assertNotNull(addressesList);
    }

    @Test
    public void transformValidateEmailsTest() {
        List<Emails> emails = Collections.singletonList(Emails.builder().build());
        List<ValidateIdentityEmails> emailsList = vITransUC.transformValidateEmails(emails);
        assertNotNull(emailsList);
    }

    @Test
    public void transformValidateEmailsTestNull() {
        List<ValidateIdentityEmails> emailsList = vITransUC.transformValidateEmails(null);
        assertNotNull(emailsList);
    }

    @Test
    public void transformValidateMobileTest() {
        List<Mobiles> mobiles = Collections.singletonList(Mobiles.builder().build());
        List<ValidateIdentityMobiles> mobilesList = vITransUC.transformValidateMobiles(mobiles);
        assertNotNull(mobilesList);
    }

    @Test
    public void transformValidateMobileTestNull() {
        List<ValidateIdentityMobiles> mobilesList = vITransUC.transformValidateMobiles(null);
        assertNotNull(mobilesList);
    }

    @Test
    public void transformValidatePhonesTest() {
        List<Phones> phones = Collections.singletonList(Phones.builder().build());
        List<ValidateIdentityPhones> phonesList = vITransUC.transformValidatePhones(phones);
        assertNotNull(phonesList);
    }

    @Test
    public void transformValidatePhonesTestNull() {
        List<ValidateIdentityPhones> phonesList = vITransUC.transformValidatePhones(null);
        assertNotNull(phonesList);
    }

    @Test
    public void transformValidateAlertsTest() {
        List<Alerts> alerts = Collections.singletonList(Alerts.builder().build());
        List<ValidateIdentityAlerts> alertsList = vITransUC.transformValidateAlerts(alerts);
        assertNotNull(alertsList);
    }

    @Test
    public void transformValidateAlertsTestNull() {
        List<ValidateIdentityAlerts> alertsList = vITransUC.transformValidateAlerts(null);
        assertNotNull(alertsList);
    }

    @Test
    public void transformValidateIdentitySaveTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder().messageId("")
                .userTransaction("").build();
        Meta meta = Meta.builder().messageId("").requestDate(date).build();
        Age age = Age.builder().build();
        IdDetail idDetail = IdDetail.builder().build();
        NaturalLegalPerson naturalLegalPerson = NaturalLegalPerson.builder().age(age).idDetail(idDetail).build();
        ValidateIdentityItemsResponse validateIdentity = ValidateIdentityItemsResponse.builder()
                .naturalLegalPerson(naturalLegalPerson).build();
        ValidateIdentityResponse validateIdentityResponse = ValidateIdentityResponse.builder().meta(meta)
                .data(validateIdentity).build();
        ValidateIdentitySave validateIdentitySave = this.vITransUC.transformValidateIdentitySave(validateIdentityResponse,
                AcquisitionReply.builder().acquisitionId("").build(), basicAcquisitionRequest);
        assertNotNull(validateIdentitySave);
    }

    @Test
    public void transformValidateIdentitySaveNullAgeTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder().messageId("")
                .userTransaction("").build();
        Meta meta = Meta.builder().messageId("").requestDate(date).build();
        IdDetail idDetail = IdDetail.builder().build();
        NaturalLegalPerson naturalLegalPerson = NaturalLegalPerson.builder().idDetail(idDetail).build();
        ValidateIdentityItemsResponse validateIdentity = ValidateIdentityItemsResponse.builder().naturalLegalPerson(naturalLegalPerson).build();
        ValidateIdentityResponse validateIdentityResponse = ValidateIdentityResponse.builder().meta(meta)
                .data(validateIdentity).build();
        ValidateIdentitySave validateIdentitySave = this.vITransUC
                .transformValidateIdentitySave(validateIdentityResponse, AcquisitionReply.builder().acquisitionId("").build(),
                        basicAcquisitionRequest);
        assertNotNull(validateIdentitySave);
    }

    @Test
    public void transformValidateIdentitySaveNullDetailTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder().messageId("")
                .userTransaction("").build();
        Meta meta = Meta.builder().messageId("").requestDate(date).build();
        Age age = Age.builder().build();
        NaturalLegalPerson naturalLegalPerson = NaturalLegalPerson.builder().age(age).build();
        ValidateIdentityItemsResponse validateIdentity = ValidateIdentityItemsResponse.builder()
                .naturalLegalPerson(naturalLegalPerson).build();
        ValidateIdentityResponse validateIdentityResponse = ValidateIdentityResponse.builder().meta(meta)
                .data(validateIdentity).build();
        ValidateIdentitySave validateIdentitySave = this.vITransUC
                .transformValidateIdentitySave(validateIdentityResponse, AcquisitionReply.builder().acquisitionId("").build(),
                        basicAcquisitionRequest);
        assertNotNull(validateIdentitySave);
    }

    @Test
    public void transformValidateIdentityNullNaturalPersonalSaveTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2010-05-12");
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder().messageId("")
                .userTransaction("").build();
        Meta meta = Meta.builder().messageId("").requestDate(date).build();
        ValidateIdentityItemsResponse validateIdentity = ValidateIdentityItemsResponse.builder().build();
        ValidateIdentityResponse validateIdentityResponse = ValidateIdentityResponse.builder().meta(meta)
                .data(validateIdentity).build();
        ValidateIdentitySave validateIdentitySave = this.vITransUC
                .transformValidateIdentitySave(validateIdentityResponse, AcquisitionReply.builder().acquisitionId("").build(),
                        basicAcquisitionRequest);
        assertNotNull(validateIdentitySave);
    }
}
