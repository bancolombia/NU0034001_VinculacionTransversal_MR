package co.com.bancolombia.generatepdf;

import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.GeneratePdfResponseData;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENEPDF_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENPDF_EXITO;
import static co.com.bancolombia.util.constants.Constants.FILE_CLIENT;
import static co.com.bancolombia.util.constants.Constants.FILE_CUSTODIE;

@RequiredArgsConstructor
public class GeneratePdfProcessUseCaseImpl implements GeneratePdfProcessUseCase {

    private final GeneratePdfDocumentUseCase generatePdfDocumentUseCase;

    @Override
    public GeneratePdfResponseData generatePdf(AcquisitionPdf acquisitionPdf) {
        Map<String, String> resultGeneratePdf = generatePdfDocumentUseCase.generatePdf(acquisitionPdf);
        GeneratePdfResponseData gp;
        if (resultGeneratePdf != null) {
            gp = GeneratePdfResponseData.builder()
                    .acquisitionId(acquisitionPdf.getAcquisitionId())
                    .statusGeneracionPdf(GENPDF_EXITO)
                    .urlFileClient(resultGeneratePdf.get(FILE_CLIENT))
                    .urlFileCustodie(resultGeneratePdf.get(FILE_CUSTODIE))
                    .build();
        } else {
            gp = GeneratePdfResponseData.builder()
                    .acquisitionId(acquisitionPdf.getAcquisitionId())
                    .statusGeneracionPdf(GENEPDF_ERROR)
                    .urlFileClient(EMPTY)
                    .urlFileCustodie(EMPTY)
                    .build();
        }

        return gp;
    }
}
