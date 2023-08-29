package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MapErrorAcquisition {
    HashMap<String, List<ErrorField>> map;

    public MapErrorAcquisition(String code, ErrorField errorField) {
        this.map = new HashMap<>();
        this.map.put(code, Collections.singletonList(errorField));
    }
}
