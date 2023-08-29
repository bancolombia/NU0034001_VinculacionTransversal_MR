package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentError;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentResponse;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.model.persistencedocument.gateways.PersistenceDocumentRepository;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class PersistenceDocumentValidationUseCaseTest {

    @InjectMocks
    @Spy
    PersistenceDocumentValidationUseCaseImpl persistenceDocumentUseCase;

    @Mock
    private PersistenceDocumentValidateUseCase perDValidateUC;

    @Mock
    private PersistenceQueueUseCase persistenceQueueUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private PersistenceDocumentRepository persistenceDocumentRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void persistenceDocumentTwodFilesTDCTest() {
        String user = "userTest";
        List<TdcDocumentsFile> dFiles = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocumentsFile tdcDocumentsFileRut = TdcDocumentsFile.builder().documentalSubTypeCode("002")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        dFiles.add(0, tdcDocumentsFileCC);
        dFiles.add(1, tdcDocumentsFileRut);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(dFiles).build();
        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument("STG-1234").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse).infoReuseCommon(infoReuseCommon).build();
        doReturn(persistenceDocumentWithLog).when(persistenceDocumentUseCase).getListDocumentsR(any(TdcDocument.class),
                anyString(), anyBoolean(), anyList());
        doNothing().when(persistenceQueueUseCase).sendMessagesToQueue(any(TdcDocument.class));
        persistenceDocumentUseCase.persistenceDocumentTDC(tdcDocument, user, false);
        PersistenceDocument persistenceDocument = persistenceDocumentUseCase.persistenceDocumentTDC(tdcDocument, user,
                false);
        assertNotNull(persistenceDocument);
    }

    @Test
    public void persistenceDocumentOnedFilesTDCTest() {
        String user = "userTest";
        List<TdcDocumentsFile> dFiles = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        dFiles.add(0, tdcDocumentsFileCC);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(dFiles).build();
        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument("STG-1234").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse).infoReuseCommon(infoReuseCommon).build();
        doReturn(persistenceDocumentWithLog).when(perDValidateUC).getPersistenceDocumentWithLog(any(TdcDocument.class),
                any(TdcDocumentsFile.class));
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().errorDescription("asd")
                .errorCode("1433").build();
        doReturn(persistenceDocumentList).when(persistenceDocumentUseCase)
                .processDocumentResponse(any(PersistenceDocument.class), any(TdcDocument.class),
                        any(TdcDocumentsFile.class), anyString());
        doNothing().when(persistenceQueueUseCase).sendMessagesToQueue(any(TdcDocument.class));
        PersistenceDocument persistenceDocument = persistenceDocumentUseCase.persistenceDocumentTDC(tdcDocument, user,
                false);
        assertNotNull(persistenceDocument);
    }

    @Test
    public void persisDocuOnedFilesNullReuseRetriesTrueTDCTest() {
        String user = "userTest";
        List<TdcDocumentsFile> dFiles = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        dFiles.add(0, tdcDocumentsFileCC);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(dFiles).build();
        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument("STG-1234").build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse).infoReuseCommon(null).build();
        doReturn(persistenceDocumentWithLog).when(perDValidateUC).getPersistenceDocumentWithLog(any(TdcDocument.class),
                any(TdcDocumentsFile.class));
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().errorDescription("NA")
                .errorCode("1433").build();
        doReturn(persistenceDocumentList).when(persistenceDocumentUseCase)
                .processDocumentResponse(any(PersistenceDocument.class), any(TdcDocument.class),
                        any(TdcDocumentsFile.class), anyString());
        doNothing().when(persistenceQueueUseCase).sendMessagesToQueue(any(TdcDocument.class));
        PersistenceDocument persistenceDocument = persistenceDocumentUseCase.persistenceDocumentTDC(tdcDocument, user,
                true);
        assertNotNull(persistenceDocument);
    }

    @Test
    public void saveTest() {
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().errorDescription("NA")
                .errorCode("1433").build();
        persistenceDocumentUseCase.save(persistenceDocumentList);
        verify(persistenceDocumentUseCase, times(1)).save(persistenceDocumentList);
    }

    @Test
    public void mapperObjectPersistenceDocTest(){
        String user = "userTest";
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument("STG-1234").build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse).infoReuseCommon(null).build();
        doReturn(persistenceDocumentWithLog).when(perDValidateUC).getPersistenceDocumentWithLog(any(TdcDocument.class),
                any(TdcDocumentsFile.class));
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().errorDescription("NA")
                .errorCode("1433").build();
        PersistenceDocumentList result = persistenceDocumentUseCase.mapperObjectPersistenceDoc(persistenceDocumentList,
                tdcDocumentsFileCC,user);
        assertNotNull(result);
    }

    @Test
    public void getListDocumentRStatusOkTest() {
        String user = "userTest";
        List<TdcDocumentsFile> dFiles = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocumentsFile tdcDocumentsFileRut = TdcDocumentsFile.builder().documentalSubTypeCode("002")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        dFiles.add(0, tdcDocumentsFileCC);
        dFiles.add(1, tdcDocumentsFileRut);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(dFiles).build();

        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument("STG-1234").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse).infoReuseCommon(infoReuseCommon).build();


        doReturn(persistenceDocumentWithLog).when(perDValidateUC).getPersistenceDocumentWithLog(any(TdcDocument.class),
                any(TdcDocumentsFile.class));
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().status("OK").build();

        doReturn(persistenceDocumentList).when(persistenceDocumentUseCase)
                .processDocumentResponse(any(PersistenceDocument.class),any(TdcDocument.class),
                any(TdcDocumentsFile.class),anyString());

        PersistenceDocument persistenceDocument1 = persistenceDocumentUseCase.getListDocumentsR(tdcDocument, user,
                false, dFiles);
        assertNotNull(persistenceDocument1);
    }

    @Test
    public void getListDocumentRStatusAnyTest() {
        String user = "userTest";
        List<TdcDocumentsFile> dFiles = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocumentsFile tdcDocumentsFileRut = TdcDocumentsFile.builder().documentalSubTypeCode("002")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        dFiles.add(0, tdcDocumentsFileCC);
        dFiles.add(1, tdcDocumentsFileRut);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(dFiles).build();

        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument("STG-1234").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse).infoReuseCommon(infoReuseCommon).build();
        doReturn(persistenceDocumentWithLog).when(perDValidateUC).getPersistenceDocumentWithLog(any(TdcDocument.class),
                any(TdcDocumentsFile.class));
        doNothing().when(persistenceQueueUseCase).sendMessagesToQueue(any(TdcDocument.class));

        PersistenceDocumentList persistenceDocumentList =
                PersistenceDocumentList.builder().status("asd").errorDescription("1433").build();
        doReturn(persistenceDocumentList).when(persistenceDocumentUseCase)
                .processDocumentResponse(any(PersistenceDocument.class),any(TdcDocument.class),
                        any(TdcDocumentsFile.class),anyString());
        doReturn(persistenceDocumentList).when(persistenceDocumentUseCase)
                .mapperObjectPersistenceDoc(any(PersistenceDocumentList.class), any(TdcDocumentsFile.class),
                        anyString());

        PersistenceDocument persistenceDocument1 = persistenceDocumentUseCase.getListDocumentsR(tdcDocument, user,
                false, dFiles);
        assertNotNull(persistenceDocument1);
    }

    @Test
    public void processDocumentResponseTest(){
        String user = "userTest";
        List<TdcDocumentsFile> dFiles = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocumentsFile tdcDocumentsFileRut = TdcDocumentsFile.builder().documentalSubTypeCode("002")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        dFiles.add(0, tdcDocumentsFileCC);
        dFiles.add(1, tdcDocumentsFileRut);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(dFiles).build();
        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument("STG-1234").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse).infoReuseCommon(infoReuseCommon).build();

        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().errorDescription("NA")
                .errorCode("1433").build();
        doReturn(persistenceDocumentList).when(persistenceDocumentUseCase).save(any(PersistenceDocumentList.class));

        PersistenceDocumentList result = persistenceDocumentUseCase.processDocumentResponse(persistenceDocumentWithLog,
                tdcDocument, tdcDocumentsFileCC, user);
        assertNotNull(result);
    }

    @Test
    public void processDocumentResponseNullIdDocumentTest(){
        String user = "userTest";
        List<TdcDocumentsFile> dFiles = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocumentsFile tdcDocumentsFileRut = TdcDocumentsFile.builder().documentalSubTypeCode("002")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        dFiles.add(0, tdcDocumentsFileCC);
        dFiles.add(1, tdcDocumentsFileRut);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(dFiles).build();
        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument(null).build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).build();
        PersistenceDocumentError persistencedocumentError = PersistenceDocumentError.builder().code(null)
                .description("asd").build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse)
                .persistenceDocumentError(persistencedocumentError)
                .infoReuseCommon(infoReuseCommon).build();
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().errorDescription("NA")
                .errorCode("1433").build();
        doReturn(persistenceDocumentList).when(persistenceDocumentUseCase).save(any(PersistenceDocumentList.class));

        PersistenceDocumentList result = persistenceDocumentUseCase.processDocumentResponse(persistenceDocumentWithLog,
                tdcDocument, tdcDocumentsFileCC, user);
        assertNotNull(result);
    }

    @Test
    public void processDocumentResponseDocumentErrorTest(){
        String user = "userTest";
        List<TdcDocumentsFile> dFiles = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(1, "NameSecond.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder().documentalSubTypeCode("001")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        TdcDocumentsFile tdcDocumentsFileRut = TdcDocumentsFile.builder().documentalSubTypeCode("002")
                .documentalTypeCode("01").fileNames(fileNameList).build();
        dFiles.add(0, tdcDocumentsFileCC);
        dFiles.add(1, tdcDocumentsFileRut);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(dFiles).build();
        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder()
                .idDocument(null).build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).build();
        PersistenceDocumentError persistencedocumentError = PersistenceDocumentError.builder().code("asd")
                .description("asd").build();
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentResponse(persistenceDocumentResponse)
                .persistenceDocumentError(persistencedocumentError)
                .infoReuseCommon(infoReuseCommon).build();
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().errorDescription("NA")
                .errorCode("1433").build();
        doReturn(persistenceDocumentList).when(persistenceDocumentUseCase).save(any(PersistenceDocumentList.class));

        PersistenceDocumentList result = persistenceDocumentUseCase.processDocumentResponse(persistenceDocumentWithLog,
                tdcDocument, tdcDocumentsFileCC, user);
        assertNotNull(result);
    }
}
