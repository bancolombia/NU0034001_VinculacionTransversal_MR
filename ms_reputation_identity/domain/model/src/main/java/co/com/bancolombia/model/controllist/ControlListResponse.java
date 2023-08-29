package co.com.bancolombia.model.controllist;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ControlListResponse {

    private MetaControlList meta;
    private List<DataControlList> data;
    private LinksControlList links;

    private InfoReuseCommon infoReuseCommon;
}
