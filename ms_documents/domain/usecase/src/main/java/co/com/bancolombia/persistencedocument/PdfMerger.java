package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.util.constants.Constants.DOC_PROCESSED_BUCKET_FOLDER;
import static co.com.bancolombia.util.constants.Constants.FILE_CANT_DELETE;
import static co.com.bancolombia.util.constants.Constants.FILE_NOT_FOUND_BUCKET;
import static co.com.bancolombia.util.constants.Constants.PDF_MERGER_NAME;
import static co.com.bancolombia.util.constants.Constants.PDF_MERGER_UTILITY;
import static co.com.bancolombia.util.constants.Constants.PDF_MIXED_NAME;
import static co.com.bancolombia.util.constants.Constants.PDF_ONE_NAME;
import static co.com.bancolombia.util.constants.Constants.PDF_TWO_NAME;

public class PdfMerger {

    private final DataFileRepository dataFileRepository;

    private static LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, PDF_MERGER_UTILITY, PDF_MERGER_NAME);

    public PdfMerger(DataFileRepository dataFileRepository) {
        this.dataFileRepository = dataFileRepository;
    }

    public String mergePdf(List<String> fileNames) {
        String base64 = EMPTY;
        try {
            File file1 = dataFileRepository.convertInputStreamToFile(dataFileRepository
                            .getInputStreamOfFile(fileNames.get(0), DOC_PROCESSED_BUCKET_FOLDER),
                    new File(FilenameUtils.getName(PDF_ONE_NAME)));
            File file2 = dataFileRepository.convertInputStreamToFile(dataFileRepository
                            .getInputStreamOfFile(fileNames.get(1), DOC_PROCESSED_BUCKET_FOLDER),
                    new File(FilenameUtils.getName(PDF_TWO_NAME)));

            PDFMergerUtility mergerUtility = new PDFMergerUtility();

            mergerUtility.setDestinationFileName(FilenameUtils.getName(PDF_MIXED_NAME));

            mergerUtility.addSource(file1);
            mergerUtility.addSource(file2);
            mergerUtility.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
            File fileMerged = new File(PDF_MIXED_NAME);
            byte[] fileContent = Files.readAllBytes(fileMerged.toPath());
            base64 = Base64.getEncoder().encodeToString(fileContent);
            this.deleteFiles(file1, file2, fileMerged);
        } catch (IOException e) {
            adapter.error(FILE_NOT_FOUND_BUCKET, e);
        }
        return base64;
    }

    public void deleteFiles(File file1, File file2, File fileMerged) {
        try {
            Files.delete(file1.toPath());
            Files.delete(file2.toPath());
            Files.delete(fileMerged.toPath());
        } catch (IOException e) {
            adapter.error(FILE_CANT_DELETE, e);
        }
    }
}
