package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_TYPE_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.JPEG_EXT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.JPG_EXT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PDF_TEXT_GUION_BAJO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PNG_EXT;
import static co.com.bancolombia.util.constants.Constants.DOCUMENTAL_SUBTYPE_CC_CODE;
import static co.com.bancolombia.util.constants.Constants.DOC_PROCESSED_BUCKET_FOLDER;
import static co.com.bancolombia.util.constants.Constants.PDF_EXTE;

@RequiredArgsConstructor
public class PersistenceValidateTypeDocumentImpl implements PersistenceValidateTypeDocument {

    private final PdfMerger pdfMerger;
    private final ImageConverter imageConverter;
    private final DataFileRepository dataFileRepositoryAdapter;

    @Override
    public String getBase64DiffExtension(TdcDocumentsFile tdcDocumentsFile,
                                         TdcDocument tdcDocument) throws IOException, CustomException {
        List<String> fileNames = tdcDocumentsFile.getFileNames();
        Optional<String> ext = getExtension(fileNames.get(0));
        String base64File;
        if (ext.isPresent()) {
            if (ext.get().equals(PDF_EXTE)) {
                base64File = pdfMerger.mergePdf(tdcDocumentsFile.getFileNames());
            } else if (ext.get().equals(JPEG_EXT) || ext.get().equals(JPG_EXT) || ext.get().equals(PNG_EXT)) {
                base64File = imageConverter.convert(dataFileRepositoryAdapter.getBase64Bytes(tdcDocumentsFile
                                .getFileNames().get(0), DOC_PROCESSED_BUCKET_FOLDER),
                        dataFileRepositoryAdapter.getBase64Bytes(tdcDocumentsFile.getFileNames().get(1),
                                DOC_PROCESSED_BUCKET_FOLDER));
            } else {
                List<byte[]> bytes = fileNames.stream()
                        .map(a -> {
                            try {
                                return dataFileRepositoryAdapter.getBase64Bytes(a, DOC_PROCESSED_BUCKET_FOLDER);
                            } catch (IOException e) {
                                throw new CustomException("");
                            }
                        }).collect(Collectors.toList());
                base64File = imageConverter.convertTIFFToPDF(bytes);
            }
            return base64File;
        } else {
            throw new IOException();
        }
    }

    private String getFileFinalName(String documentNumber) {
        return DOCUMENTAL_SUBTYPE_CC_CODE + PDF_TEXT_GUION_BAJO + DOCUMENT_TYPE_CC + PDF_TEXT_GUION_BAJO +
                documentNumber + "." + PDF_EXTE;
    }

    private Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf('.') + Numbers.ONE.getIntNumber()));
    }
}
