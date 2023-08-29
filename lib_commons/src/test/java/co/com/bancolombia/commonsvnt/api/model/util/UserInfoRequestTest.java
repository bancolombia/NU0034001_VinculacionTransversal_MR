package co.com.bancolombia.commonsvnt.api.model.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserInfoRequestTest {

    @Test
    public void validateUserInfoRequestTest() {

        MetaRequest metaRequest = MetaRequest.builder().build();
        UserInfoRequestData userInfoRequestData = UserInfoRequestData.builder().build();
        UserInfoRequest userInfoRequest1 = new UserInfoRequest(metaRequest, userInfoRequestData);

        UserInfoRequest userInfoRequest2 = new UserInfoRequest();
        userInfoRequest2.setData(userInfoRequestData);
        userInfoRequest2.setMeta(metaRequest);

        userInfoRequest1.getData();
        userInfoRequest1.getMeta();

        assertNotNull(userInfoRequest1);
        assertNotNull(userInfoRequest1.toString());
        assertNotNull(userInfoRequest1.hashCode());
        Assert.assertEquals(userInfoRequest1,userInfoRequest2);
    }

}