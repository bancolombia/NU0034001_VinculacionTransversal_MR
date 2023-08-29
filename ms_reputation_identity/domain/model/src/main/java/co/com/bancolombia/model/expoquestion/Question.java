package co.com.bancolombia.model.expoquestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Question {

    private String identifier;
    private String text;
    private String order;
    private String correctAnswerId;
    private String weight;
}
