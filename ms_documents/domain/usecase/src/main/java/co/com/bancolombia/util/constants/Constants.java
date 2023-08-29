package co.com.bancolombia.util.constants;

public class Constants {

    /**
     * Upload Document Retries
     */
    public static final String COINCIDENCE_OCR_BCR_DOC_NR = "COINCIDENCE_OCR_BCR_DOC_NR";
    public static final String IDENTITY_RETRIES = "IDENTITY_RETRIES";
    public static final String RUT_RETRIES = "RUT_RETRIES";

    /**
     * Upload Document Fields
     */
    public static final String DOCUMENT_NUMBER = "DOCUMENT_NUMBER";
    public static final String FIRST_NAMES = "FIRST_NAMES";
    public static final String LAST_NAMES = "LAST_NAMES";
    public static final String BIRTH_DATE = "BIRTH_DATE";
    public static final String GENDER = "GENDER";
    public static final String EMISSION_DATE = "EMISSION_DATE";

    /**
     * Upload Document Parameters
     */
    public static final String PARAM_TAX_IDENTIFICATION_NUMBER = "TAX_IDENTIFICATION_NUMBER";
    public static final String PARAM_CORPORATE_NAME = "CORPORATE_NAME";
    public static final String PARAM_FIRST_NAME = "FIRST_NAME";
    public static final String PARAM_SECOND_NAME = "SECOND_NAME";
    public static final String PARAM_FIRST_SURNAME = "FIRST_SURNAME";
    public static final String PARAM_SECOND_SURNAME = "SECOND_SURNAME";
    public static final String PARAM_IDENTIFICATION_TYPE = "IDENTIFICATION_TYPE";
    public static final String PARAM_MAIN_ACTIVITY = "MAIN_ACTIVITY";
    public static final String PARAM_SECONDARY_ACTIVITY = "SECONDARY_ACTIVITY";
    public static final String PARAM_EMISSION_RUT_DATE = "EMISSION_RUT_DATE";
    public static final String PARAM_ASSESSEE_TYPE = "ASSESSEE_TYPE";

    /**
     * Upload Document Identity Fields
     */
    public static final String OCR_DOC_NR = "OCR_docNr";
    public static final String OCR_FIRST_NAMES = "OCR_firstNames";
    public static final String OCR_LAST_NAMES = "OCR_lastNames";
    public static final String OCR_GENDER = "OCR_gender";
    public static final String OCR_BIRTH_DATE = "OCR_birthDate";
    public static final String OCR_BIRTH_PLACE = "OCR_birthPlace";
    public static final String OCR_EMISSION_DATE = "OCR_emissionDate";
    public static final String OCR_EMISSION_PLACE = "OCR_emissionPlace";
    public static final String BCR_DOC_NR = "BC_docNr";
    public static final String BCR_LAST_NAME = "BC_lastNames";
    public static final String BCR_FIRST_NAME = "BC_firstNames";
    public static final String BCR_GENDER = "BC_gender";
    public static final String BCR_BIRTH_DATE = "BC_birthDate";
    public static final String DIFFERENCE_DOC_NR = "Difference_docNr";

    /**
     * Upload Document RUT Fields
     */
    public static final String TAX_IDENTIFICATION_NUMBER = "NumIdentificacionTribu";
    public static final String CORPORATE_NAME = "RazonSocial";
    public static final String FIRST_NAME = "PrimerNombre";
    public static final String SECOND_NAME = "SegundoNombre";
    public static final String FIRST_SURNAME = "PrimerApellido";
    public static final String SECOND_SURNAME = "SegundoApellido";
    public static final String IDENTIFICATION_TYPE = "TipoIdentificacion";
    public static final String MAIN_ACTIVITY = "ActividadPrincipal";
    public static final String SECONDARY_ACTIVITY = "ActividadSecundaria";
    public static final String EMISSION_RUT_DATE = "FechaExpDocumento";
    public static final String ASSESSEE_TYPE = "TipoContribuyente";

    /**
     * Upload Document Properties
     */
    public static final String TYPE_ID_CC = "Cédula";
    public static final String TYPE_RUT = "Rut";
    public static final String CODE_SUCCESS_PROCESS_DOCUMENT = "00";
    public static final String FIRST = "first";
    public static final String SECOND = "second";
    public static final String EXPEDITION_DATE_FIELD = "expeditionDate";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Upload Document Split Names
     */
    public static final String[] UNION = {"DE LA ", "DE LAS ", "DE LOS ", "DEL ", "DE ", "LA ", "SAN "};
    public static final String[] COMPLEMENT_TWO = {"DE LA", "DE LAS", "DE LOS"};
    public static final String[] COMPLEMENT_ONE = {"DEL", "DE", "LA", "SAN"};
    public static final int VAL_NAME_COMPOSE_TWO = 2;

    /**
     * Upload Document Reuse
     */
    public static final String PROCESS_DOCUMENT = "/v1/business-support/document-management/document-management" +
            "/digitalization/digitalization/action/digitize";
    public static final String STATUS_CODE = "STATUS_CODE: ";

    /**
     * Generate PDF Properties
     */
    public static final String FORMATO_PDF = ".pdf";
    public static final String RUTA_ARCHIVO_CUSTODIA = "RUTA_ARCHIVO_CUSTODIA";
    public static final String RUTA_ARCHIVO_CLIENTE = "RUTA_ARCHIVO_CLIENTE";
    public static final String NAME_FILE = "NAME_FILE";
    public static final String TEXT_CUSTODIA = "CUSTODIA";
    public static final String TEXT_COPY_FROM_ORIGIN = "EL PRESENTE DOCUMENTO ES UNA COPIA DEL ORIGINAL";
    public static final String FILE_CUSTODIE = "FILE_CUSTODIE";
    public static final String FILE_CLIENT = "FILE_CLIENT";
    public static final String CODE_TEMPLATE_PDF = "PDF_TEMPLATE";

    /**
     * Generate PDF Errors
     */
    public static final String ERROR_LOAD_TEMPLATE_PDF = "Error cargando la Plantilla del PDF";
    public static final String ERROR_CONFIGURE_CONTENT_STREAM = "Error construyendo la linea del ContentStream";
    public static final String ERROR_READ_JSON = "Error leyendo el Json";
    public static final String ERROR_ENCRYPT_FILE = "Error Aplicando politicas de seguridad";
    public static final String ERROR_SAVE_FILE = "Error Grabando el archivo";
    public static final String ERROR_CLOSE_FILE = "Error Cerrando el archivo";
    public static final String ERROR_CONVERTING_PDFA1B = "Error convirtiendo el archivo a PdfA1B";

    /**
     * Persistence Document Response
     */
    public static final String OUT_COME_CODE_ZERO = "0";
    public static final String OUT_COME_NAME_ZERO = "Documentos guardados exitosamente";

    /**
     * Sign Document Reuse
     */
    public static final String ES_URI = "/v1/business-support/document-management/document-management/digital" +
            "-signature/sign-documents";
    public static final String ES_X_USERNAME = "x_username";
    public static final String ES_X_USERTOKEN = "x_usertoken";
    public static final String ES_X_USERTOKEN_VALUE = "system";

    /**
     * CONSTANTS PERSISTENCE DOCUMENT
     */
    public static final String DOC_PROCESSED_BUCKET_FOLDER = "processed";
    public static final String ERROR_DETAIL_TIMEOUT_PERSISTING_DOCUMENT = "Super\u00F3 tiempo de time out.";
    public static final String MESSAGE_CONSUMING_SOAP_SERVICE = "Error consumiendo el servicio de TDC, se " +
            "interrumpi\u00F3 su ejecuci\u00F3n";
    public static final String ERROR_DETAIL_FETCHING_DOCUMENT = "No se encuentra el documento o no es posible" +
            " recuperarlo del " +
            "repositorio temporal para su custodia definitiva.";
    public static final String NAME_TIMEOUT_PARAMETER ="DOCPERSISTENCE_TIME_OUT";
    public static final String PARENT_DOC_PERSISTENCE ="documentPersistence";
    public static final String DOCUMENTAL_SUBTYPE_CC_CODE = "001";
    public static final String PDF_EXTE = "pdf";
    public static final String PDF_MERGER_UTILITY = "pdf-merger-utility";
    public static final String PDF_MERGER_NAME = "PdfMerger";
    public static final String PDF_ONE_NAME = "file1.pdf";
    public static final String PDF_TWO_NAME = "file2.pdf";
    public static final String PDF_MIXED_NAME = "mixedFile.pdf";
    public static final String DOCUMENTS_BUCKET_FOLDER = "documents";
    public static final String FILE_NOT_FOUND_BUCKET = "Archivo no encontrado en bucket o error en la recuperacion";
    public static final String FILE_CANT_DELETE = "Error eliminando archivos del repositorio";
    public static final String IMAGE_CONVERTER_UTILITY = "image-converter-utility";
    public static final String IMAGE_CONVERTER_NAME = "ImageConverter";
    public static final String ERROR_IMAGE_CONVERSION = "Error convirtiendo imagenes en PDF";
    public static final String NO_APPLY ="NA";
    public static final String TDC_RETRIES = "persistenceDocumentRetries";
    public static final String API_KEY = "apiKey";
    public static final String DOCUMENTS_RETRIES_REST = "DocumentPersistenceRetriesRest";
    public static final String FAIL_DOCUMENTS_CONNECTION = "Connection not established with DOCUMENTS micro";
    public static final String SUCCESFUL_DOCUMENTS_CONNECTION = "Connection established with DOCUMENTS micro";
    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";
    public static final String MSG_PERSISTENCE_TRANSMIT_DOCUMENT = "Error al transmitir el documento a persistir";
    public static final String TYPE_CC = "C\u00e9dula de ciudadan\u00EDa";
    public static final String CODE_RETRIES_DOCUMENT_PERSISTENCE = "RETRIESDOCPERSISTENCE";
}
