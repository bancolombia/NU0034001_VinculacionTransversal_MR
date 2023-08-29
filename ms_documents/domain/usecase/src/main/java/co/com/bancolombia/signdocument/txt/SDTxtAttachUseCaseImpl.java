package co.com.bancolombia.signdocument.txt;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.FIRST_ATTACH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.NAME_TXT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.TEST_PLAIN;

@RequiredArgsConstructor
public class SDTxtAttachUseCaseImpl implements SDTxtAttachUseCase {

    private final SDTxtInitialUseCase sdTxtInitialUseCase;

    @Override
    public String attachFileToPdf(Acquisition acquisition, SDRequestTxt requestTxt, InputStream inputStream,
                                  SignDocumentReply sdReply) throws IOException {
        PDDocument doc = Loader.loadPDF(inputStream);
        PDEmbeddedFilesNameTreeNode efTree = new PDEmbeddedFilesNameTreeNode();
        PDComplexFileSpecification fs = new PDComplexFileSpecification();
        fs.setFile(NAME_TXT);
        InputStream sdTxt = sdTxtInitialUseCase.createTxt(acquisition, requestTxt, sdReply);
        PDEmbeddedFile ef = new PDEmbeddedFile(doc, sdTxt);
        ef.setSubtype(TEST_PLAIN);
        fs.setEmbeddedFile(ef);
        Map<String, PDComplexFileSpecification> efMap = new HashMap<>();
        efMap.put(FIRST_ATTACH, fs);
        efTree.setNames(efMap);
        PDDocumentNameDictionary names = new PDDocumentNameDictionary(doc.getDocumentCatalog());
        names.setEmbeddedFiles(efTree);
        doc.getDocumentCatalog().setNames(names);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out);
        doc.close();
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }
}
