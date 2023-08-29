package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.ADRRESS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.C_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.EXT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.NEIGHTBORHOOD;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.PHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.REST_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SEPARATOR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_BRAND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_CELLPHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_CITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_DEPARTMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.S_EMAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.T_ADDRESS;

@RequiredArgsConstructor
public class SDTxtSixUseCase {

    private final SDTxtUtilUseCase genericUseCase;

    public void createIteration(BufferedWriter bw, SDRequestTxt requestTxt, int iteration,
                                ContactInfoReply contactInfo) throws IOException {
        List<ContactInfoCompReply> contactInfoComp = contactInfo.getContactInfoCompList();
        requestTxt = requestTxt.toBuilder().dateRequest(contactInfo.getCreatedDate().toString()).build();
        genericUseCase.addIteration(bw, requestTxt, Numbers.SIX.getNumber(), iteration);
        for (ContactInfoCompReply item : contactInfoComp) {
            genericUseCase.addLine(bw, T_ADDRESS, item.getAddressType());
            genericUseCase.addLine(bw, S_BRAND, item.getBrand());
            genericUseCase.addLine(bw, ADRRESS, item.getAddress());
            genericUseCase.addLine(bw, NEIGHTBORHOOD, item.getNeighborhood());
            genericUseCase.addLine(bw, S_COUNTRY, item.getCountry());
            genericUseCase.addLine(bw, S_DEPARTMENT, item.getDepartment());
            genericUseCase.addLine(bw, S_CITY, item.getCity());
            genericUseCase.addLine(bw, PHONE, item.getPhone());
            genericUseCase.addLine(bw, EXT, item.getExt());
            genericUseCase.addLine(bw, S_CELLPHONE, item.getCellphone());
            genericUseCase.addLine(bw, S_EMAIL, item.getEmail());
            genericUseCase.addLine(bw, C_NAME, item.getCompanyName());
            if (contactInfoComp.size() > 1) {
                genericUseCase.addLine(bw, SEPARATOR, EMPTY);
            }
        }
        genericUseCase.addLine(bw, REST_OK, EMPTY);
        bw.newLine();
    }
}
