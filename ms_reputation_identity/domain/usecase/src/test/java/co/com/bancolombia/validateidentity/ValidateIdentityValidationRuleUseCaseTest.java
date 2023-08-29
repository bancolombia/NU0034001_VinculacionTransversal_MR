package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRuleReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.validateidentity.ValidateIdentityAddresses;
import co.com.bancolombia.model.validateidentity.ValidateIdentityAlerts;
import co.com.bancolombia.model.validateidentity.ValidateIdentityEmails;
import co.com.bancolombia.model.validateidentity.ValidateIdentityMobiles;
import co.com.bancolombia.model.validateidentity.ValidateIdentityPhones;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA01;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA02;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA03;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA04;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA05;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA06;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA07;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA08;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA09;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA10;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MIN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_PHONETHICS;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class ValidateIdentityValidationRuleUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateIdentityValidationRuleUseCaseImpl validationRuleUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private ValidateIdentityRuleUtilUseCase validateIUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getBigDecimalTest() {
        BigDecimal number = validationRuleUseCase.getBigDecimal(Double.parseDouble("10.0"));
        assertNotNull(number);
    }

    @Test
    public void calculateAccumulatedTest() {
        ArrayList<ValidateIdentityRuleReply> list = new ArrayList<>();
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA01).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA02).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA03).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA04).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA05).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA06).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA07).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA08).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA09).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA10).score("0.0").build());
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        rulesMap.put(THRESHOLD_PHONETHICS, Double.valueOf("80"));
        List<ValidateIdentityPhones> phones = Collections.singletonList(ValidateIdentityPhones.builder().address("").phoneNumber("").build());
        List<ValidateIdentityMobiles> mobiles = Collections.singletonList(ValidateIdentityMobiles.builder().mobile("").build());
        List<ValidateIdentityEmails> emails = Collections.singletonList(ValidateIdentityEmails.builder().email("").build());
        List<ValidateIdentityAddresses> addresses = Collections.singletonList(ValidateIdentityAddresses.builder().address("").build());
        List<ValidateIdentityAlerts> alerts = Collections.singletonList(ValidateIdentityAlerts.builder().idAlert("").build());

        ValidateIdentitySave validateIdentityAllRegister = ValidateIdentitySave.builder().firstSurname("").mobiles(mobiles).alerts(alerts)
                .phones(phones).emails(emails).antiquity("").ageMax("100").ageMin("0").addresses(addresses).fullName("").issueDate(new Date())
                .secondSurname("").build();
        ValidateIdentityReply infoNecesaryValidateRule = ValidateIdentityReply.builder().firstSurname("").secondSurname("").secondName("").firstName("")
                .expeditionDate(new Date()).birthDate(new Date()).address("").cellphonePersonal("").cellphoneContact("")
                .companyName("").emailContact("").emailPersonal("").telephoneNumber("").build();

        Mockito.doReturn(ValidateIdentityRuleReply.builder().active("true")
                .name("").score("0.0").build()).when(validateIUtil).validateIfApplyRule(anyList(), anyString());
        Mockito.doReturn(101).when(coreFunctionDate).returnAge(any(Date.class));
        Mockito.doReturn(false).when(validateIUtil).compareString(anyString(), anyString(), anyDouble());
        Mockito.doReturn("M").when(validateIUtil).getFullNameInverse(any(ValidateIdentityReply.class));
        Mockito.doReturn("M").when(validateIUtil).getNames(any(ValidateIdentityReply.class));
        Mockito.doReturn(false).when(validateIUtil).isInRange(anyString(), anyString(), anyInt());

        ValidateIdentityScore score = validationRuleUseCase.calculateAccumulated(list,
                validateIdentityAllRegister, infoNecesaryValidateRule, rulesMap);
        assertNotNull(score);
    }

    @Test
    public void calculateAccumulatedTwoTest() {
        ArrayList<ValidateIdentityRuleReply> list = new ArrayList<>();
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA01).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA02).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA03).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA04).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA05).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA06).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA07).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA08).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA09).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA10).score("0.0").build());
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        rulesMap.put(THRESHOLD_PHONETHICS, Double.valueOf("80"));
        List<ValidateIdentityPhones> phones = Collections.singletonList(ValidateIdentityPhones.builder().address("").phoneNumber("").build());
        List<ValidateIdentityMobiles> mobiles = Collections.singletonList(ValidateIdentityMobiles.builder().mobile("").build());
        List<ValidateIdentityEmails> emails = Collections.singletonList(ValidateIdentityEmails.builder().email("").build());
        List<ValidateIdentityAddresses> addresses = Collections.singletonList(ValidateIdentityAddresses.builder().address("").build());
        List<ValidateIdentityAlerts> alerts = Collections.singletonList(ValidateIdentityAlerts.builder().idAlert("").build());

        ValidateIdentitySave validateIdentityAllRegister = ValidateIdentitySave.builder().firstSurname("").mobiles(mobiles).alerts(alerts)
                .phones(phones).emails(emails).antiquity("").ageMax("100").ageMin("0").addresses(addresses).fullName("M").issueDate(new Date())
                .secondSurname("M").name("M").build();
        ValidateIdentityReply infoNecesaryValidateRule = ValidateIdentityReply.builder().firstSurname("").secondSurname("M").secondName("").firstName("")
                .expeditionDate(new Date(2010, Calendar.NOVEMBER, 12)).birthDate(new Date()).address("").cellphonePersonal("").cellphoneContact("")
                .companyName("").emailContact("").emailPersonal("").telephoneNumber("").build();

        Mockito.doReturn(ValidateIdentityRuleReply.builder().active("true")
                .name("").score("0.0").build()).when(validateIUtil).validateIfApplyRule(anyList(), anyString());
        Mockito.doReturn(10).when(coreFunctionDate).returnAge(any(Date.class));
        Mockito.doReturn(true).when(validateIUtil).compareString(anyString(), anyString(), anyDouble());
        Mockito.doReturn("M").when(validateIUtil).getFullNameInverse(any(ValidateIdentityReply.class));
        Mockito.doReturn("M").when(validateIUtil).getNames(any(ValidateIdentityReply.class));
        Mockito.doReturn(true).when(validateIUtil).isInRange(anyString(), anyString(), anyInt());

        ValidateIdentityScore score = validationRuleUseCase.calculateAccumulated(list,
                validateIdentityAllRegister, infoNecesaryValidateRule, rulesMap);
        assertNotNull(score);
    }

    @Test
    public void calculateAccumulatedThreeTest() {
        ArrayList<ValidateIdentityRuleReply> list = new ArrayList<>();
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA01).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA02).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA03).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA04).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA05).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA06).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA07).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA08).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA09).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA10).score("0.0").build());
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        rulesMap.put(THRESHOLD_PHONETHICS, Double.valueOf("80"));
        List<ValidateIdentityPhones> phones = Collections.singletonList(ValidateIdentityPhones.builder().address("").phoneNumber("").build());
        List<ValidateIdentityMobiles> mobiles = Collections.singletonList(ValidateIdentityMobiles.builder().mobile("").build());
        List<ValidateIdentityEmails> emails = Collections.singletonList(ValidateIdentityEmails.builder().email("").build());
        List<ValidateIdentityAddresses> addresses = Collections.singletonList(ValidateIdentityAddresses.builder().address("").build());
        List<ValidateIdentityAlerts> alerts = Collections.singletonList(ValidateIdentityAlerts.builder().idAlert("").build());

        ValidateIdentitySave validateIdentityAllRegister = ValidateIdentitySave.builder().firstSurname("").mobiles(mobiles).alerts(alerts)
                .phones(phones).emails(emails).antiquity("").ageMax("100").ageMin("0").addresses(addresses).fullName("").issueDate(new Date())
                .secondSurname("").build();
        ValidateIdentityReply infoNecesaryValidateRule = ValidateIdentityReply.builder().firstSurname("").secondSurname("").secondName("").firstName("")
                .expeditionDate(new Date()).birthDate(new Date()).address("").cellphonePersonal("").cellphoneContact("")
                .companyName("").emailContact("").emailPersonal("").telephoneNumber("").build();

        Mockito.doReturn(null).when(validateIUtil).validateIfApplyRule(anyList(), anyString());
        Mockito.doReturn(101).when(coreFunctionDate).returnAge(any(Date.class));
        Mockito.doReturn(false).when(validateIUtil).compareString(anyString(), anyString(), anyDouble());
        Mockito.doReturn("M").when(validateIUtil).getFullNameInverse(any(ValidateIdentityReply.class));
        Mockito.doReturn("M").when(validateIUtil).getNames(any(ValidateIdentityReply.class));
        Mockito.doReturn(false).when(validateIUtil).isInRange(anyString(), anyString(), anyInt());

        ValidateIdentityScore score = validationRuleUseCase.calculateAccumulated(list,
                validateIdentityAllRegister, infoNecesaryValidateRule, rulesMap);
        assertNotNull(score);
    }

    @Test
    public void calculateAccumulatedFourTest() {
        ArrayList<ValidateIdentityRuleReply> list = new ArrayList<>();
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA01).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA02).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA03).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA04).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA05).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA06).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA07).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA08).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA09).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA10).score("0.0").build());
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        rulesMap.put(THRESHOLD_PHONETHICS, Double.valueOf("80"));
        List<ValidateIdentityPhones> phones = Collections.singletonList(ValidateIdentityPhones.builder().address("").phoneNumber("").build());
        List<ValidateIdentityMobiles> mobiles = Collections.singletonList(ValidateIdentityMobiles.builder().mobile("").build());
        List<ValidateIdentityEmails> emails = Collections.singletonList(ValidateIdentityEmails.builder().email("").build());
        List<ValidateIdentityAddresses> addresses = Collections.singletonList(ValidateIdentityAddresses.builder().address("").build());
        List<ValidateIdentityAlerts> alerts = Collections.singletonList(ValidateIdentityAlerts.builder().idAlert("").build());

        ValidateIdentitySave validateIdentityAllRegister = ValidateIdentitySave.builder().firstSurname("").mobiles(mobiles).alerts(alerts)
                .phones(phones).emails(emails).antiquity("").ageMax("100").ageMin("0").addresses(addresses).fullName("M").issueDate(new Date())
                .secondSurname("M").name("M").build();
        ValidateIdentityReply infoNecesaryValidateRule = ValidateIdentityReply.builder().firstSurname("1").secondSurname("M").secondName("1").firstName("1")
                .expeditionDate(null).birthDate(null).address("1").cellphonePersonal("1").cellphoneContact("1")
                .companyName("1").emailContact("1").emailPersonal("1").telephoneNumber("1").build();

        Mockito.doReturn(ValidateIdentityRuleReply.builder().active("true")
                .name("").score("0.0").build()).when(validateIUtil).validateIfApplyRule(anyList(), anyString());
        Mockito.doReturn(10).when(coreFunctionDate).returnAge(any(Date.class));
        Mockito.doReturn(false).when(validateIUtil).compareString(anyString(), anyString(), anyDouble());
        Mockito.doReturn("M").when(validateIUtil).getFullNameInverse(any(ValidateIdentityReply.class));
        Mockito.doReturn("M").when(validateIUtil).getNames(any(ValidateIdentityReply.class));
        Mockito.doReturn(true).when(validateIUtil).isInRange(anyString(), anyString(), anyInt());

        ValidateIdentityScore score = validationRuleUseCase.calculateAccumulated(list,
                validateIdentityAllRegister, infoNecesaryValidateRule, rulesMap);
        assertNotNull(score);
    }

    @Test
    public void calculateAccumulatedFiveTest() {
        ArrayList<ValidateIdentityRuleReply> list = new ArrayList<>();
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA01).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA02).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA03).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA04).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA05).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA06).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA07).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA08).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA09).score("0.0").build());
        list.add(ValidateIdentityRuleReply.builder().active("true")
                .name(REGLA10).score("0.0").build());
        Map<String, Double> rulesMap = new HashMap<>();
        rulesMap.put(THRESHOLD_MIN, Double.valueOf("10"));
        rulesMap.put(THRESHOLD_MAX, Double.valueOf("90"));
        rulesMap.put(THRESHOLD_PHONETHICS, Double.valueOf("80"));
        List<ValidateIdentityPhones> phones = Collections.singletonList(ValidateIdentityPhones.builder().address("").phoneNumber("").build());
        List<ValidateIdentityMobiles> mobiles = Collections.singletonList(ValidateIdentityMobiles.builder().mobile("").build());
        List<ValidateIdentityEmails> emails = Collections.singletonList(ValidateIdentityEmails.builder().email("").build());
        List<ValidateIdentityAddresses> addresses = Collections.singletonList(ValidateIdentityAddresses.builder().address("").build());
        List<ValidateIdentityAlerts> alerts = Collections.singletonList(ValidateIdentityAlerts.builder().idAlert("").build());

        ValidateIdentitySave validateIdentityAllRegister = ValidateIdentitySave.builder().firstSurname("").mobiles(mobiles).alerts(alerts)
                .phones(phones).emails(emails).antiquity("").ageMax("100").ageMin("0").addresses(addresses).fullName("M").issueDate(new Date())
                .secondSurname("M").name("M").build();
        ValidateIdentityReply infoNecesaryValidateRule = ValidateIdentityReply.builder().firstSurname("1").secondSurname("J").secondName("1").firstName("1")
                .expeditionDate(null).birthDate(null).address("1").cellphonePersonal(null).cellphoneContact(null)
                .companyName(null).emailContact(null).emailPersonal(null).telephoneNumber(null).build();

        Mockito.doReturn(ValidateIdentityRuleReply.builder().active("true")
                .name("").score("0.0").build()).when(validateIUtil).validateIfApplyRule(anyList(), anyString());
        Mockito.doReturn(10).when(coreFunctionDate).returnAge(any(Date.class));
        Mockito.doReturn(true).when(validateIUtil).compareString(anyString(), anyString(), anyDouble());
        Mockito.doReturn("M").when(validateIUtil).getFullNameInverse(any(ValidateIdentityReply.class));
        Mockito.doReturn("M").when(validateIUtil).getNames(any(ValidateIdentityReply.class));
        Mockito.doReturn(true).when(validateIUtil).isInRange(anyString(), anyString(), anyInt());

        ValidateIdentityScore score = validationRuleUseCase.calculateAccumulated(list,
                validateIdentityAllRegister, infoNecesaryValidateRule, rulesMap);
        assertNotNull(score);
    }


}
