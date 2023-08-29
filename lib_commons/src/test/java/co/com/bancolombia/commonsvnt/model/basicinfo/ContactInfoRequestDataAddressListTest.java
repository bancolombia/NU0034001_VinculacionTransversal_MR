package co.com.bancolombia.commonsvnt.model.basicinfo;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class ContactInfoRequestDataAddressListTest {

    @Test
    public void validateContactInfoRequestDataAddressListTest(){
        ContactInfoRequestDataAddressList contactInfoRequestDataAddressList1 =
                ContactInfoRequestDataAddressList.builder()
                        .addressType("")
                        .brand("")
                        .companyName("")
                        .address("")
                        .neighborhood("")
                        .city("")
                        .department("")
                        .country("")
                        .phone("")
                        .cellphone("")
                        .ext("")
                        .email("").build();

        ContactInfoRequestDataAddressList contactInfoRequestDataAddressList2 = new ContactInfoRequestDataAddressList();
        contactInfoRequestDataAddressList2.setAddressType("");
        contactInfoRequestDataAddressList2.setBrand("");
        contactInfoRequestDataAddressList2.setCompanyName("");
        contactInfoRequestDataAddressList2.setAddress("");
        contactInfoRequestDataAddressList2.setNeighborhood("");
        contactInfoRequestDataAddressList2.setCity("");
        contactInfoRequestDataAddressList2.setDepartment("");
        contactInfoRequestDataAddressList2.setCountry("");
        contactInfoRequestDataAddressList2.setPhone("");
        contactInfoRequestDataAddressList2.setCellphone("");
        contactInfoRequestDataAddressList2.setExt("");
        contactInfoRequestDataAddressList2.setEmail("");

        contactInfoRequestDataAddressList2.getAddressType();
        contactInfoRequestDataAddressList2.getBrand();
        contactInfoRequestDataAddressList2.getCompanyName();
        contactInfoRequestDataAddressList2.getAddress();
        contactInfoRequestDataAddressList2.getNeighborhood();
        contactInfoRequestDataAddressList2.getCity();
        contactInfoRequestDataAddressList2.getDepartment();
        contactInfoRequestDataAddressList2.getCountry();
        contactInfoRequestDataAddressList2.getPhone();
        contactInfoRequestDataAddressList2.getCellphone();
        contactInfoRequestDataAddressList2.getExt();
        contactInfoRequestDataAddressList2.getEmail();

        ContactInfoRequestDataAddressList contactInfoRequestDataAddressList3 =
                new ContactInfoRequestDataAddressList("","","","","","","","","","","","");

        assertNotNull(contactInfoRequestDataAddressList1);
        assertNotNull(contactInfoRequestDataAddressList1.toString());
        assertNotNull(contactInfoRequestDataAddressList1.hashCode());
        assertNotNull(contactInfoRequestDataAddressList3.hashCode());
        assertNotNull(contactInfoRequestDataAddressList3.toString());
        Assert.assertEquals(contactInfoRequestDataAddressList1,contactInfoRequestDataAddressList2);
        Assert.assertEquals(contactInfoRequestDataAddressList1,contactInfoRequestDataAddressList3);
    }

}