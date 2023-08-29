package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.util.constants.Constants.ERROR_IMAGE_CONVERSION;
import static co.com.bancolombia.util.constants.Constants.IMAGE_CONVERTER_NAME;
import static co.com.bancolombia.util.constants.Constants.IMAGE_CONVERTER_UTILITY;
import static co.com.bancolombia.util.constants.Constants.PDF_MIXED_NAME;

public class ImageConverter {


    private static LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, IMAGE_CONVERTER_UTILITY,
            IMAGE_CONVERTER_NAME);

    public String convert(byte[] image1, byte[] image2) {
        Document document = new Document();
        String output = "mixedFile.pdf";
        String base64 = EMPTY;
        try {
            FileOutputStream fos = new FileOutputStream(output);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            document.open();
            this.addPagesToDoc(document, image1, image2);
            document.close();
            writer.close();
            File fileMerged = new File(PDF_MIXED_NAME);
            byte[] fileContent = Files.readAllBytes(fileMerged.toPath());
            base64 = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException | DocumentException e) {
            adapter.error(ERROR_IMAGE_CONVERSION, e);
        }
        return base64;
    }

    private void addPagesToDoc(Document document, byte[] image1, byte[] image2) throws DocumentException,
            IOException {
        Image tempImage = Image.getInstance(image1);
        Rectangle pageSize = new Rectangle(tempImage.getWidth() + Numbers.SIXTY.getIntNumber(),
                tempImage.getHeight() + Numbers.SIXTY.getIntNumber());
        document.setPageSize(pageSize);
        document.newPage();
        document.add(tempImage);
        Image tempImageTwo = Image.getInstance(image2);
        Rectangle pageSizeTwo = new Rectangle(tempImage.getWidth() + Numbers.SIXTY.getIntNumber(),
                tempImage.getHeight() + Numbers.SIXTY.getIntNumber());
        document.setPageSize(pageSizeTwo);
        document.newPage();
        document.add(tempImageTwo);
    }

    private List<RandomAccessFileOrArray> getListAccessFiles(List<byte[]> bytes) {
        return bytes.stream().map(bytes1 -> new RandomAccessFileOrArray
                (new RandomAccessSourceFactory().createSource(bytes1))).collect(Collectors.toList());
    }

    public String convertTIFFToPDF(List<byte[]> bytes) throws IOException {
        String base64 = EMPTY;
        try (FileOutputStream fos = new FileOutputStream("output.pdf")) {
            List<RandomAccessFileOrArray> randomAccess = getListAccessFiles(bytes);
            Document tifftoPDF = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(tifftoPDF, fos);
            pdfWriter.setStrictImageSequence(true);
            tifftoPDF.open();
            Image tempImage;
            for (RandomAccessFileOrArray myTiffFile : randomAccess) {
                for (int j = 1; j <= TiffImage.getNumberOfPages(myTiffFile); j++) {
                    tempImage = TiffImage.getTiffImage(myTiffFile, j);
                    Rectangle pageSize = new Rectangle(tempImage.getWidth() + Numbers.SIXTY.getIntNumber(),
                            tempImage.getHeight() + Numbers.SIXTY.getIntNumber());
                    tifftoPDF.setPageSize(pageSize);
                    tifftoPDF.newPage();
                    tifftoPDF.add(tempImage);
                }
            }
            tifftoPDF.close();
            File pdfFile = new File("output.pdf");
            byte[] fileContent = Files.readAllBytes(pdfFile.toPath());
            base64 = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException | DocumentException e) {
            adapter.error(ERROR_IMAGE_CONVERSION, e);
        }
        return base64;
    }
}
