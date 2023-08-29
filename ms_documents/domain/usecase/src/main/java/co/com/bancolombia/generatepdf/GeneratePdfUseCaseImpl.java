package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.GeneratePdf;
import co.com.bancolombia.model.generatepdf.GeneratePdfResponseData;
import co.com.bancolombia.model.generatepdf.gateways.GeneratePdfRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENEPDF_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENPDF_EXITO;

@RequiredArgsConstructor
public class GeneratePdfUseCaseImpl implements GeneratePdfUseCase {

	private final GeneratePdfProcessUseCase generatePdfProcessUseCase;
	private final GeneratePdfUtilTwoUseCase generatePdfUtilTwoUseCase;
	private final GeneratePdfRepository generatePdfRepository;
	private final CoreFunctionDate coreFunctionDate;

	@Override
	public GeneratePdf findByAcquisition(Acquisition acquisition) {
		return generatePdfRepository.findTopByAcquisitionOrderByCreatedDateDesc(acquisition);
	}

	@Override
	public GeneratePdf generatePdf(Acquisition acquisition, String createdBy) {
		Optional<AcquisitionPdf> acquisitionPdfOptional = generatePdfUtilTwoUseCase.validateAndFindInfo(acquisition);
		Date now = coreFunctionDate.getDatetime();
		GeneratePdf generatePdf = GeneratePdf.builder().codeResult(GENEPDF_ERROR).dateRequest(now).build();

		if (acquisitionPdfOptional.isPresent()) {
			GeneratePdfResponseData data = generatePdfProcessUseCase.generatePdf(acquisitionPdfOptional.get());
			if(data != null) {
				generatePdf = generatePdf.toBuilder()
						.urlFileClient(data.getUrlFileClient())
						.urlFileCustody(data.getUrlFileCustodie())
						.codeResult(GENPDF_EXITO).build();
			}
		}

		return generatePdfRepository.save(
				generatePdf.toBuilder()
						.createdBy(createdBy).createdDate(now)
						.dateResponse(now).acquisition(acquisition).build());
	}
}
