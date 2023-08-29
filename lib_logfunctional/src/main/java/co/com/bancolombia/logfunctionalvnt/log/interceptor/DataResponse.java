package co.com.bancolombia.logfunctionalvnt.log.interceptor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataResponse {
    @JsonProperty("response")
    private JsonObject response;
    @JsonProperty("otherDataResponse")
    private JsonObject otherDataResponse;
}
