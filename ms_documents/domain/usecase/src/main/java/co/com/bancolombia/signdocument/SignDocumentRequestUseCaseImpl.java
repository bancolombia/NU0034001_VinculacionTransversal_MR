package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.generatepdf.GeneratePdfUseCase;
import co.com.bancolombia.model.generatepdf.GeneratePdf;
import co.com.bancolombia.model.signdocument.SDDocument;
import co.com.bancolombia.model.signdocument.SDMetadata;
import co.com.bancolombia.model.signdocument.SDRequest;
import co.com.bancolombia.model.signdocument.SDRequestItem;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.signdocument.txt.SDTxtAttachUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class SignDocumentRequestUseCaseImpl implements SignDocumentRequestUseCase {

    private final DataFileRepository dataFileRepository;
    private final NaturalPersonUseCase naturalPersonUseCase;
    private final SDTxtAttachUseCase sdTxtAttachUseCase;
    private final GeneratePdfUseCase generatePdfUseCase;

    @Value("${signDocument.subtypeCode}")
    private String subtypeCode;

    @Override
    public SDRequest createRequest(Acquisition acquisition, SDRequestTxt requestTxt) throws IOException {
        GeneratePdf generatePdf = generatePdfUseCase.findByAcquisition(acquisition);
        SignDocumentReply sdReply = naturalPersonUseCase.getSignDocumentReply(acquisition.getId());
        InputStream inputStream = dataFileRepository.retrieveFileInputStream(generatePdf.getUrlFileCustody());
        String file = sdTxtAttachUseCase.attachFileToPdf(acquisition, requestTxt, inputStream, sdReply);
        SDMetadata metadata = SDMetadata.builder().numeroRadicado(acquisition.getId().toString()).build();
        SDDocument document = SDDocument.builder().subtypeCode(subtypeCode).fileName(generatePdf.getUrlFileCustody())
                .file(file).metadata(metadata).build();
        SDRequestItem requestItem = SDRequestItem.builder()
                .documentType(acquisition.getDocumentType().getCodeGenericType())
                .documentNumber(acquisition.getDocumentNumber())
                .digitalSignature(sdReply.getValidateToken().getTokenCode())
                .document(document).build();
        return SDRequest.builder().data(requestItem).build();
    }
}
