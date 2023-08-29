package co.com.bancolombia.model.expoquestion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataResponse {
    @JsonProperty("identification")
    private JsonObject identification;
    @JsonProperty("questionnaire")
    private JsonObject questionnaire;
}
