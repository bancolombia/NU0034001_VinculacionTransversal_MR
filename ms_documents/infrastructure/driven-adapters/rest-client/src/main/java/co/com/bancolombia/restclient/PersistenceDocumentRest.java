package co.com.bancolombia.restclient;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentError;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentResponse;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.model.persistencedocument.gateways.DocumentPersistenceRestRepository;
import com.grupobancolombia.ents.soi.coreextensions.v2_1.Classifications;
import com.grupobancolombia.ents.soi.coreextensions.v2_1.Destination;
import com.grupobancolombia.ents.soi.coreextensions.v2_1.MessageContext;
import com.grupobancolombia.ents.soi.coreextensions.v2_1.Property;
import com.grupobancolombia.ents.soi.coreextensions.v2_1.UsernameToken;
import com.grupobancolombia.ents.soi.messageformat.v2_1.RequestHeader;
import com.grupobancolombia.ents.soi.messageformat.v2_1.RequestHeaderE;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.enlace.v1_0.GestionInternaDocumentalStub;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.enlace.v1_0.SystemExceptionMsg;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.Autor_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.CodigoSubSerie_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.CuentaSeguridad_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.GrupoSeguridad_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.IdPerfil_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.MetaData;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.NombreArchivo_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.Nombre_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.NumeroDocumentoIdentificacion_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.SubTipoDocumental_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.TipoDocumental_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.TipoDocumentoIdentificacion_type1;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.TiposMetaData;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.TransmitirDocumento;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.TransmitirDocumentoE;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.TransmitirDocumentoResponseE;
import com.grupobancolombia.intf.corporativo.administraciondocumentos.gestioninternadocumental.v1_0.Valor_type1;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.util.constants.Constants.MSG_PERSISTENCE_TRANSMIT_DOCUMENT;

@Component
public class PersistenceDocumentRest implements DocumentPersistenceRestRepository {

    private ProtocolSocketFactory protocolSocketFactory;
    private ExecutorService executor;
    private CoreFunctionDate coreFunctionDate;

    @Value("${baseUrlSOAP.TDC}")
    private String BASE_URL_TDC;

    public PersistenceDocumentRest(ProtocolSocketFactory protocolSocketFactory, CoreFunctionDate coreFunctionDate) {
        this.protocolSocketFactory = protocolSocketFactory;
        executor = Executors.newSingleThreadExecutor();
        this.coreFunctionDate = coreFunctionDate;
    }


    LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE, PersistenceDocumentRest.class.getName());


    private RequestHeaderE constructHeader(String messageId) throws URI.MalformedURIException {
        RequestHeaderE requestHeaderE = new RequestHeaderE();
        RequestHeader requestHeader = new RequestHeader();
        Destination destination = new Destination();
        Classifications classifications = new Classifications();
        UsernameToken usernameToken = new UsernameToken();
        MessageContext messageContext = new MessageContext();
        Property property = new Property();
        destination.setName("GestionInternaDocumental");
        destination.setOperation("transmitirDocumento");
        destination.setNamespace(new URI("http://grupobancolombia.com/intf/Corporativo/administracionDocumentos/GestionInternaDocumental/Enlace/V1.0"));
        classifications.addClassification(new URI("http://grupobancolombia.com/clas/AplicacionesActuales"));
        usernameToken.setUserName("userToken");
        usernameToken.setUserToken("1234567");
        property.setKey("");
        property.setValue("");
        messageContext.addProperty(property);
        requestHeader.setMessageContext(messageContext);
        requestHeader.setUserId(usernameToken);
        requestHeader.setSystemId("NU0034001");
        requestHeader.setMessageId(messageId);
        Calendar createDate = Calendar.getInstance();
        requestHeader.setTimestamp(createDate);
        requestHeader.setDestination(destination);
        requestHeader.setClassifications(classifications);
        requestHeaderE.setRequestHeader(requestHeader);
        return requestHeaderE;
    }

    private Map<String, String> valuesForDocumentalSubTypes(String documentalSubtype) {
        Map<String, String> values = new HashMap<>();
        if (documentalSubtype.equals("001")) {
            values.put("CuentaSeguridad", "Estudio/Reusables");
            values.put("SubtipoDocumental", "Cedula de ciudadania");
            values.put("TipoDocumentoIdentificacion", "CC");
        } else {
            values.put("CuentaSeguridad", "Cliente/Cumplimiento");
            values.put("SubtipoDocumental", "Rut");
            values.put("TipoDocumentoIdentificacion", "RUT");
        }
        return values;
    }

    private TransmitirDocumentoE constructRequestSOAP(TdcDocument tdcDocument, TdcDocumentsFile tdcDocumentsFile,
                                                      String base64, String rutExpeditionDate) {
        TransmitirDocumentoE transmitirDocumentoE = new TransmitirDocumentoE();
        TransmitirDocumento transmitirDocumento = new TransmitirDocumento();
        MetaData metaData = new MetaData();
        TipoDocumental_type1 tipoDocumental_type1 = new TipoDocumental_type1();
        Autor_type1 autor_type1 = new Autor_type1();
        GrupoSeguridad_type1 grupoSeguridad_type1 = new GrupoSeguridad_type1();
        CuentaSeguridad_type1 cuentaSeguridad_type1 = new CuentaSeguridad_type1();
        SubTipoDocumental_type1 subTipoDocumental_type1 = new SubTipoDocumental_type1();
        TipoDocumentoIdentificacion_type1 tipoDocumentoIdentificacion_type1 = new TipoDocumentoIdentificacion_type1();
        NumeroDocumentoIdentificacion_type1 numeroDocumentoIdentificacion_type1 = new NumeroDocumentoIdentificacion_type1();
        CodigoSubSerie_type1 codigoSubSerie_type1 = new CodigoSubSerie_type1();
        IdPerfil_type1 idPerfil_type1 = new IdPerfil_type1();
        NombreArchivo_type1 nombreArchivo_type1 = new NombreArchivo_type1();
        byte[] arrayBytes = Base64.getDecoder().decode(base64);
        DataSource ds = new ByteArrayDataSource(arrayBytes, "application/pdf");
        DataHandler result = new DataHandler(ds);
        tipoDocumental_type1.setTipoDocumental_type0("DocumentosIdentidad");
        autor_type1.setAutor_type0("VinculacionTransversal");
        grupoSeguridad_type1.setGrupoSeguridad_type0("Conocimiento");
        cuentaSeguridad_type1.setCuentaSeguridad_type0(valuesForDocumentalSubTypes
                (tdcDocumentsFile.getDocumentalSubTypeCode()).get("CuentaSeguridad"));
        subTipoDocumental_type1.setSubTipoDocumental_type0(valuesForDocumentalSubTypes
                (tdcDocumentsFile.getDocumentalSubTypeCode()).get("SubtipoDocumental"));
        tipoDocumentoIdentificacion_type1.setTipoDocumentoIdentificacion_type0(valuesForDocumentalSubTypes
                (tdcDocumentsFile.getDocumentalSubTypeCode()).get("TipoDocumentoIdentificacion"));
        numeroDocumentoIdentificacion_type1.setNumeroDocumentoIdentificacion_type0(tdcDocument.getDocumentNumber());
        codigoSubSerie_type1.setCodigoSubSerie_type0("668.1");
        idPerfil_type1.setIdPerfil_type0("VincActNovClientes");
        nombreArchivo_type1.setNombreArchivo_type0(tdcDocumentsFile.getFileNames().get(0));
        TiposMetaData tiposMetaData = new TiposMetaData();
        Nombre_type1 nombre_type1 = new Nombre_type1();
        Valor_type1 valor_type1 = new Valor_type1();
        nombre_type1.setNombre_type0("xFechaExpedicionDocumento");
        valor_type1.setValor_type0("");
        tiposMetaData.setNombre(nombre_type1);
        tiposMetaData.setValor(valor_type1);
        metaData.addTiposMetaData(tiposMetaData);
        transmitirDocumento.setTipoDocumental(tipoDocumental_type1);
        transmitirDocumento.setAutor(autor_type1);
        transmitirDocumento.setGrupoSeguridad(grupoSeguridad_type1);
        transmitirDocumento.setCuentaSeguridad(cuentaSeguridad_type1);
        transmitirDocumento.setSubTipoDocumental(subTipoDocumental_type1);
        transmitirDocumento.setTipoDocumentoIdentificacion(tipoDocumentoIdentificacion_type1);
        transmitirDocumento.setNumeroDocumentoIdentificacion(numeroDocumentoIdentificacion_type1);
        transmitirDocumento.setCodigoSubSerie(codigoSubSerie_type1);
        transmitirDocumento.setIdPerfil(idPerfil_type1);
        transmitirDocumento.setNombreArchivo(nombreArchivo_type1);
        transmitirDocumento.setArchivo(result);
        transmitirDocumento.setMetaData(metaData);
        transmitirDocumentoE.setTransmitirDocumento(transmitirDocumento);
        return transmitirDocumentoE;
    }

    @Override
    public Future<PersistenceDocument> getPersistence(TdcDocument tdcDocument, TdcDocumentsFile tdcDocumentsFile,
                                                      String base64, String rutExpeditionDate) {
        Date dateRequestReuse = coreFunctionDate.getDatetime();
        PersistenceDocumentResponse persistenceDocumentResponse = PersistenceDocumentResponse.builder().build();
        PersistenceDocumentError persistenceDocumentError = PersistenceDocumentError.builder().build();
        return executor.submit(() -> {
            try {
                GestionInternaDocumentalStub gestionInternaDocumental = new GestionInternaDocumentalStub(BASE_URL_TDC);
                gestionInternaDocumental._getServiceClient().getOptions().setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER,
                        new Protocol("https", this.protocolSocketFactory, 443));
                TransmitirDocumentoResponseE transmitirDocumentoResponseE = gestionInternaDocumental
                        .transmitirDocumento(constructRequestSOAP(tdcDocument, tdcDocumentsFile, base64, rutExpeditionDate),
                                constructHeader(tdcDocument.getMessageId()));
                if (transmitirDocumentoResponseE.getTransmitirDocumentoResponse().getIdDocumento() != null) {
                    persistenceDocumentResponse.setIdDocument(transmitirDocumentoResponseE
                            .getTransmitirDocumentoResponse().getIdDocumento());
                }
            } catch (SystemExceptionMsg systemExceptionMsg) {
                if (systemExceptionMsg.getFaultMessage().getSystemException().getGenericException() != null) {
                    persistenceDocumentError.setCode(systemExceptionMsg.getFaultMessage().getSystemException()
                            .getGenericException().getCode());
                    persistenceDocumentError.setDescription(systemExceptionMsg.getFaultMessage().getSystemException()
                            .getGenericException().getDescription());
                }
            } catch (Exception e) {
                adapter.error(MSG_PERSISTENCE_TRANSMIT_DOCUMENT);
            }
            Date dateResponseReuse = coreFunctionDate.getDatetime();
            return PersistenceDocument.builder().persistenceDocumentResponse(persistenceDocumentResponse)
                    .persistenceDocumentError(persistenceDocumentError).infoReuseCommon(InfoReuseCommon.builder()
                            .dateRequestReuse(dateRequestReuse).dateResponseReuse(dateResponseReuse)
                            .build()).build();
        });
    }
}
