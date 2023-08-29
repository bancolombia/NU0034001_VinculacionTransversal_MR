package co.com.bancolombia.commonsvnt.model.commons.secretsmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.netty.tcp.TcpClient;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TpcClientGeneric {
    private TcpClient client;
}
