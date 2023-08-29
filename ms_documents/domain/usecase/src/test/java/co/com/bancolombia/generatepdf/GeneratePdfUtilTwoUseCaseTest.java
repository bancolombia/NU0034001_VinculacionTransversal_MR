package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.GenExposeReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.CompanyInformationPdf;
import co.com.bancolombia.model.generatepdf.ContactInformationPdf;
import co.com.bancolombia.model.generatepdf.EconomicInformationPdf;
import co.com.bancolombia.model.generatepdf.FinancialInformationPdf;
import co.com.bancolombia.model.generatepdf.InternationalOperationPdf;
import co.com.bancolombia.model.generatepdf.TributaryInformationPdf;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_PENDIENTE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GeneratePdfUtilTwoUseCaseTest {

    @InjectMocks
    @Spy
    private GeneratePdfUtilTwoUseCaseImpl generatePdfUtilTwoUseCase;

    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;

    @Mock
    private GeneratePdfUtilOneUseCase generatePdfUtilOneUseCase;

    @Mock
    private GeneratePdfTaxUseCase generatePdfTaxUseCase;

    @Mock
    private GeneratePdfConvertJson generatePdfConvertJson;

    private CheckList checkList;

    private Acquisition acquisition;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        checkList = CheckList.builder()
                .state(StateStep.builder().code(CODE_ST_OPE_COMPLETADO).build())
                .build();

        acquisition = Acquisition.builder()
                .id(UUID.randomUUID())
                .documentNumber("10000000")
                .documentType(DocumentType.builder().code("TIPDOC_FS001").build())
                .typeAcquisition(TypeAcquisition.builder().code("VT001").build())
                .build();
    }

    @Test
    public void validateAndFindInfoSuccessTest() {
        GenExposeReply r = GenExposeReply.builder()
                .personalInfo(PersonalInfoReply.builder().firstName("JUAN").firstSurname("PEREZ").build())
                .basicInfo(BasicInfoReply.builder().build())
                .contactResInfo(ContactInfoCompReply.builder().build())
                .contactWorkInfo(ContactInfoCompReply.builder().build())
                .economicInfo(EconomicInfoReply.builder().build())
                .taxInfo(TaxInfoReply.builder().build())
                .foreignInfo(ForeignInfoReply.builder().build())
                .build();

        doReturn(checkList).when(generatePdfUtilOneUseCase).checkListFindByOperation(
                any(Acquisition.class), anyString());
        doReturn(r).when(naturalPersonUseCase).getGenerateExposeInfo(any(UUID.class));
        doReturn("NAME").when(generatePdfUtilOneUseCase).getGeographicName(any(), any(), anyInt());
        doReturn("NAME").when(generatePdfUtilOneUseCase).getCatalogName(anyString(), any());
        doReturn(ContactInformationPdf.builder().build()).when(generatePdfTaxUseCase).contactInformationPdf(
                any(ContactInfoCompReply.class));
        doReturn(EconomicInformationPdf.builder().build()).when(generatePdfTaxUseCase).economicInformationPdf(
                any(EconomicInfoReply.class));
        doReturn(CompanyInformationPdf.builder().build()).when(generatePdfTaxUseCase).companyInformationPdf(
                any(ContactInfoCompReply.class));
        doReturn(FinancialInformationPdf.builder().build()).when(generatePdfTaxUseCase).financialInformationPdf(
                any(EconomicInfoReply.class));
        doReturn(TributaryInformationPdf.builder().build()).when(generatePdfTaxUseCase).tributaryInformationPdf(
                any(TaxInfoReply.class));
        doReturn(InternationalOperationPdf.builder().build()).when(generatePdfTaxUseCase).internationalOperationPdf(
                any(ForeignInfoReply.class));
        doReturn(new JsonObject()).when(generatePdfConvertJson).getInfo(any(AcquisitionPdf.class));

        Optional<AcquisitionPdf> acquisitionPdf = generatePdfUtilTwoUseCase.validateAndFindInfo(acquisition);
        assertTrue(acquisitionPdf.isPresent());
    }

    @Test
    public void validateAndFindInfoEmptyTest() {
        checkList.getState().setCode(CODE_ST_OPE_PENDIENTE);
        doReturn(checkList).when(generatePdfUtilOneUseCase).checkListFindByOperation(
                any(Acquisition.class), anyString());

        Optional<AcquisitionPdf> acquisitionPdf = generatePdfUtilTwoUseCase.validateAndFindInfo(acquisition);
        assertFalse(acquisitionPdf.isPresent());
    }
}
