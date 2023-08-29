package co.com.bancolombia.signdocument.txt;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.model.signdocument.SDRequestTxt;

import java.io.IOException;
import java.io.InputStream;

public interface SDTxtInitialUseCase {

    InputStream createTxt(Acquisition acquisition, SDRequestTxt requestTxt, SignDocumentReply sdReply)
            throws IOException;
}
