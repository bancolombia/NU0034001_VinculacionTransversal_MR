package co.com.bancolombia.generatepdf;

import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.generatepdf.UtilContentStream;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.pdfobject.PdfObject;
import co.com.bancolombia.model.pdfobject.gateway.PdfObjectRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPOSE_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.util.constants.Constants.CODE_TEMPLATE_PDF;
import static co.com.bancolombia.util.constants.Constants.ERROR_CONFIGURE_CONTENT_STREAM;

@RequiredArgsConstructor
public class GeneratePdfUtilDocsUseCaseImpl implements GeneratePdfUtilDocsUseCase {

	private final ParametersRepository parametersRepository;
	private final PdfObjectRepository pdfObjectRepository;

	private LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, OPER_EXPOSE_DOCS, "GeneratePdfUtilDocsUseCase");

	@Override
	public String findUrlTemplate(String typeAcquisition) {
		Optional<Parameters> parameters = parametersRepository.findByCodeAndParent(typeAcquisition, CODE_TEMPLATE_PDF);
        Parameters parameter = null;
        if (parameters.isPresent()) {
            parameter = parameters.get();
        }

        return parameter != null ? parameter.getName() : "";
	}

	@Override
	public List<UtilContentStream> getCoordinates(String typeAcquisition) {
		List<UtilContentStream> list = new ArrayList<>();

        List<PdfObject> listObjectPdf = pdfObjectRepository.findByTypeAcquisition(typeAcquisition);
        listObjectPdf.forEach(item -> {
            try {
                list.add(UtilContentStream.builder()
                        .key(item.getKey())
                        .page(item.getPage())
                        .color((Color) Class.forName("java.awt.Color").getField(item.getColor()).get(null))
                        .fontSize(item.getFontSize())
                        .fontName((PDType1Font) Class.forName("org.apache.pdfbox.pdmodel.font.PDType1Font")
                                .getField(item.getFontName()).get(null))
                        .positionX(item.getPositionX())
                        .positionY(item.getPositionY())
                        .build());
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
                    | ClassNotFoundException e) {
                adapter.error(ERROR_CONFIGURE_CONTENT_STREAM, e);
            }
        });

        return list;
	}
}
