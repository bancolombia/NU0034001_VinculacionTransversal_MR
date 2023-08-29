package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.api.model.util.Meta;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.DigitalizationIdentitySave;
import co.com.bancolombia.model.uploaddocument.DigitalizationRutSave;
import co.com.bancolombia.model.uploaddocument.ProcessedDocument;
import co.com.bancolombia.model.uploaddocument.ProcessedFields;
import co.com.bancolombia.model.uploaddocument.UploadDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.gateways.DigitalizationIdentityRepository;
import co.com.bancolombia.model.uploaddocument.gateways.DigitalizationRutRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.util.constants.Constants.ASSESSEE_TYPE;
import static co.com.bancolombia.util.constants.Constants.BCR_BIRTH_DATE;
import static co.com.bancolombia.util.constants.Constants.BCR_DOC_NR;
import static co.com.bancolombia.util.constants.Constants.BCR_FIRST_NAME;
import static co.com.bancolombia.util.constants.Constants.BCR_GENDER;
import static co.com.bancolombia.util.constants.Constants.BCR_LAST_NAME;
import static co.com.bancolombia.util.constants.Constants.CORPORATE_NAME;
import static co.com.bancolombia.util.constants.Constants.DIFFERENCE_DOC_NR;
import static co.com.bancolombia.util.constants.Constants.EMISSION_RUT_DATE;
import static co.com.bancolombia.util.constants.Constants.FIRST_NAME;
import static co.com.bancolombia.util.constants.Constants.FIRST_SURNAME;
import static co.com.bancolombia.util.constants.Constants.IDENTIFICATION_TYPE;
import static co.com.bancolombia.util.constants.Constants.MAIN_ACTIVITY;
import static co.com.bancolombia.util.constants.Constants.OCR_BIRTH_DATE;
import static co.com.bancolombia.util.constants.Constants.OCR_BIRTH_PLACE;
import static co.com.bancolombia.util.constants.Constants.OCR_DOC_NR;
import static co.com.bancolombia.util.constants.Constants.OCR_EMISSION_DATE;
import static co.com.bancolombia.util.constants.Constants.OCR_EMISSION_PLACE;
import static co.com.bancolombia.util.constants.Constants.OCR_FIRST_NAMES;
import static co.com.bancolombia.util.constants.Constants.OCR_GENDER;
import static co.com.bancolombia.util.constants.Constants.OCR_LAST_NAMES;
import static co.com.bancolombia.util.constants.Constants.SECONDARY_ACTIVITY;
import static co.com.bancolombia.util.constants.Constants.SECOND_NAME;
import static co.com.bancolombia.util.constants.Constants.SECOND_SURNAME;
import static co.com.bancolombia.util.constants.Constants.TAX_IDENTIFICATION_NUMBER;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ProcessDocumentSaveUseCaseTest {

    @InjectMocks
    @Spy
    private ProcessDocumentSaveUseCaseImpl processDocumentSaveUseCase;

    @Mock
    private DigitalizationIdentityRepository identityRepository;

    @Mock
    private DigitalizationRutRepository rutRepository;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    private UploadDocumentResponse upd;

    private Meta meta;

    private UploadDocument data;

    private AcquisitionProcessDocument acquisition;

    private List<ProcessedFields> fieldsIdentity;

    private List<ProcessedFields> fieldsRut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        meta = new Meta();

        data = UploadDocument.builder()
                .processedDocument(Collections.singletonList(ProcessedDocument.builder().build()))
                .build();

        upd = UploadDocumentResponse.builder()
                .meta(meta)
                .data(data)
                .build();

        acquisition = AcquisitionProcessDocument.builder()
                .id(UUID.randomUUID())
                .documentNumber("123456")
                .uploadDocumentRetries(3)
                .uploadRutRetries(3)
                .build();

        fieldsIdentity = Arrays.asList(
                getProcessedField(OCR_FIRST_NAMES),
                getProcessedField(OCR_LAST_NAMES),
                getProcessedField(OCR_DOC_NR),
                getProcessedField(OCR_BIRTH_DATE),
                getProcessedField(OCR_BIRTH_PLACE),
                getProcessedField(OCR_EMISSION_DATE),
                getProcessedField(OCR_EMISSION_PLACE),
                getProcessedField(OCR_GENDER),
                getProcessedField(BCR_DOC_NR),
                getProcessedField(BCR_FIRST_NAME),
                getProcessedField(BCR_LAST_NAME),
                getProcessedField(BCR_GENDER),
                getProcessedField(BCR_BIRTH_DATE),
                getProcessedField(DIFFERENCE_DOC_NR));

        fieldsRut = Arrays.asList(
                getProcessedField(TAX_IDENTIFICATION_NUMBER),
                getProcessedField(CORPORATE_NAME),
                getProcessedField(MAIN_ACTIVITY),
                getProcessedField(SECONDARY_ACTIVITY),
                getProcessedField(EMISSION_RUT_DATE),
                getProcessedField(ASSESSEE_TYPE),
                getProcessedField(FIRST_NAME),
                getProcessedField(SECOND_NAME),
                getProcessedField(FIRST_SURNAME),
                getProcessedField(SECOND_SURNAME),
                getProcessedField(IDENTIFICATION_TYPE),
                getProcessedField(BCR_GENDER),
                getProcessedField(BCR_BIRTH_DATE),
                getProcessedField(DIFFERENCE_DOC_NR));
    }

    @Test
    public void saveDigitalizationIdentityTest() {
        doReturn(DigitalizationIdentitySave.builder().build()).when(identityRepository).save(
                any(DigitalizationIdentitySave.class));

        upd.getData().getProcessedDocument().get(0).setProcessedFields(fieldsIdentity);
        processDocumentSaveUseCase.saveDigitalizationIdentity(upd, acquisition, "");
        verify(identityRepository, times(1)).save(any(DigitalizationIdentitySave.class));
    }

    @Test
    public void saveDigitalizationRutTest() {
        doReturn(DigitalizationRutSave.builder().build()).when(rutRepository).save(any(DigitalizationRutSave.class));

        upd.getData().getProcessedDocument().get(0).setProcessedFields(fieldsRut);
        processDocumentSaveUseCase.saveDigitalizationRut(upd, acquisition, "");
        verify(rutRepository, times(1)).save(any(DigitalizationRutSave.class));
    }

    @Test
    public void transDigitalizationIdentitySaveTest() {
        upd.getData().getProcessedDocument().get(0).setProcessedFields(fieldsIdentity);
        DigitalizationIdentitySave save = processDocumentSaveUseCase.transDigitalizationIdentitySave(
                upd, acquisition, "");
        assertNotNull(save);
    }

    @Test
    public void transDigitalizationRutSaveTest() {
        upd.getData().getProcessedDocument().get(0).setProcessedFields(fieldsRut);
        DigitalizationRutSave save = processDocumentSaveUseCase.transDigitalizationRutSave(
                upd, acquisition, "");
        assertNotNull(save);
    }

    @Test
    public void findByAcquisitionTest() {
        doReturn(DigitalizationRutSave.builder().build()).when(rutRepository).findByAcquisition(any(Acquisition.class));

        Optional<DigitalizationRutSave> save = processDocumentSaveUseCase.findByAcquisition(UUID.randomUUID());
        assertTrue(save.isPresent());
    }

    private ProcessedFields getProcessedField(String fieldName) {
        return ProcessedFields.builder().fieldName(fieldName).confidencePercentage("").fieldValue("").build();
    }
}
