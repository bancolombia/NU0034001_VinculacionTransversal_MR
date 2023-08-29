package co.com.bancolombia.api;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import lombok.Data;

@Data
public class TestUtils {

    public static MetaRequest buildMetaRequest(String op) {
        MetaRequest meta = new MetaRequest(
                "AW78461", "3a15584b-6a7a-4ba2-8d27-3fea93c97b87", "1",
                "20150625200000", "BIZAGI", "10.0.0.0", "management", op);
        return meta;
    }
}