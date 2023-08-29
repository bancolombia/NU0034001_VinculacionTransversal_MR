package co.com.bancolombia.logtechnicalvnt.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cipher {
	private static final int START_TRUNK = 3;

    private Cipher() {
    }

    public static String cipherMessage(String jsonString, List<String> fieldsToHide) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode actualObj;
        StringBuilder data;
        actualObj = (ObjectNode) mapper.readTree(jsonString);
        data = new StringBuilder(jsonString);

        if (actualObj != null && !fieldsToHide.isEmpty()) {
            StringBuilder fieldValue = new StringBuilder();
            for (String field : fieldsToHide) {
                for (JsonNode list : actualObj.findValues(field)) {
                    trunk(list, fieldValue, data);
                }
            }
        }
        return data.toString();
    }

    private static void trunk(JsonNode list, StringBuilder fieldValue, StringBuilder data) {
        String t = list.textValue();
        fieldValue.append(t);
        int end = t.length();
        int amountChRepeated = end - START_TRUNK;
        String sRepeated = IntStream.range(0, amountChRepeated).mapToObj(i -> "*")
                .collect(Collectors.joining(""));
        int index = data.indexOf(fieldValue.toString());
        fieldValue.replace(START_TRUNK, end, sRepeated);
        data.replace(index, (end + index), String.valueOf(fieldValue));
        fieldValue.delete(0, end);
    }
}
