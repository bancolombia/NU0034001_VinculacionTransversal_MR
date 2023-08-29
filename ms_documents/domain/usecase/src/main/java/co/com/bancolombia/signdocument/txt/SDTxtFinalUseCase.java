package co.com.bancolombia.signdocument.txt;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.model.signdocument.SDRequestTxt;

import java.io.BufferedWriter;
import java.io.IOException;

public interface SDTxtFinalUseCase {

    void createTxt(BufferedWriter bufferedWriter, SDRequestTxt requestTxt, SignDocumentReply sdReply)
            throws IOException;
}
