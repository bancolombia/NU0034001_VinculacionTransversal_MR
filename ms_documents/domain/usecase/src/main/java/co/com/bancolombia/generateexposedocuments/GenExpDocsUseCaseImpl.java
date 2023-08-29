package co.com.bancolombia.generateexposedocuments;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.generatepdf.GeneratePdfUseCase;
import co.com.bancolombia.model.exposedocuments.ExposeDocs;
import co.com.bancolombia.model.generatepdf.GeneratePdf;
import co.com.bancolombia.model.generatepdf.GeneratePdfResponseData;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_GEN_EXP_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENEPDF_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPOSE_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR_DOCUMENT_EXPOSE;

@RequiredArgsConstructor
public class GenExpDocsUseCaseImpl implements GenExpDocsUseCase {

    private final DataFileRepository dataFileRepository;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final GeneratePdfUseCase generatePdfUseCase;
    private final Exceptions exceptions;
    private final CoreFunctionDate coreFunctionDate;

    @Override
    public GeneratePdf getPdf(Acquisition acquisition){
        GeneratePdf generatePdf = generatePdfUseCase.generatePdf(acquisition, acquisition.getCreatedBy());

        GeneratePdfResponseData data = GeneratePdfResponseData.builder()
                .statusGeneracionPdf(generatePdf.getCodeResult())
                .urlFileClient(generatePdf.getUrlFileClient())
                .urlFileCustodie(generatePdf.getUrlFileCustody()).build();

        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder()
                .dateRequestReuse(generatePdf.getDateRequest())
                .responseReuse(new Gson().toJson(data, GeneratePdfResponseData.class))
                .dateResponseReuse(generatePdf.getDateResponse()).build();

        return generatePdf.toBuilder().infoReuseCommon(infoReuseCommon).build();
    }

    @Override
    public ExposeDocs urlFinal(Acquisition acquisition, GeneratePdf generatePdf){
        if (generatePdf.getCodeResult().equals(GENEPDF_ERROR)){
            exceptions.createException(null, OPER_EXPOSE_DOCS, EMPTY, CODE_ERROR_DOCUMENT_EXPOSE);
        }

        String preSignedUrl = dataFileRepository.generateUrl(
                generatePdf.getUrlFileCustody(), coreFunctionDate.expirationTimeMillis(Numbers.FIVE.getIntNumber()));
        vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_GEN_EXP_DOCS, CODE_ST_OPE_COMPLETADO);
        return ExposeDocs.builder().preSignedUrl(preSignedUrl).build();
    }
}