package co.com.bancolombia.model.generatetoken.gateways;

import co.com.bancolombia.model.generatetoken.reuserequest.GTRequest;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseWithLog;

import java.util.Date;

public interface GenerateTokenRestRepository {
    GTResponseWithLog getToken(GTRequest generateTokenRequest, String messageId, Date date);
}
