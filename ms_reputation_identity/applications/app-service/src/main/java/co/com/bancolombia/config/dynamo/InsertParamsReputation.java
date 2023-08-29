package co.com.bancolombia.config.dynamo;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.parameter.Parameter;
import co.com.bancolombia.model.parameter.gateways.ParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;

@Component
public class InsertParamsReputation implements ApplicationRunner {

    @Autowired
    ParametersRepository repository;

    @Autowired
    CoreFunctionDate coreFunctionDate;

    private static List<String> dataInsert() {
        List<String> list = new ArrayList<>();
        list.add("50,THRESHOLD_MIN,VT001,validateIdentity");
        list.add("80,THRESHOLD_MAX,VT001,validateIdentity");
        list.add("90,THRESHOLD_PHONETHICS,VT001,validateIdentity");
        list.add("50,THRESHOLD_MIN,VT003,validateIdentity");
        list.add("80,THRESHOLD_MAX,VT003,validateIdentity");
        list.add("90,THRESHOLD_PHONETHICS,VT003,validateIdentity");
        list.add("1,VALIDATE_UPLOAD_MANUAL,,validateIdentity");
        list.add("1,VALIDATE_MATCH_EMAIL_CELL,,validateIdentity");
        list.add("00,Documento Vigente 00,,PARENT_VIGENT_IDENTITY");
        list.add("98,Documento Vigente 98,,PARENT_VIGENT_IDENTITY");
        return list;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        repository.deletedAll();
        List<Parameter> parameters = repository.listAll();
        dataInsert().forEach(item -> {
            String[] attributes = item.split(",");
            Optional<Parameter> itemSave = createParameters(attributes, parameters);
            itemSave.ifPresent(parameter -> repository.save(parameter));
        });
    }

    private Optional<Parameter> createParameters(String[] metadata, List<Parameter> listExist) {
        if (!listExist.isEmpty()) {
            for (Parameter item : listExist) {
                if (item.getCode().equals(metadata[0])
                        && item.getName().equals(metadata[1])
                        && nullToString(item.getTypeAcquisition()).equals(nullToString(metadata[2]))
                        && item.getParent().equals(metadata[3])) {
                    return Optional.empty();
                }
            }
        }
        return buildParameters(metadata);
    }

    private Optional<Parameter> buildParameters(String[] metadata) {
        return Optional.of(Parameter.builder()
                .code(metadata[0])
                .name(metadata[1])
                .typeAcquisition(metadata[2])
                .parent(metadata[3])
                .createdDate(coreFunctionDate.getDatetime()).createdBy("USUARIO").build());
    }

    private static String nullToString(String string) {
        if (string == null) {
            return EMPTY;
        }
        return string;
    }
}
