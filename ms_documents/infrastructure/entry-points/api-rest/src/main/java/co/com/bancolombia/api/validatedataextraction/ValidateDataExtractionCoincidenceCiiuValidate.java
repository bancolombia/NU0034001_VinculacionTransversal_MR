package co.com.bancolombia.api.validatedataextraction;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentApiResponseData;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COINCIDENCE_CIIU_FIELD;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT_APPLY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;

@Controller
public class ValidateDataExtractionCoincidenceCiiuValidate {

    @Autowired
    private NaturalPersonUseCase naturalPersonUseCase;

    public Map<String, String> findCoincidenceCiiu(Acquisition acquisition, UploadDocumentApiResponseData data) {
        String value = NOT_APPLY;
        Map<String, String> map = new HashMap<>();
        if (data.getProcessedDocuments().get(0).getDocumentalSubTypeCode().equals(RUT_SUBTYPE)) {
            InfoRutReply reply = naturalPersonUseCase.getRequiredRut(acquisition.getId());
            if (reply.isValid() && reply.getCiiu() != null) {
                value = reply.getFlagCiiu();
            }
        }
        map.put(COINCIDENCE_CIIU_FIELD, value);
        return map;
    }
}
