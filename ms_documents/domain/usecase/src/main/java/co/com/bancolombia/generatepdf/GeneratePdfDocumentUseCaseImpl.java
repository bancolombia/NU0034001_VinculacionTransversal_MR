package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.UtilContentStream;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPOSE_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.util.constants.Constants.ERROR_CLOSE_FILE;
import static co.com.bancolombia.util.constants.Constants.ERROR_CONFIGURE_CONTENT_STREAM;
import static co.com.bancolombia.util.constants.Constants.ERROR_LOAD_TEMPLATE_PDF;
import static co.com.bancolombia.util.constants.Constants.ERROR_READ_JSON;
import static co.com.bancolombia.util.constants.Constants.ERROR_SAVE_FILE;

@RequiredArgsConstructor
public class GeneratePdfDocumentUseCaseImpl implements GeneratePdfDocumentUseCase {

    private final DataFileRepository dataFileRepository;
    private final GeneratePdfUtilAmazon generatePdfUtilAmazon;
    private final GeneratePdfUtilDocsUseCase generatePdfUtilDocsUseCase;

    private LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, OPER_EXPOSE_DOCS, "GeneratePdfDocumentUseCase");

    @Override
    public void configureContentStream(UtilContentStream utilContentStream, PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream;
            contentStream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true, true);

            contentStream.setNonStrokingColor(utilContentStream.getColor());
            contentStream.beginText();
            contentStream.setFont(utilContentStream.getFontName(), utilContentStream.getFontSize());
            contentStream.newLineAtOffset(utilContentStream.getPositionX(), utilContentStream.getPositionY());
            contentStream.showText(utilContentStream.getText());
            contentStream.endText();
            contentStream.closeAndStroke();
            contentStream.close();
        } catch (IOException e) {
            adapter.info(ERROR_CONFIGURE_CONTENT_STREAM);
        }
    }

    @Override
    public Map<String, String> generatePdf(AcquisitionPdf acquisitionPdf) {
        String typeAcquisition = acquisitionPdf.getTypeAcquisition();
        List<UtilContentStream> list = getCoordinates(typeAcquisition);
        Optional<PDDocument> documentTemplate = returnTemplatePdf(findUrlTemplate(typeAcquisition));
        Map<String, String> result;
        if (documentTemplate.isPresent()) {
            PDDocument documentFinal = new PDDocument();
            List<PDPage> pages = new ArrayList<>();
            List<List<UtilContentStream>> dataList = new ArrayList<>();
            documentTemplate.get().getDocumentCatalog().getPages().forEach(page -> {
                pages.add(page);
                dataList.add(list.stream().filter(p -> p.getPage() == dataList.size()).collect(Collectors.toList()));
            });

            PDPage pageFinal = constructAll(acquisitionPdf, pages, documentFinal, dataList);
            result = finalizeDoc(acquisitionPdf, documentFinal, pageFinal);

            try {
                documentTemplate.get().close();
            } catch (IOException e) {
                adapter.error(ERROR_CLOSE_FILE, e);
            }
        } else {
            adapter.info(ERROR_LOAD_TEMPLATE_PDF);
            result = null;
        }
        return result;
    }

    private List<UtilContentStream> getCoordinates(String typeAcquisition) {
    	return generatePdfUtilDocsUseCase.getCoordinates(typeAcquisition);
    }

    private Optional<PDDocument> returnTemplatePdf(String urlFileTemplate) {
        PDDocument documentTemplate = null;

        try {
            InputStream template = dataFileRepository.retrieveFileInputStream(urlFileTemplate);
            documentTemplate = Loader.loadPDF(template);
        } catch (IOException e) {
            adapter.info(ERROR_LOAD_TEMPLATE_PDF);
        }

        return Optional.ofNullable(documentTemplate);
    }

    private String findUrlTemplate(String typeAcquisition) {
        return generatePdfUtilDocsUseCase.findUrlTemplate(typeAcquisition);
    }

    private JsonObject getObject(String info) {
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(info.replace("null", " "));
    }

    private PDPage constructAll(AcquisitionPdf acquisitionPdf, List<PDPage> pages,
            PDDocument documentFinal, List<List<UtilContentStream>> dataList) {

        int index = 0;
        PDPage finalPage = null;
        try {
            JsonObject jObj = getObject(acquisitionPdf.getAllInfo());
            for (PDPage page : pages) {
                documentFinal.addPage(page);
                dataList.get(index).forEach(utilContentStream -> {
                    JsonElement data = jObj.get(utilContentStream.getKey());
                    if (data != null) {
                        UtilContentStream utl = utilContentStream;
                        utl.setText(data.getAsString());
                        configureContentStream(utl, documentFinal, page);
                    }
                });
                finalPage = page;
                index++;
            }
        } catch (CustomException e) {
            adapter.error(ERROR_READ_JSON, e);
        }

        return finalPage;
    }

    private Map<String, String> finalizeDoc(AcquisitionPdf acquisitionPdf, PDDocument documentFinal, PDPage page) {
        Map<String, String> result = null;
        try {
            result = generatePdfUtilAmazon.saveInAmazon(acquisitionPdf, documentFinal, page);
        } catch (IOException e) {
            adapter.error(ERROR_SAVE_FILE);
        }

        return result;
    }
}
