package co.com.bancolombia.documenttype;

import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.model.documenttype.gateways.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class DocumentTypeUseCaseTest {

    @InjectMocks
    @Spy
    DocumentTypeUseCaseImpl documentTypeUseCase;

    @Mock
    DocumentTypeRepository documentTypeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeTest() {

        DocumentType td = DocumentType.builder().active(true).build();

        Mockito.doReturn(Optional.of(td)).when(documentTypeRepository).findByCode(any(String.class));

        Optional<DocumentType> tdd = this.documentTypeUseCase.findByCode("");

        assertNotNull(tdd);

    }

    @Test
    public void validateInactive() {
        DocumentType td = DocumentType.builder().active(false).build();
        this.documentTypeUseCase.validateActive(Optional.of(td));
    }

    @Test
    public void validateNotFound() {
        DocumentType td = null;
        this.documentTypeUseCase.validate("", Optional.ofNullable(td));
    }

    @Test
    public void validate() {
        DocumentType td = DocumentType.builder().active(true).build();
        this.documentTypeUseCase.validate("", Optional.of(td));
    }

    @Test
    public void validateActive() {
        DocumentType td = DocumentType.builder().active(true).build();
        this.documentTypeUseCase.validateActive(Optional.of(td));
    }

}
