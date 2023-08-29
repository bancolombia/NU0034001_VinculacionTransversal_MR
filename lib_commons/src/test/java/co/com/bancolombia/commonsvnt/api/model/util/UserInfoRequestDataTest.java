package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;

public class UserInfoRequestDataTest {

    @InjectMocks
    @Spy
    UserInfoRequestData sut;

    @Test
    public void validateUserInfoRequestDataTest() {

        UserInfoRequestData userInfoRequestData = UserInfoRequestData.builder().build();

        userInfoRequestData.setAcquisitionId("");
        userInfoRequestData.setDocumentType("");
        userInfoRequestData.setDocumentNumber("");

        userInfoRequestData.getAcquisitionId();
        userInfoRequestData.getDocumentType();
        userInfoRequestData.getDocumentNumber();

        UserInfoRequestData userInfoRequestData1 = new UserInfoRequestData("","","");

        assertNotNull(userInfoRequestData);
        assertNotNull(userInfoRequestData.toString());
        assertNotNull(userInfoRequestData.hashCode());
        Assert.assertEquals(userInfoRequestData,userInfoRequestData1);
    }



}