package co.com.bancolombia.usecase.util;


import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class UtilCatalogsUseCaseTest {

    @InjectMocks
    @Spy
    UtilCatalogs utilCatalogs;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void valid(){
        assertTrue(utilCatalogs.valid("asd"));
    }

    @Test
    public void callValidateCatalog() {
        doReturn(EmptyReply.builder().build()).when(vinculationUpdateUseCase).validateCatalog(anyMap(), anyMap());
        EmptyReply reply = utilCatalogs
                .callValidateCatalog(new ArrayList<>(), new ArrayList<>(), "asd", "asd");
        assertNotNull(reply);
    }
}