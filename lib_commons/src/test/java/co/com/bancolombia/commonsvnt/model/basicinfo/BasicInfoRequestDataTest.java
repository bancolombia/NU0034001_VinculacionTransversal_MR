package co.com.bancolombia.commonsvnt.model.basicinfo;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BasicInfoRequestDataTest {

    @Test
    public void validateBasicInfoRequestDataTest() {
        BasicInfoRequestData basicInfoRequestData1 = BasicInfoRequestData.builder()
                .gender("")
                .birthCity("")
                .country("")
                .birthDepartment("")
                .civilStatus("")
                .nationality("")
                .dependants("")
                .educationLevel("")
                .socialStratum("")
                .housingType("")
                .pep("")
                .contractType("")
                .entryCompanyDate("").build();

        BasicInfoRequestData basicInfoRequestData2 = new BasicInfoRequestData
                ("", "", "", "", "",
                        "", "", "", "",
                        "", "", "", "");

        basicInfoRequestData1.getGender();
        basicInfoRequestData1.getBirthCity();
        basicInfoRequestData1.getCountry();
        basicInfoRequestData1.getBirthDepartment();
        basicInfoRequestData1.getCivilStatus();
        basicInfoRequestData1.getNationality();
        basicInfoRequestData1.getDependants();
        basicInfoRequestData1.getEducationLevel();
        basicInfoRequestData1.getSocialStratum();
        basicInfoRequestData1.getHousingType();
        basicInfoRequestData1.getPep();
        basicInfoRequestData1.getContractType();
        basicInfoRequestData1.getEntryCompanyDate();

        basicInfoRequestData2.setGender("");
        basicInfoRequestData2.setBirthCity("");
        basicInfoRequestData2.setCountry("");
        basicInfoRequestData2.setBirthDepartment("");
        basicInfoRequestData2.setCivilStatus("");
        basicInfoRequestData2.setNationality("");
        basicInfoRequestData2.setDependants("");
        basicInfoRequestData2.setEducationLevel("");
        basicInfoRequestData2.setSocialStratum("");
        basicInfoRequestData2.setHousingType("");
        basicInfoRequestData2.setPep("");
        basicInfoRequestData2.setContractType("");
        basicInfoRequestData2.setEntryCompanyDate("");

        BasicInfoRequestData basicInfoRequestData3 = new BasicInfoRequestData();

        assertNotNull(basicInfoRequestData1);
        assertNotNull(basicInfoRequestData3);
        assertNotNull(basicInfoRequestData3.toString());
        assertNotNull(basicInfoRequestData1.toString());
        assertNotNull(basicInfoRequestData1.hashCode());
        Assert.assertEquals(basicInfoRequestData1,basicInfoRequestData2);

    }


}