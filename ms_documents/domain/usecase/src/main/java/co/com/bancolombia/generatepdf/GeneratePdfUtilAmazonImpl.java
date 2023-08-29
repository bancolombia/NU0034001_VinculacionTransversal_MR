package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.common.exception.CustomIOException;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.UtilContentStream;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.uploadedfile.DataFile;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPOSE_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PDF_TEXT_GUION_BAJO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.util.constants.Constants.CODE_TEMPLATE_PDF;
import static co.com.bancolombia.util.constants.Constants.ERROR_ENCRYPT_FILE;
import static co.com.bancolombia.util.constants.Constants.FILE_CLIENT;
import static co.com.bancolombia.util.constants.Constants.FILE_CUSTODIE;
import static co.com.bancolombia.util.constants.Constants.FORMATO_PDF;
import static co.com.bancolombia.util.constants.Constants.NAME_FILE;
import static co.com.bancolombia.util.constants.Constants.RUTA_ARCHIVO_CLIENTE;
import static co.com.bancolombia.util.constants.Constants.RUTA_ARCHIVO_CUSTODIA;
import static co.com.bancolombia.util.constants.Constants.TEXT_COPY_FROM_ORIGIN;
import static co.com.bancolombia.util.constants.Constants.TEXT_CUSTODIA;

@RequiredArgsConstructor
public class GeneratePdfUtilAmazonImpl implements GeneratePdfUtilAmazon {

    private final DataFileRepository dataFileRepository;
    private final GeneratePdfDocumentUseCase generatePdfDocumentUseCase;
    private final ParametersRepository parametersRepository;
    private final GenPdfA1B genPdfA1B;

    private LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, OPER_EXPOSE_DOCS, "GeneratePdfUtilAmazon");

    @Override
    public Map<String, String> saveInAmazon(
            AcquisitionPdf acquisitionPdf, PDDocument document, PDPage page) throws IOException {

        Map<String, String> result = null;
        Map<String, String> findUrlFolders = findUrlFolders();
        PDDocument documentFinal = genPdfA1B.convertPdfA1B(document);

        String folderFileCustody = findUrlFolders.get(RUTA_ARCHIVO_CUSTODIA);
        String folderFileClient = findUrlFolders.get(RUTA_ARCHIVO_CLIENTE);
        String nameFile = findUrlFolders.get(NAME_FILE);
        String docNumber = acquisitionPdf.getDocumentNumber();
        String docType = acquisitionPdf.getDocumentType();
        docType = docType.substring(docType.lastIndexOf(PDF_TEXT_GUION_BAJO) + 1);
        String nameFileFirstPart = nameFile.concat(PDF_TEXT_GUION_BAJO)
                .concat(docType).concat(PDF_TEXT_GUION_BAJO)
                .concat(docNumber);

        String nameFileCustody = nameFileFirstPart.concat(PDF_TEXT_GUION_BAJO)
                .concat(TEXT_CUSTODIA).concat(FORMATO_PDF);
        String nameFileClient = nameFileFirstPart.concat(FORMATO_PDF);

        DataFile dfCustody = returnDatafile(documentFinal, nameFileCustody, folderFileCustody);

        insertCommentCopy(TEXT_COPY_FROM_ORIGIN, documentFinal, page);
        applySecurityAndPolitic(acquisitionPdf.getDocumentNumber(), documentFinal);

        DataFile dfClient = returnDatafile(documentFinal, nameFileClient, folderFileClient);
        result = save(dfClient, dfCustody);
        documentFinal.close();

        return result;
    }

    private Map<String, String> findUrlFolders() {
        Map<String, String> map = new HashMap<>();
        List<Parameters> list = parametersRepository.findByParent(CODE_TEMPLATE_PDF);
        list.stream().forEach(item -> {
            if (RUTA_ARCHIVO_CUSTODIA.equals(item.getCode())) {
                map.put(RUTA_ARCHIVO_CUSTODIA, item.getName());
            }
            if (RUTA_ARCHIVO_CLIENTE.equals(item.getCode())) {
                map.put(RUTA_ARCHIVO_CLIENTE, item.getName());
            }
            if (NAME_FILE.equals(item.getCode())) {
                map.put(NAME_FILE, item.getName());
            }
        });
        return map;
    }

    private DataFile returnDatafile(
            PDDocument documentFinal, String nameFileCustody, String folderFileCustody) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] pdfAcquisitionBytes = null;
        documentFinal.save(os);
        pdfAcquisitionBytes = os.toByteArray();
        os.flush();
        os.close();

        try (FileOutputStream fos = new FileOutputStream(FilenameUtils.getName(nameFileCustody))) {
            fos.write(pdfAcquisitionBytes);
        }

        String nameF = FilenameUtils.getName(nameFileCustody);
        return DataFile.builder()
                .folder(folderFileCustody).name(nameF)
                .file(new File(FilenameUtils.getName(nameFileCustody))).build();
    }

    private Map<String, String> save(DataFile dfClient, DataFile dfCustody) throws IOException {
        dfCustody = dataFileRepository.save(dfCustody);
        dfClient = dataFileRepository.save(dfClient);

        Map<String, String> result = new HashMap<>();
        result.put(FILE_CLIENT, dfClient.getUrl());
        result.put(FILE_CUSTODIE, dfCustody.getUrl());
        return result;
    }

    private void insertCommentCopy(String text, PDDocument documentFinal, PDPage page) {
        if (true) {
            generatePdfDocumentUseCase.configureContentStream(UtilContentStream.builder()
                    .color(Color.RED)
                    .fontSize(Numbers.EIGHT.getIntNumber())
                    .fontName(PDType1Font.HELVETICA)
                    .positionX(Numbers.ONE_HUNDRED.getIntNumber() + Numbers.SEVENTY.getIntNumber())
                    .positionY(Numbers.TEN.getIntNumber())
                    .text(text)
                    .build(), documentFinal, page);
        }
    }

    private void applySecurityAndPolitic(String password, PDDocument documentFinal) throws CustomIOException {
        AccessPermission ap = new AccessPermission();
        ap.setCanPrint(true);
        ap.setCanExtractContent(true);
        ap.setCanModify(false);
        ap.setCanPrintDegraded(true);
        ap.setCanAssembleDocument(true);
        ap.setCanExtractContent(true);
        ap.setCanExtractForAccessibility(true);
        ap.setReadOnly();
        int keyLength = Numbers.TWO_HUNDRED.getIntNumber() + Numbers.FIFTY.getIntNumber() + Numbers.SIX.getIntNumber();
        StandardProtectionPolicy spp = new StandardProtectionPolicy(null, null, ap);
        spp.setEncryptionKeyLength(keyLength);
        spp.setPermissions(ap);
        spp.setUserPassword(password);
        spp.setOwnerPassword(password);

        try {
            documentFinal.protect(spp);
        } catch (IOException e) {
            adapter.error(ERROR_ENCRYPT_FILE);
        }
    }
}
