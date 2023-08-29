package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.model.persistencedocument.gateways.DocumentPersistenceRestRepository;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.parameters.ParametersUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class PersistenceDocumentValidateUseCaseTest {

    @InjectMocks
    @Spy
    PersistenceDocumentValidateUseCaseImpl persistenceDocumentValidateUseCase;

    @Mock
    private DocumentPersistenceRestRepository documentPersistenceRestRepository;

    @Mock
    private DataFileRepository dataFileRepositoryAdapter;

    @Mock
    private ParametersUseCase parametersUseCase;

    @Mock
    private PersistenceValidateTypeDocument persistenceValidateTypeDocument;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPersistenceDocumentWithLogOneFileTest() throws IOException {
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();

        when(dataFileRepositoryAdapter.getBase64File(anyString(), anyString())).thenThrow(new IOException());

        this.persistenceDocumentValidateUseCase.getPersistenceDocumentWithLog(tdcDocument, tdcDocumentsFileCC);
        Mockito.verify(this.persistenceDocumentValidateUseCase, Mockito.times(1))
                .getPersistenceDocumentWithLog(tdcDocument, tdcDocumentsFileCC);
    }

   @Test
    public void getPersistenceDocumentWithLogTest() throws IOException {
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        fileNameList.add(0, "NameTwo.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("001").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();

        Mockito.doReturn("").when(persistenceValidateTypeDocument).getBase64DiffExtension(any(TdcDocumentsFile.class),
                any(TdcDocument.class));

       Future<PersistenceDocument> future = new CompletableFuture<>();
       doReturn(future).when(documentPersistenceRestRepository).getPersistence(any(TdcDocument.class),
               any(TdcDocumentsFile.class),anyString(),anyString());
       Parameters parameters = Parameters.builder().code("10").build();
       doReturn(Optional.of(parameters)).when(parametersUseCase).findByNameAndParent(anyString(),anyString());

       this.persistenceDocumentValidateUseCase.getPersistenceDocumentWithLog(tdcDocument, tdcDocumentsFileCC);
        Mockito.verify(this.persistenceDocumentValidateUseCase, Mockito.times(1))
                .getPersistenceDocumentWithLog(tdcDocument, tdcDocumentsFileCC);
    }

    @Test
    public void getPersistenceDocumentWithLogTwoTest() throws IOException {
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("001").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();
        Mockito.doReturn("").when(dataFileRepositoryAdapter).getBase64File(anyString(),anyString());

        Future<PersistenceDocument> future = new CompletableFuture<>();
        doReturn(future).when(documentPersistenceRestRepository).getPersistence(any(TdcDocument.class),
                any(TdcDocumentsFile.class),anyString(),anyString());
        Parameters parameters = Parameters.builder().code("10").build();
        doReturn(Optional.of(parameters)).when(parametersUseCase).findByNameAndParent(anyString(),anyString());

        this.persistenceDocumentValidateUseCase.getPersistenceDocumentWithLog(tdcDocument, tdcDocumentsFileCC);
        Mockito.verify(this.persistenceDocumentValidateUseCase, Mockito.times(1))
                .getPersistenceDocumentWithLog(tdcDocument, tdcDocumentsFileCC);
    }
}
