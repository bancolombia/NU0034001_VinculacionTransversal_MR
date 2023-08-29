package co.com.bancolombia.commonsvnt.usecase.util.constants;


import java.util.Arrays;
import java.util.List;

public final class Constants {

    /**
     * GENERAL STRINGS
     */
    public static final String META = "meta";
    public static final String EMPTY = "";
    public static final String STRING_EMPTY = "EMPTY";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String CODE_OPERATION = "codeOperation";
    public static final String NAME_OPERATION = "nameOperation";
    public static final String MDM = "MDM";
    public static final String SPACE = " ";
    public static final String POINT = ".";
    public static final String COMMA = ",";
    public static final String DOES_NOT_EXIST = "NO EXISTE";
    public static final String MIDDLE_SCREEN = "-";
    public static final String UNDERSCORE = "_";
    public static final String POSITION_LIST = ". Registro: ";
    public static final String NOT = "NO";
    public static final String YES = "SI";
    public static final Integer ZERO = 0;
    public static final Double DOUBLE_ZERO = 0.0;
    public static final Integer ONE = 1;
    public static final Integer TWO = 2;
    public static final Integer TEN = 10;
    public static final Integer TEWNTY = 20;
    public static final Integer NINENINENINE = 999;
    public static final String MESSAGE_ATTRIBUTE_DATA_TYPE = "String";

    public static final Integer NUMBER_ONE_HUNDRED = 100;
    public static final String CODE = "Code: ";

    public static final String SYSTEM_VTN = "NU0034001";
    public static final String PRODUCT_VTN = "VINCUTRANS";
    public static final String CHANNEL_VNT = "VNT";

    /**
     * LOGS
     */
    public static final String MY_APP = "NU0034001_VinculacionTransversal_Profiling";
    public static final String SERVICE_IDENTITY = "identity";
    public static final String SERVICE_VALIDATION = "validation";
    public static final String SERVICE_MANAGEMENT = "management";
    public static final String SERVICE_INFORMATION = "information";
    public static final String SERVICE_PROFILE = "profile";
    public static final String SERVICE_DIGITALIZATION = "digitalization";
    public static final String SERVICE_GENPDF = "rest-genpdf";
    public static final String SERVICE_PERSISTENCE = "persistence";
    public static final String SERVICE_MONITORING = "monitoring";
    public static final String MESSAGE_CODE = "Error controlado de la aplicacion externa ";
    public static final String MESSAGE_NOT_CONTROL = "Error no controlado de la aplicacion externa";
    public static final String CONSUME_EXTERNAL = "Se procede a consumir el servicio externo";
    public static final String CONSUME_EXTERNAL_RESULT = "Se finaliza el consumo con resultado: ";
    public static final String SAVE_RESULT = "Se realiza almacenamiento de la info: ";
    public static final String STATE_EXTERNAL = "Se realiza la validacion externa dando como resultado: ";
    public static final String NOT_FOUND_FIELD = "Validando no se cuenta con el valor del campo";
    public static final String START_OPERATION = "INICIANDO PROCESAMIENTO OPERACION";
    public static final String FINISH_OPERATION = "FINALIZANDO PROCESAMIENTO OPERACION";
    public static final String CUSTOMER_NOT_FOUND = "CUSTOMER NOT FOUND IN MDM: ";
    public static final String INCONSISTENCY = "THERE IS INCONSISTENCY THROUGH IN THE RULES";
    public static final String MESSAGE_NOT_FOUND_BY_ACQ = "NO SE ENCONTRO NINGUN MENSAJE RELACIONADO A LA " +
            "ACQUISITION y A LA OPERACION";
    public static final String MESSAGE_NOT_FOUND_BY_DOC = "NO SE ENCONTRO NINGUN MENSAJE RELACIONADO AL TIPO Y" +
            " SUBTIPO DOCUMENTAL";
    public static final String STATE = " Estado: ";

    public static final String ERROR_CONSUME_REUSE_SERVICE = "Error consumiendo Servicio de Reuso";


    /**
     * ACQUISITION OPERATION
     */
    public static final String CODE_USER_ACQUISITION_INITIAL = "Usuario1";

    /**
     * ACQUISITION VALIDITY
     */
    public static final String PARAMETER_ACQUISITION_TIME_VALIDITY = "maxAcquisitionMinutes";

    /**
     * NAMES CATALOG START ACQUISITION
     */
    public static final String BUSINESS_LINE = "businessLineId";
    public static final String CHANNEL_TYPE = "channelId";
    public static final String DOCUMENT_TYPE = "documentType";
    public static final String PRODUCT_TYPE = "productId";
    public static final String STATE_ACQUISITION = "StateAcquisition";

    /**
     * PROFILE ACTION CODES
     */
    public static final String OPEN_PRODUCT = "1";
    public static final String UPDATE_CUSTOMER = "2";

    /**
     * NAMES CATALOGS GENERAL
     */
    public static final String RESPONSE_SN = "RESPUESTA_S_N";
    public static final String FOREIGN_INFORMATION = "RESPUE_S";
    public static final String N_FOREIGN_INFORMATION = "RESPUE_N";

    /**
     * FIELDS OF OPERATION PERSONAL INFORMATION
     */
    public static final String EXPEDITION_DATE = "expeditionDate";
    public static final String BIRTHDATE = "birthdate";
    public static final String EXPEDITION_COUNTRY = "expeditionCountry";
    public static final String EXPEDITION_DEPARTMENT = "expeditionDepartment";
    public static final String EXPEDITION_PLACE = "expeditionPlace";
    public static final String FIRST_NAME = "firstName";
    public static final String SECOND_NAME = "secondName";
    public static final String FIRST_SURNAME = "firstSurname";
    public static final String SECOND_SURNAME = "secondSurname";

    /**
     * NAMES CATALOGS BASIC INFORMATION
     */
    public static final String GENDER = "GENERO";
    public static final String CIVIL_STATUS = "ESTCIVIL";
    public static final String EDUCATION_LEVEL = "NIVACADEMI";
    public static final String SOCIAL_STRATUM = "ESTRATO";
    public static final String HOUSING_TYPE = "TIPVIVIENDA";
    public static final String CONTRACT_TYPE = "TIPCONTRATO";
    public static final String NATIONALITY = "NACIONALIDAD";

    /**
     * FIELDS OF OPERATION BASIC INFORMATION
     */
    public static final String F_GENDER = "gender";
    public static final String F_CIVIL_STATUS = "civilStatus";
    public static final String F_EDUCATION_LEVEL = "educationLevel";
    public static final String F_SOCIAL_STRATUM = "socialStratum";
    public static final String F_HOUSING_TYPE = "housingType";
    public static final String F_CONTRACT_TYPE = "contractType";
    public static final String F_PEP = "pep";
    public static final String F_NATIONALITY = "nationality";

    public static final String BIRTH_CITY = "birthCity";
    public static final String BIRTH_DEPARTMENT = "birthDepartment";

    /**
     * NAMES CATALOGS CONTACT INFORMATION
     */
    public static final String ADDRESS_TYPE = "CLASEDIRECC";
    public static final String BRAND = "MARCACORRES";

    /**
     * FIELDS OF OPERATION CONTACT INFORMATION
     */
    public static final String F_ADDRESS_TYPE = "addressType";
    public static final String F_BRAND = "brand";

    /**
     * NAMES CATALOGS ECONOMIC INFORMATION
     */
    public static final String PROFESSION = "PROFESION";
    public static final String POSITION_TRADE = "CARGO";
    public static final String OCCUPATION = "OCUPACION";
    public static final String CIIU = "CIIU";
    public static final String CURRENCY = "MONEDA";
    public static final String RUT = "RESPUESTA_S_N";

    /**
     * FIELDS OF OPERATION ECONOMIC INFORMATION
     */
    public static final String F_PROFESION = "profession";
    public static final String F_POSITION_TRADE = "positionTrade";
    public static final String F_OCCUPATION = "occupation";
    public static final String F_CIIU = "ciiu";
    public static final String F_CURRENCY = "currency";
    public static final String F_RUT = "rut";

    /**
     * NAMES CATALOGS FOREING INFORMATION
     */
    public static final String CURRENCY_TRANSACTION_TYPE = "TIPOPEMODEXT";
    public static final String CURRENCY_TRANSACTION_PRODUCT = "TIPOPRODUCTO";
    public static final String MONEDA = "MONEDA";

    /**
     * FIELDS CATALOGS FOREING INFORMATION
     */
    public static final String CURRENCY_TRANSACTION = "foreignCurrencyTransactions";
    public static final String F_CURRENCY_TRANSACTION_TYPE = "foreignCurrencyTransactionType";
    public static final String FOREIGN_INFORMATION_OP = "Foreing currency information";
    public static final String TRANSACTION_PRODUCT = "productType";
    public static final String NOTHING_PRODUCT = "TIPOPE_008";

    /**
     * NAMES CATALOGS TAX INFORMATION
     */
    public static final String VREGIME = "REGIMENIVA";
    public static final String TAX_COUNTRY = "PAISRESIFISCAL";

    /**
     * FIELDS CATALOGS TAX INFORMATION
     */
    public static final String D_INCOME = "declaringIncome";
    public static final String WH_AGENT = "withHoldingAgent";
    public static final String F_VREGIME = "vatRegime";
    public static final String R_TAX_US_TAX = "requiredToTaxUsTax";
    public static final String B_TAX_PAYMENT = "businessTaxPayment";
    public static final String SS_PAYMENT = "socialSecurityPayment";
    public static final String D_TAX_ANOTHER_COUNTRY = "declareTaxInAnotherCountry";
    public static final String S_CITY_R = "sourceCityResource";
    public static final String S_COUNTRY_R = "sourceCountryResource";
    public static final String TAX_COUNTRY_IDENTIFIER = "identifier";

    /**
     * FIELDS OF COMMON INFORMATION
     */
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String EMAIL = "email";
    public static final String CELLPHONE = "cellphone";
    public static final String DEPARTMENT = "department";

    /**
     * CODE OF ACTION TO REVOKE INSTRUCTIONS
     */
    public static final String UNCHECK_REVOCATION = "AC001";
    public static final String CHECK_AUTORIZATION = "AC002";
    public static final String CHECK_REVOCATION = "AC003";
    public static final String UNCHECK_AUTORIZATION = "AC004";

    /**
     * CODE OF STEPS
     */
    public static final String CODE_ECONOMIC_INFO = "ECONINFO";
    public static final String CODE_BASIC_INFO = "BASCINFO";
    public static final String CODE_CONTACT_INFO = "CONTINFO";
    public static final String CODE_PERSONAL_INFO = "PERSINFO";
    public static final String CODE_ACCEPT_CLAUSES = "ACCPTCLA";
    public static final String CODE_START_ACQUISITION = "STARTACQ";
    public static final String CODE_START_UPDATE = "STARTUPD";
    public static final String CODE_TAX_INFO = "TAXEINFO";
    public static final String CODE_TAX_COUNTRY = "TAXCOUNT";
    public static final String CODE_FOREIGN_INFORMATION = "FORGINFO";
    public static final String CODE_FOREIGN_CURRENCY = "FORGCURR";
    public static final String CODE_CONSULT_INSTRUCTIONS = "CONSUINT";
    public static final String CODE_CONTROL_LIST = "VALCTRLIST";
    public static final String CODE_VALIDATE_STATUS = "VALIDSTA";
    public static final String CODE_GENERATE_TOKEN = "GENTOKEN";
    public static final String CODE_VALIDATE_TOKEN = "VALTOKEN";
    public static final String CODE_VALIDATE_IDENTITY = "VALIDENT";
    public static final String CODE_VALIDATE_EXPQUESTIONS = "EXPQUESTIONS";
    public static final String CODE_VALIDATE_VALIDATEQUESTIONS = "VALQUESTIONS";
    public static final String CODE_PROFILE_CUSTOMER = "PROFCUST";
    public static final String CODE_SEGMENT_CUSTOMER = "SEGCUSTOMER";
    public static final String CODE_CUSTOMER_PERSISTENCE = "CUSTOMERPERSISTENCE";
    public static final String CODE_GEN_EXP_DOCS = "GENEXPDOCUMENT";
    public static final String CODE_PRELOAD_INFORMATION = "PRLDINFO";
    public static final String CODE_PROCESS_DOCUMENTS = "PROCESSDOCUMENTS";
    public static final String CODE_PROCESS_ADDITIONALDOCUMENTS = "PRODOCADD";
    public static final String CODE_SIGNDOCUMENT = "SIGNDOCUMENT";
    public static final String CODE_SENDFORM = "SENDFORM";
    public static final String CODE_VAL_DATA_EXTRAC = "VALDATAEXTRAC";
    public static final String CODE_CUSTOMER_DOCUMENT_PERSISTENCE = "CUSTOMERDOCPERSISTENCE";
    public static final String CODE_VALIDATE_LEGAL_REP = "VALLEGALREP";
    public static final String CODE_RETRIEVE_SCENARIO = "RETRISCENARIO";
    public static final String CODE_RETRIEVE_COMPLEMENTARY_FIELDS = "RETRICOMPLEMENTFIELDS";

    /**
     * CODE STATE OF STEPS - OPERATION
     */

    public static final String CODE_ST_OPE_PENDIENTE = "1";
    public static final String CODE_ST_OPE_COMPLETADO = "2";
    public static final String CODE_ST_OPE_RECHAZADO = "3";
    public static final String CODE_ST_OPE_ESCALADO = "4";
    public static final String CODE_ST_OPE_BLOQUEADO = "5";
    public static final String CODE_ST_OPE_COMPLETADO_AUTO = "6";
    public static final String CODE_ST_OPE_COMPLETADO_PARCIAL = "7";

    /**
     * DATE FORMATS
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_TIME = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_TIME_MILLISECONDS = "yyyy-MM-dd'T'HH:mm:ss.sss";
    public static final String TIMESTAMP_WITH_MILI_NUMERIC = "ddMMyyyyHHmmssSSS";

    /**
     * CODES AND STATUS RESPONSE
     */
    public static final String CODE_OK = "200";
    public static final String CODE_NOT_FOUND = "404";
    public static final String STATUS_OK = "Status OK";
    public static final String STATUS_INTERNAL_SERVER_ERROR = "500";

    /**
     * DOCUMENT TYPES
     */
    public static final String DOCUMENT_TYPE_CC = "TIPDOC_FS001";
    public static final String DOCUMENT_TYPE_NIT = "TIPDOC_FS003";

    /**
     * API MDM - CUSTOMER PERSONAL DATA
     */
    public static final String BASIC_INFO_MDM_URI = "/v1/sales-services/customer-management/customer-reference-data-"
            + "management/customer-personal-data/retrieve-basic-information";
    public static final String CLIENT_ID_KEY = "X-IBM-Client-Id";
    public static final String CLIENT_SECRET_KEY = "X-IBM-Client-Secret";
    public static final String BANK_HEADER = "application/vnd.bancolombia.v4+json";
    public static final String MESSAGE_ID = "message-id";
    public static final String NAME_BASIC_INFO_MDM_OPE = "profile-customer";

    /**
     * TYPE ADDRESS STRINGS
     */
    public static final String CO_ADDRESS_TYPE_RES = "CLASED_Z001";
    public static final String CO_BRAND_TYPE_RES = "MARCAC_XXDEFAULT";
    public static final String CONCAT_TYPE_ADDRESS_I = " [TYPE ADRESS: ";
    public static final String CONCAT_TYPE_ADDRESS_F = "]";
    public static final String CO_ADDRESS_TYPE_WORK = "CLASED_Z002";

    /**
     * LOGIC COMPARISONS
     */
    public static final String HIGHER = ">";
    public static final String LESS = "<";
    public static final String LESS_OR_EQUAL = "<=";
    public static final String HIGHER_OR_EQUAL = ">=";
    public static final String EQUAL = "==";

    /**
     * PARAMETERS MAX REQUIRE
     */
    public static final String PARAMETER_EMAIL = "maxEmail";
    public static final String PARAMETER_CELLPHONE = "maxCellphone";
    public static final String PARAMETER_COUNTRY = "countryResidence";
    public static final String PARAMETER_VAL_TOKEN_TIME_BLOCK = "validateTokenTimeBlock";
    public static final String PARAMETER_VAL_TOKEN_MAX_RETRIES = "validateTokenMaxRetries";
    public static final String PARAMETER_GEN_TOKEN_MAX_RETRIES = "generateTokenMaxRetries";
    public static final String PARAMETER_GEN_TOKEN_TIME_BLOCK = "generateTokenTimeBlock";
    public static final String PARAMETER_SIGN_DOC_RETRIES = "signDocumentRetries";
    public static final String PARAMETER_SEND_FORM_RETRIES = "sendFormRetries";
    public static final String PARAMETER_REQUIRED_RUT_OCCUPATION = "requiredRutOcupation";

    /**
     * LISTS VALIDATIONS FIELDS REQUEST
     */
    public static final List<String> STRINGS_EMAIL_ERROR = Arrays.asList("sininformacion", "noinforma", "sincorreo");

    public static final List<String> STRINGS_ADDRESS_ERROR = Arrays.asList("SININFORMACION", "SINDIRECCION",
            "NOREGISTRA");

    /**
     * NAME OPERATIONS
     */
    public static final String OPER_CONTACT_INF = "contact-information";
    public static final String OPER_ECONOMIC_INF = "economic-information";
    public static final String OPER_TAX_INF = "tax-information";
    public static final String OPER_TAX_COUNTRY_INF = "tax-country";
    public static final String OPER_FOREIGN_INF = "foreign-information";
    public static final String OPER_FOREIGN_CURR = "foreign-currency";
    public static final String OPER_G_TOKEN_OPERATION = "generate-token";
    public static final String OPER_TOKEN_OPERATION = "validate-token";
    public static final String OPER_VALIDATE_IDENTITY = "validate-identity";
    public static final String OPER_SEGMENT_CUSTOMER = "segment-customer";
    public static final String OPER_CUSTOMER_PERSISTENCE = "customer-persistence";
    public static final String OPER_EXPOSE_DOCS = "generate-expose-documents";
    public static final String OPER_SIGN_DOCUMENT = "sign-document";
    public static final String OPER_SEND_FORM = "send-form";
    public static final String OPER_EXPO_QUESTIONS = "expo-questions";
    public static final String OPER_EXPO_QUESTIONS_I = "expo-questions-identification";
    public static final String OPER_EXPO_QUESTIONS_Q = "expo-questions-questionnaire";
    public static final String OPER_VALIDATE_QUESTIONS = "validate-questions";
    public static final String OPER_UPLOAD_DOCUMENT = "upload-document";
    public static final String OPER_GET_DOCIDENTITYDATA = "get-extdata";
    public static final String OPER_PROCESS_ADDITIONALDOCUMENTS = "process-docadd";
    public static final String OPER_GET_ADDITIONALDOCUMENTSDATA = "get-extdataadd";
    public static final String OPER_MARK_CUSTOMER = "mark-customer";
    public static final String OPER_START_ACQUISITION = "start-acquisition";
    public static final String OPER_VALIDATE_DATA_EXTRACTION = "validate-data-extraction";
    public static final String OPER_START_UPDATE = "start-update";
    public static final String OPER_CUSTOMER_DOC_PERSISTENCE = "customer-document-persistence";
    public static final String OPER_VALIDATE_LEGAL_REP = "validate-legal-representative";
    public static final String OPER_RETRIEVE_SCENARIO = "retrieve-scenario";
    /**
     * VALUES NULLABLES
     */

    public static final String C_NOT_NULL = "NOT NULL";
    public static final String C_NULL = "NULL";

    /*
     * CONTROL_LIST OPERATIONS
     *
     */
    public static final String NAME_CONTROL_LIST_OPE = "control-list";

    public static final String BASIC_CONTROL_LIST_URI = "/v2/risk-compliance/models/customer-behavior/"
            + "customer/actions/check-control-list";
    public static final String MESSAGE_ID_CONTROL_LIST = "messageId";
    public static final String CTR_LIST_VIGENTE = "Vigente";
    public static final String CTR_LIST_CTR_ALERT = "Alerta";
    public static final String CTR_LIST_STR_BLOQ = "Bloqueo";
    public static final String CTR_LIST_MESSAGE_CONTINUE_ALERT_BLOQ = "Potencial cliente con {0} "
            + "est\u00E1 en listas de control [Estado: {1}], " + "puede continuar con el proceso de vinculaci\u00F3n";
    public static final String CTR_LIST_MESSAGE_NOT_IN = "Potencial cliente no est\u00E1 en listas de control, "
            + "puede continuar con el proceso de vinculaci\u00F3n";

    public static final String CTR_LIST_CODE_TWO_ALERT = "2";
    public static final String CTR_LIST_CODE_THREE_BLOQ = "3";
    public static final String CTR_LIST_CODE_SIX_NOT_IN = "6";

    /***** CODE OPERATION-STEP *******
     /* // PerfilarVinculaci?n (Iniciar Vinculaci?n) *
     public static final String OPE_STARTACQ_VALUE = "STARTACQ";
     /* // Re-uso de informaci?n (Consultar Estado Solicitud) *
     public static final String OPE_VALIDSTA_VALUE = "VALIDSTA";
     /* // ConsultaInstrucciones (Consultar Instrucciones Proceso) *
     public static final String OPE_CONSUINT_VALUE = "CONSUINT";
     /* // AceptarClausulas (Aceptar Clausulas) *
     public static final String OPE_ACCPTCLA_VALUE = "ACCPTCLA";
     /* // SolicitarDatosB?sicos ( Informaci?n Personal) *
     public static final String OPE_PERSINFO_VALUE = "PERSINFO";

     /* // Validaci?nListasDeControl (Validar Cliente Listas de control) *
     public static final String OPE_VALCTRLIST_VALUE = "VALCTRLIST";
     /* // Validaci?nListasDeControl (Validar Cliente Listas de control - REUSO) *
     public static final String OPE_VALCTRLIST_VALUE_REUSE = "VALCTRLIST-REUSE";

     /* // PefilarCliente (Validar existencia cliente en el Maestro MDM) *
     public static final String OPE_PROFCUST_VALUE = "PROFCUST";
     /* // Validaci?nListasDeControl (Validar Cliente Listas de control - REUSO) *
     public static final String OPE_PROFCUST_VALUE_REUSE = "VALIDENT";

     /* // PrecargaInformaci?nFuentesExternasInternas(Precargar) *
     public static final String OPE_PRLDINFO_VALUE = "PRLDINFO";
     /* // Informaci?n B?sica *
     public static final String OPE_BASCINFO_VALUE = "BASCINFO";
     /* // Informaci?n Contactos *
     public static final String OPE_CONTINFO_VALUE = "CONTINFO";
     /* // Informaci?n Econ?mica *
     public static final String OPE_ECONINFO_VALUE = "ECONINFO";
     /* // Informaci?n tributaria *
     public static final String OPE_TAXEINFO_VALUE = "TAXEINFO";
     /* // Informaci?n tributaria paises *
     public static final String OPE_TAXCOUNT_VALUE = "TAXCOUNT";
     /* // Informaci?n Moneda extranjera *
     public static final String OPE_FORGINFO_VALUE = "FORGINFO";
     /* // Informaci?n Moneda extranjera2 *
     public static final String OPE_FORGCURR_VALUE = "FORGCURR";
     /* // Informaci?n para exponer preguntas cliente */
    public static final String OPE_EXPQUESTIONS_VALUE = "EXPQUESTIONS";
    /* // Informaci?n para marcar cliente no objetivo */
    public static final String OPE_MARKCUSTOMER_VALUE = "MARKCUSTOMER";

    /****** APIS - NAME ***********/
    public static final String API_CUSTOMER_VALUE = "customer-operation";

    /****** APIS - NAME ***********/
    public static final String API_DOCUMENTATION_VALUE = "documentation-operation";

    /*
     * GENERATE TOKEN OPERATIONS
     */
    public static final String GENERATE_TOKEN_URI = "/v1/reference-data/party/party-data-management/"
            + "tokens/actions/generate/send-to-email-and-phone";
    public static final String ACCEPT_TOKEN_GENERATE = "application/vnd.bancolombia.v1+json";
    public static final String CONTENT_TYPE_TOKEN_GENERATE = "application/vnd.bancolombia.v2+json";


    public static final String SEND_TOKEN = "3";
    public static final String SERVER_ID_TOKEN = "OTP";
    public static final String ENCRYPTED_TOKEN = "0";
    public static final String VALIDITY_TOKEN = "0000";
    public static final String ACCOUNT_NUMBER = "0";
    public static final String ACCOUNT_TYPE = "1";
    public static final String TEMPLATE_CODE = "0000";


    public static final String VALIDATE_TOKEN_URI = "/v1/reference-data/party/party-data-management/tokens/actions/" +
            "validate/account-information";

    public static final String COMMERCIAL_INFO_MDM_URI = "v1/sales-services/customer-management/" +
            "Customer-Relationship-Management/customer-commercial-data/customers/retrieve-commercial-management";


    public static final Integer READ_TIMEOUT_HANDLER = 100_000;
    public static final Integer WRITE_TIMEOUT_HANDLER = 10_000;
    public static final Integer CONNECT_TIMEOUT_MILLIS_VALUE = 10_000;

    public static final String SERVER_ID = "OTP";
    public static final String SOURCE_SYSTEM_ID = "VNT";
    public static final String TOKEN_ENCRYPTION = "0";
    public static final String TOKEN_VALIDITY = "0000";
    public static final String CODE_PIN_EXPIRED = "BP0116";
    public static final String CODE_INTERNAL_SERVER_ERROR = "SA500";

    /*
     * VALIDATION IDENTIFICATION GENERAL
     */
    public static final String ELEMENT_FOUND_NAME = "Nombre";
    public static final String ELEMENT_FOUND_SURNAME = "Primer Apellido";
    public static final String ELEMENT_FOUND_DATE_EXP = "Fecha de expedicion";

    /*
     * VALIDATE_IDENTITY OPERATIONS
     *
     */
    public static final String VALIDATE_IDENTITY_URI = "/v1/reference-data/party/customer-profile-information/"
            + "customers/";
    public static final String PARAM_FIRST_SURNAME = "firstSurname";

    public static final String NAME_SERVICE_VALIDATE_IDENTITY = "Private External Sources";

    public static final String REGLA01 = "RuleOneCellphone";
    public static final String REGLA02 = "RuleTwoEmail";
    public static final String REGLA03 = "RuleThreeAge";
    public static final String REGLA04 = "RuleFourDateExpedition";
    public static final String REGLA05 = "RuleFiveFullName";
    public static final String REGLA06 = "RuleSixSecondSurname";
    public static final String REGLA07 = "RuleSevenWorkPhone";
    public static final String REGLA08 = "RuleEightWorkCellphone";
    public static final String REGLA09 = "RuleNineWorkEmail";
    public static final String REGLA10 = "RuleTenNames";

    public static final String CODE_NOT_VIGENTE = "00";
    public static final String PARENT_VIGENT_IDENTITY = "PARENT_VIGENT_IDENTITY";
    public static final Double DOUBLE_CERO_INIT = 0.0;
    public static final String PARENT_VALIDATE_IDENTITY = "validateIdentity";
    /* 
     * SI ES 0 NO VALIDA CARGA MANUAL
     * SI ES 1 SE VALIDA CONTRA LA CARGA MANUAL
     */
    public static final String NAME_VAL_IDENTITY_UPD_MANUAL = "VALIDATE_UPLOAD_MANUAL";
    public static final String VALIDATE_MATCH_EMAIL_CELL = "VALIDATE_MATCH_EMAIL_CELL";

    
    
    public static final String THRESHOLD_MIN = "THRESHOLD_MIN";
    public static final String THRESHOLD_MAX = "THRESHOLD_MAX";

    public static final String THRESHOLD_PHONETHICS = "THRESHOLD_PHONETHICS";
    public static final Double DEFAULD_THRESHOLD_PHONETHICS = 90.0;
    public static final String CODE_STATUS_STEP_DILIGENCIAMIENTO_MANUAL = "8";


    public static final Integer NUM_DOS = 2;

    public static final String OUT_COME_CODE_ONE = "01";
    public static final String OUT_COME_NAME_ONE = "01 - Validaci\u00F3n de identidad exitosa, "
            + "es posible continuar con el proceso de vinculaci\u00F3n.";
    public static final String OUT_COME_CODE_TWO = "02";
    public static final String OUT_COME_NAME_TWO = "02 - Validaci\u00F3n de identidad NO exitosa, "
            + "Requiere validaci\u00F3n en Etapa 2  - Operacion preguntas reto.";
    public static final String OUT_COME_CODE_THREE = "03";
    public static final String OUT_COME_NAME_THREE = "03 - Validaci\u00F3n de identidad no exitosa. "
            + "El potencial cliente no super\u00F3 el umbral minimo de coincidencia en las validaciones de identidad";

    public enum FuzzyMatchMeta {
        OPERATION_TYPE_METAPHONE, OPERATION_TYPE_DOUBLE_METAPHONE, OPERATION_TYPE_SOUNDEX,
        OPERATION_TYPE_REFINED_SOUNDEX
    }

    /*
     * EXPO QUESTIONS OPERATION
     */
    public static final String VALIDATE_IDE_URI = "/v1/reference-data/party/customer-profile/internal/" +
            "customers-risks/actions/identification-validate";

    public static final String QUESTION_URI = "/v1/reference-data/party/customer-profile/internal/customers-risks" +
            "/actions/questionnaire";

    /*
     * VERIFY QUESTIONS OPERATION
     */

    public static final String QUESTION_VERIFY = "/v1/reference-data/party/customer-profile/" +
            "internal/customers-risks/actions/questionnaire-verify";

    public static final String OUT_COME_NAME_APPROVED = "01 - Aprobado";
    public static final String OUT_COME_NAME_REJECT = "02 - Rechazado";

    /**
     * SEGMENT CUSTOMER OPERATIONS - REST
     */
    public static final String SEGMENT_CUSTOMER_URI_POST = "/v1/sales-services/customer-management/customer-manage" +
            "-prospect/" +
            "customers/natural-persons";
    public static final String SEGMENT_CUSTOMER_URI_PATCH = "/v1/sales-services/customer-management/" +
            "customer-manage-prospect/customers/modify-natural-persons";
    public static final String CONTENT_TYPE_SEGMENT_CUSTOMER = "application/json";
    public static final String MESSAGE_SEGMENT_ID = "messageId";

    /**
     * SEGMENT CUSTOMER OPERATIONS - REQUEST
     */
    public static final String SCR_ZLISTACONT = "ZLISTACONT";
    public static final String SCR_ZEXPERIAN = "ZEXPERIAN";
    public static final String SCR_NO_PROBLEMS = "Sin problemas";
    public static final String SCR_OPERATION_I = "I";
    public static final String SCR_NATURAL_PERSON = "PN";
    public static final String SCR_CONTACT_CHANNEL = "ZV5";
    public static final String SCR_CONTACT_PLACE = "ZV15";
    public static final String SCR_ORGANIZATION_SALES = "00000002";
    public static final String SCR_ZERO_ONE = "01";
    public static final String SCR_BRANCH_OFFICE = "70005035";
    public static final String SCR_FUNCTION_TYPE1 = "BUR011";
    public static final String SCR_FUNCTION_TYPE2 = "ZCHM40";
    public static final String SCR_CC = "CC";
    public static final String SCR_DOC_NUMBER = "98668734";
    public static final String SCR_BUSINESS_ROLE = "NOEXISTE";
    public static final String SCR_BONDING_STATUS = "NA";


    /*
     * SEGMENT CUSTOMER OPERATIONS - TRANSFORM CATALOGS
     */
    public static final String SEGMENT_CODE = "SEGMEN_";
    public static final String SUB_SEGMENT_CODE = "SUBSEG_";
    public static final String MARK_NICHE_CODE = "REMTO_";
    public static final String SEGMENT = "SEGMENTO";
    public static final String SUB_SEGMENT = "SUBSEGMENTO";
    public static final String MARK_NICHE = "REMTO";
    public static final String NUMBER_SN = "1";
    public static final String ZETA_SN = "2";
    public static final String MARK_CORR = "3";
    public static final String LAST_TWO = "4";
    public static final String SUITABLE = "APTO";
    public static final String NOT_SUITABLE = "NO APTO";
    public static final Character CAT_FLOOR = '_';
    public static final String CAT_S = "S";
    public static final String CAT_Z = "Z";
    public static final String CAT_EMPTY = "Vacio";

    /*
     * SEGMENT CUSTOMER OPERATIONS - ERRORS
     */
    public static final String ERROR_BP001 = "BP001";
    public static final String ERROR_SA400 = "SA400";
    public static final String ERROR_BP025 = "BP025";
    public static final String ERROR_BP026 = "BP026";
    public static final String ERROR_BP108 = "BP108";
    public static final String ERROR_BP500 = "BP500";

    public static final String[] ERROR_SEG_CUS_BVNT020 = {
            ERROR_BP001, "BP002", "BP003", "BP006", "BP008", "BP009", "BP010", "BP011", "BP012", "BP013", "BP014",
            "BP015", "BP016", "BP017", "BP018", "BP019", "BP020", "BP022", "BP023", "BP024", "BP066", "BP067", "BP068",
            "BP069", "BP070", "BP071", "BP072", "BP073", "BP074", "BP075", "BP076", "BP077", "BP078", "BP079", "BP080",
            "BP081", "BP082", "BP083", "BP084", "BP085", "BP086", "BP087", "BP088", "BP109", "BP111", "BP112", "BP113"};

    public static final String[] ERROR_SEG_CUS_BVNT012 = {ERROR_BP025, ERROR_BP026};
    public static final String[] ERROR_SEG_CUS_TVNT012 = {
            "BP046", "BP061", "BP062", "BP063", "BP064", "BP065", "BP107", "BP999", "BP401", "BP404", ERROR_SA400};

    public static final String[] ERROR_SEG_CUS_TVNT047 = {ERROR_BP500, CODE_INTERNAL_SERVER_ERROR};

    /*
     * SEGMENT CUSTOMER OPERATIONS - VALIDATE
     */
    public static final String[] SG_VALIDATE_PERSONAL = {FIRST_NAME, PARAM_FIRST_SURNAME, BIRTHDATE, EXPEDITION_DATE};
    public static final String GET = "get";
    public static final String MARK = "mark";

    /*
     * MARK CUSTOMER OPERATIONS
     */
    public static final String MARK_CUSTOMER_URI = "/v1/sales-services/customer-management/customer-reference-" +
            "data-management/actions/control-lists";
    public static final String MC_DEPARTMENT = "103700";
    public static final String MC_CATEGORY = "69";
    public static final String MC_SUBCATEGORY = "02";
    public static final String MC_CUSTOMER_TYPE_N = "N";
    public static final String MC_REASON = "ACTUALIZACION LISTA CONTROL";
    public static final String MC_ANSWER_NAME = "Cliente marcado exitosamente como no Objetivo. Informaci\u00F3n de " +
            "car\u00E1cter confidencial";
    public static final String MC_NOT_ANSWER_NAME = "No aplica marcaci\u00F3n del potencial cliente, continuar " +
            "con el proceso de vinculaci\u00F3n";
    public static final String MC_BP0016 = "BP0016";
    public static final String MC_BP0008 = "BP0008";

    /*
     * CONSUMER MICROSERVICE DOCUMENTATION
     */

    public static final String CONTENT_TYPE = "application/json";
    public static final String NAME_APY_KEY = "apiKey";
    /*
     *   EXPO QUESTIONS CONSTANTS ANSWER OPTIONS IDENTIFICATION VALIDATE - GENERATE QUESTIONNAIRE
     */
    public static final String CREDIT_LIST_HISTORY = "01";
    public static final String NO_CREDIT_LIST_HISTORY = "05";
    public static final String NO_DATA_MATCH = "06";
    public static final String NO_EXIST_IDENTIFICATION = "07";
    public static final String INVALID_IDENTIFICATION = "08";
    public static final String ATTEMPTS_ALLOWED = "09";

    public static final String NO_DATA_MATCH_DETAIL = "No coinciden datos";
    public static final String NO_EXIST_IDENTIFICATION_DETAIL = "No existe identificaci\u00F3n";

    public static final String LAST_CONSULT_INACTIVE = "00";
    public static final String GENERATE_QUESTIONS_OK = "01";
    public static final String ERROR_GENERATE_QUESTIONS = "02";
    public static final String INSUFFICIENT_QUESTIONS = "07";
    public static final String MAX_ATTEMPTS_DAY = "10";
    public static final String MAX_ATTEMPTS_MONTH = "11";
    public static final String MAX_ATTEMPTS_YEAR = "12";
    public static final String MAX_ENTERED_DAY = "13";
    public static final String MAX_ENTERED_MONTH = "14";
    public static final String MAX_ENTERED_YEAR = "15";
    public static final String UNAUTHORIZED_CONSULT_T1 = "16";

    public static final String LAST_CONSULT_INACTIVE_DETAIL = "Usuario no tiene activa la opci\u00F3n de \u00FAltima" +
            " consulta \u00F3 no hubo resultados en la consulta";
    public static final String INSUFFICIENT_QUESTIONS_DETAIL = "No hay suficientes preguntas";
    public static final String UNAUTHORIZED_CONSULT_T1_DETAIL = "Consulta no autorizada - t1";

    public static final String DAY = "1";
    public static final String MONTH = "2";
    public static final String YEAR = "3";

    public static final Long HOUR_PER_DAY = 23L;
    public static final Long MINUTE_PER_HOUR = 59L;
    public static final Long SECOND_PER_MINUTE = 60L;

    public static final String RESULT_CODE_VALIDATION = "ResultCodeValidation - ";
    public static final String QUESTIONNAIRE_RESULT = "QuestionnaireResult - ";
    public static final String PARAMETER_CODE = "3371";

    /***
     * MESSAGES FOR TECHNICAL LOGS
     */
    public static final String ERROR_CONTROLADO_APP = "Error controlado de la aplicacion. Validacion";
    public static final String START_ACQUISITION = "Iniciando una acquisition";
    public static final String ACQUISITION_CREATED = "Finalizando: startAcquisition";
    public static final String BASE_64_CONVERT_ERROR = "Error transformando archivos a base 64";
    public static final String SQS_MESSAGE_FOUNDED = "SQS Message FOUNDED: ";
    public static final String ASYNC_DIGITALIZATION_FOUNDED = "AsyncDigitization FOUNDED: ";
    public static final String DOCUMENTS_CONNECTION_ERROR = "Error estableciendo conexión con microservicio Documents";
    public static final String TYPE_PERSON = "typePerson";
    public static final String TYPE_ACQUISITION = "typeAcquisition";
    public static final String DESCRIPTION_ACQUISITION = "descriptionAcquisition";
    public static final String DESCRIPTION_LINK = "descripcionVinculacion";
    public static final String ERROR_CONSUMING_CUSTOMER_DOC_PERSISTENCE = "There was an error consuming micro";

    /******* GENERATE PED *******/

    public static final String GENPDF_EXITO = "GENPDF_EXITOSA";
    public static final String GENEPDF_ERROR = "GENPDF_ERROR";
    public static final String ERROR_CONVERJSON = "Error convirtiendo el Json";

    /* PARA GENERAR PDF*/
    public static final String PDF_NEIGHBORHOOD = "NEIGHBORHOOD";
    public static final String PDF_COUNTRY = "COUNTRY";
    public static final String PDF_EMAIL = "EMAIL";
    public static final String PDF_CITY = "CITY";
    public static final String PDF_DEPARTMENT = "DEPARTMENT";
    public static final String PDF_PHONE = "PHONE";
    public static final String PDF_CELL_PHONE = "CELL_PHONE";
    public static final String PDF_PROFESSION = "PROFESSION";
    public static final String PDF_JOB = "JOB";
    public static final String PDF_ECONOMIC_ACTIVITY = "ECONOMIC_ACTIVITY";
    public static final String PDF_CODE_CIIU = "CODE_CIIU";
    public static final String PDF_NUMBER_EMPLOYEES = "NUMBER_EMPLOYEES";
    public static final String PDF_COMPANY_NAMES = "COMPANY_NAMES";
    public static final String PDF_COMPANY_ADDRESS = "COMPANY_ADDRESS";
    public static final String PDF_COMPANY_NEIGHBORHOOD = "COMPANY_NEIGHBORHOOD";
    public static final String PDF_COMPANY_CITY = "COMPANY_CITY";
    public static final String PDF_COMPANY_DEPARTMENT = "COMPANY_DEPARTMENT";
    public static final String PDF_COMPANY_COUNTRY = "COMPANY_COUNTRY";
    public static final String PDF_COMPANY_PHONE = "COMPANY_PHONE";
    public static final String PDF_COMPANY_PHONE_EXT = "COMPANY_PHONE_EXT";
    public static final String PDF_COMPANY_CELLPHONE = "COMPANY_CELLPHONE";
    public static final String PDF_COMPANY_EMAIL = "COMPANY_EMAIL";
    public static final String PDF_TEXT_EQUIX = "X";
    public static final String PDF_TYPE_ACQUISITION = "TYPE_ACQUISITION";
    public static final String PDF_COMPLETION_DATE = "COMPLETION_DATE";

    public static final String PDF_FIRST_NAME = "FIRST_NAME";
    public static final String PDF_SECOND_NAME = "SECOND_NAME";
    public static final String PDF_FIRST_SURNAME = "FIRST_SURNAME";
    public static final String PDF_SECOND_SURNAME = "SECOND_SURNAME";

    public static final String PDF_TYPE_DOCUMENT_CC = "TIPDOC_FS001";
    public static final String PDF_TYPE_DOCUMENT_TI = "TIPDOC_FS004";
    public static final String PDF_TYPE_DOCUMENT_RC = "TIPDOC_FS009";
    public static final String PDF_TYPE_DOCUMENT_CE = "TIPDOC_FS002";
    public static final String PDF_TYPE_DOCUMENT_PA = "TIPDOC_FS005";
    public static final String PDF_TYPE_DOCUMENT_CD = "TIPDOC_FS000";

    public static final String PDF_MARITAL_STATUS_SOLTERO = "ESTCIV_1";
    public static final String PDF_MARITAL_STATUS_CASADO = "ESTCIV_2";
    public static final String PDF_MARITAL_STATUS_UNION = "ESTCIV_5";

    public static final String PDF_OCUPAC_01 = "OCUPAC_01";
    public static final String PDF_OCUPAC_02 = "OCUPAC_02";
    public static final String PDF_OCUPAC_03 = "OCUPAC_03";
    public static final String PDF_OCUPAC_04 = "OCUPAC_04";
    public static final String PDF_OCUPAC_05 = "OCUPAC_05";
    public static final String PDF_OCUPAC_06 = "OCUPAC_06";
    public static final String PDF_OCUPAC_07 = "OCUPAC_07";
    public static final String PDF_OCUPAC_08 = "OCUPAC_08";
    public static final String PDF_OCUPAC_09 = "OCUPAC_09";
    public static final String PDF_OCUPAC_10 = "OCUPAC_10";
    public static final String PDF_OCUPAC_11 = "OCUPAC_11";
    public static final String PDF_OCUPAC_12 = "OCUPAC_12";
    public static final String PDF_OCUPAC_13 = "OCUPAC_13";
    public static final String PDF_OCUPAC_14 = "OCUPAC_14";
    public static final String PDF_OCUPAC_NULL = "OCUPAC_NULL";


    public static final String PDF_TYPE_DOUMENT = "TYPE_DOUMENT";
    public static final String PDF_DOCUMENT = "DOCUMENT";
    public static final String PDF_EXPEDITION_PLACE = "EXPEDITION_PLACE";
    public static final String PDF_EXPEDITION_DATE = "EXPEDITION_DATE";
    public static final String PDF_BIRTH_DATE = "BIRTH_DATE";
    public static final String PDF_GENDER = "GENDER";
    public static final String PDF_GENDER_F = "GENERO_F";
    public static final String PDF_GENDER_M = "GENERO_M";
    public static final String PDF_YES = "SI";
    public static final String PDF_NOT = "NO";
    public static final String PDF_RESIDENCE_ADDRESS = "RESIDENCE_ADDRESS";
    public static final String PDF_CUENTA_DE_AHORROS = "CUENTA DE AHORROS";
    public static final String PDF_CUENTA_CORRIENTE = "CUENTA CORRIENTE";
    public static final String PDF_BIRTH_PLACE = "BIRTH_PLACE";
    public static final String PDF_NATIONALITY = "NATIONALITY";
    public static final String PDF_COLOMBIANO = "CO_COLOMBIANO";
    public static final String PDF_AMERICANO = "US_ESTADOUNIDENSE";
    public static final String PDF_NATIONALITY_DEF = "NATIONALITY_DEF";

    public static final String PDF_MONTHLY_INCOME = "MONTHLY_INCOME";
    public static final String PDF_TOTAL_ASSETS = "TOTAL_ASSETS";
    public static final String PDF_MONTHLY_OTHER_INCOME = "MONTHLY_OTHER_INCOME";
    public static final String PDF_TOTAL_PASSIVES = "TOTAL_PASSIVES";
    public static final String PDF_DETAIL_OTHER_MONTH_INCOME = "DETAIL_OTHER_MONTH_INCOME";
    public static final String PDF_TOTAL_MONTH_EXPENSES = "TOTAL_MONTH_EXPENSES";
    public static final String PDF_ANNUAL_SALES = "ANNUAL_SALES";
    public static final String PDF_CLOSING_SALES_DATE = "CLOSING_SALES_DATE";
    public static final String PDF_INCOME_DECLARANT_YES = "INCOME_DECLARANT_YES";
    public static final String PDF_INCOME_DECLARANT_NOT = "INCOME_DECLARANT_NOT";
    public static final String PDF_WITH_HOLDING_AGENT_YES = "WITH_HOLDING_AGENT_YES";
    public static final String PDF_WITH_HOLDING_AGENT_NOT = "WITH_HOLDING_AGENT_NOT";

    public static final String PDF_REGIME_IVA = "REGIME_IVA";

    public static final String PDF_REGIME_01 = "REGIME_01";
    public static final String PDF_REGIME_02 = "REGIME_02";
    public static final String PDF_REGIME_03 = "REGIME_03";


    public static final String PDF_DECLARE_TAX_IN_ANOTHER_COUNTRY_YES = "DECLARE_TAX_IN_ANOTHER_COUNTRY_YES";
    public static final String PDF_DECLARE_TAX_IN_ANOTHER_COUNTRY_NOT = "DECLARE_TAX_IN_ANOTHER_COUNTRY_NOT";
    public static final String PDF_ORIGIN_ASSET_COME_FROM = "ORIGIN_ASSET_COME_FROM";
    public static final String PDF_ORIGIN_ASSET_COME_FROM_CITY = "ORIGIN_ASSET_COME_FROM_CITY";
    public static final String PDF_ORIGIN_ASSET_COME_FROM_COUNTRY = "ORIGIN_ASSET_COME_FROM_COUNTRY";
    public static final String PDF_COUNTRY_TAX = "COUNTRY_TAX";
    public static final String PDF_ID_TAX = "ID_TAX";
    public static final String PDF_REALIZA_OPERACIONES_MONEDA_EXTRANJERA_YES
            = "REALIZA_OPERACIONES_MONEDA_EXTRANJERA_YES";
    public static final String PDF_REALIZA_OPERACIONES_MONEDA_EXTRANJERA_NOT
            = "REALIZA_OPERACIONES_MONEDA_EXTRANJERA_NOT";
    public static final String PDF_CUAL_OPERACION_MONEDA_EXTRANJERA = "CUAL_OPERACION_MONEDA_EXTRANJERA";
    public static final String PDF_NOMBRE_ENTIDAD = "NOMBRE_ENTIDAD";
    public static final String PDF_TIPOPR_01 = "TIPOPR_01";
    public static final String PDF_TIPOPR_02 = "TIPOPR_02";
    public static final String PDF_TIPOPR_03 = "TIPOPR_03";


    public static final String PDF_NRO_PRODUCTO = "NRO_PRODUCTO";
    public static final String PDF_MONTO_MENSUAL_PROMEDIO = "MONTO_MENSUAL_PROMEDIO";
    public static final String PDF_MONEDA = "MONEDA";
    public static final String PDF_CITY_FOREIGN = "CITY_FOREIGN";
    public static final String PDF_COUNTRY_FOREIGN = "COUNTRY_FOREIGN";
    public static final String PDF_PATTERN_DATE = "dd         MM        yyyy";

    public static final String PDF_TIPOPE_002 = "TIPOPE_002";
    public static final String PDF_TIPOPE_003 = "TIPOPE_003";
    public static final String PDF_TIPOPE_004 = "TIPOPE_004";
    public static final String PDF_TIPOPE_005 = "TIPOPE_005";
    public static final String PDF_TIPOPE_006 = "TIPOPE_006";
    public static final String PDF_TIPOPE_007 = "TIPOPE_007";
    public static final String PDF_TIPOPE_008 = "TIPOPE_008";
    public static final String PDF_TIPOPE_009 = "TIPOPE_009";
    public static final String PDF_TIPOPE_WICH = "TIPOPE_WICH";
    public static final String PDF_TEXT_GUION_BAJO = "_";

    /*
     * PROCESS DOCUMENT OPERATIONS - TRANSFORM CATALOGS
     */
    public static final String GENDER_CODE = "GENERO_";
    public static final String CIIU_CODE = "CIIU_0";

    /**
     * Constants for upload document and process document
     */
    public static final String DOCUMENTS_FOLDER = "documents";
    public static final String SUFFIX = "/";
    public static final String PROCESS_NAME_DIGITALIZATION = "VinculacionTransversal";
    public static final String PROCESS_CODE_CEDULA = "PROC_Cedula_VinculacionTransversal";
    public static final String PROCESS_CODE_RUT = "PROC_Rut_VinculacionTransversal";
    public static final String JPG_EXT = "jpg";
    public static final String JPEG_EXT = "jpeg";
    public static final String PNG_EXT = "png";
    public static final String PDF_EXT = "pdf";
    public static final String TIF_EXT = "tif";
    public static final String TIFF_EXT = "tiff";
    public static final String MYME_JPEG = "image/jpeg";
    public static final String MYME_TIFF = "image/tiff";
    public static final String MYME_PNG = "image/png";
    public static final String MYME_PDF = "application/pdf";
    public static final String CEDULA_SUBTYPE = "001";
    public static final String RUT_SUBTYPE = "002";
    public static final String PROCESS_DOCUMENT_MICRO = "process-document";
    public static final String TYPE_ID_CC = "C\u00e9dula";
    public static final String TYPE_RUT = "RUT";
    public static final int EXPIRED_RUT_YEAR = 2013;
    public static final String PARENT_UPLOAD_DOCUMENT = "uploadDocument";
    public static final String ERROR_SAVE_PERSONAL_INFO = "Error saving personal information from Kofax";
    public static final String ERROR_SAVE_BASIC_INFO = "Error saving basic information from Kofax";
    public static final String NOT_RUT_EXTRACTION = "No requiere cargar RUT";
    public static final String NOT_RUT_EXTRACTION_REASON = "De acuerdo a las condiciones del cliente no requiere" +
            " aportar documentos adicionales como soporte de la vinculaci\u00F3n. Puede continuar con la siguiente" +
            " etapa del proceso.";
    public static final String CODE_UPLOAD_DOCUMENT_RETRIES = "IDENTITY_RETRIES";
    public static final String CODE_UPLOAD_RUT_RETRIES = "RUT_RETRIES";
    public static final String THRESHOLD_FIRST_NAME = "THRESHOLD_FIRST_NAME";
    public static final String THRESHOLD_FIRST_SURNAME = "THRESHOLD_FIRST_SURNAME";
    public static final String ERROR_SAVE_ECONOMIC_INFO = "Error saving economic information from Kofax";
    public static final String ERROR_RETRIEVING_FILE = "ERROR AL RECUPERAR EL ARCHIVO DEL BUCKET";

    /**
     * KOFAX DOCUMENT TYPES
     */
    public static final String KOFAX_CC = "Cedula de ciudadania";
    public static final String KOFAX_PA = "Pasaporte";
    public static final String KOFAX_CE = "cedula de extranjeria";
    public static final String KOFAX_NIT = "NIT";


    public static final String COINCIDENCE_CIIU_FIELD = "coincidenceCIIU";
    public static final String NOT_APPLY = "NA";

    /* PARA UPLOAD DOCUMENT ASYN */

    public static final String IN_PROCCESS_DOCUMENT = "En Proceso";
    public static final String IN_PROCCESS_DOCUMENT_REASON = "El documento no est\u00E1 siendo procesado en " +
            "simultaneo,por favor realizar posteriormente el llamado a la Operaci\u00F3n Validar datos " +
            "extra\u00EDdos para obtener el resultado del procesamiento.";

    /**
     * SESSION ID - OPERATIONS
     */
    public static final String SI_TOKEN_URI = "/v2/security/oauth-sessionid/oauth2/token";

    public static final String SI_HEADER_CLIENT_IP = "x-client-Ip4";
    public static final String SI_HEADER_AUTHORIZATION = "Authorization";
    public static final String SI_HEADER_CONSUMER_ID = "x-consumer-id";
    public static final String SI_HEADER_SESSION_STATUS = "x-session-status";

    public static final String SI_BODY_GRANT_TYPE = "grant_type";
    public static final String SI_BODY_USERNAME = "username";
    public static final String SI_BODY_PASSWORD = "password";
    public static final String SI_BODY_SCOPE = "scope";

    public static final String SI_CLIENT_IP = "127.0.0.1";
    public static final String SI_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String SI_ACCEPT = "application/json";
    public static final String SI_SESSION_STATUS = "Validar";
    public static final String SI_GRANT_TYPE = "password";
    public static final String SI_SCOPE = "Customer-basic:read:user";

    public static final String SI_ERROR_API = "Error generado desde la API:";

    /* PARA UPLOAD DOCUMENT ASYN */
    public static final String ASYN_UPLOAD_BASE_64 = "base64";
    public static final String ASYN_UPLOAD_EXTENSION = "extension";
    public static final String ASYN_UPLOAD_FILE_NAME = "fileName";
    public static final String ASYN_UPLOAD_DOCUMENT_NUMBER = "documentNumber";
    public static final String ASYN_UPLOAD_DOCUMENT_TYPE = "documentType";
    public static final String ASYN_UPLOAD_ID = "id";
    public static final String ASYN_UPLOAD_UPLOAD_DOCUMENT_RETRIES = "uploadDocumentRetries";
    public static final String ASYN_UPLOAD_ACQUISITION = "acquisition";
    public static final String ASYN_UPLOAD_FILES = "files";
    public static final String ASYN_UPLOAD_MESSAGE_ID = "messageId";
    public static final String ASYN_UPLOAD_PROCESS_CODE = "processCode";
    public static final String ASYN_UPLOAD_PROCESS_NAME = "processName";
    public static final String ASYN_UPLOAD_USER_CODE = "userCode";
    public static final String ASYN_UPLOAD_DATA = "data";
    public static final String ASYN_UPLOAD_FLAGDATAEXTRACTION = "flagDataExtraction";
    public static final String ASYN_UPLOAD_FLAGSYNCHRONOUS = "flagSynchronous";
    public static final String ASYN_UPLOAD_DOCUMENTALSUBTYPECODE = "documentalSubTypeCode";
    public static final String ASYN_UPLOAD_DOCUMENTALTYPECODE = "documentalTypeCode";
    public static final String ASYN_UPLOAD_USRMOD = "usrMod";
    public static final String ASYN_UPLOAD_IP = "ip";
    public static final String ASYN_UPLOAD_SYSTEMID = "systemId";
    public static final String ASYN_UPLOAD_MESSAGEID = "messageId";
    public static final String ASYN_UPLOAD_VERSION = "version";
    public static final String ASYN_UPLOAD_REQUESTDATE = "requestDate";
    public static final String ASYN_UPLOAD_SERVICE = "service";
    public static final String ASYN_UPLOAD_OPERATION = "operation";


    /* PARA SQS */
    public static final String OPER_SQS_MESSAGE = "sqs-message";
    public static final String OPER_SQS_MESSAGE_USECASE = "sqs-message-usecase";

    public static final String SQS_MESSAGE_GROUP_ID = "message-group-id";
    public static final String SQS_MESSAGE_DEDUPLICATION_ID = "message-deduplication-id";

    public static final String SQS_SEND_MESSAGE_OK = "Mensaje SQS Enviado Correctamente";
    public static final String SQS_SAVE_MESSAGE_OK = "Mensaje SQS Grabado Correctamente";
    public static final String SQS_COMPLETED_OPERATION_OK = "Operaci�n de envio de mansaje Completada Correctamente";
    public static final String SQS_SEND_MESSAGE_ERROR = "No se Pudo enviar el Mensaje SQS";

    public static final String SQS_MICRO_PROFILING = "MICRO_PROFILING";
    public static final String SQS_MICRO_DOCUMENT = "MICRO_DOCUMENT";
    public static final String SQS_WHICH_MICRO_RECEIVES = "SQS_WHICH_MICRO_RECEIVES";
    public static final String SQS_WHICH_OPERATION = "SQS_WHICH_OPERATION";

    public static final String SQS_IDMESSAGE = "SQS_IDMESSAGE";

    public static final String SQS_STATUS_EARRING = "PENDIENTE";

    public static final String SQS_QUEUE_REGION = "us-east-1";

    public static final String SQS_PARAM_MAX_RETRIES_GET = "SQS_PARAM_MAX_RETRIES_GET";
    public static final String SQS_PARAM_PROFILING = "SQS_MICRO_PROFILING";

    /* PARA ECONOMIC INFORMATION */
    public static final String ECONOMIC_REQUIRE_RUT_CODE = "01";
    public static final String ECONOMIC_REQUIRE_RUT_NAME = "Exitoso. Requiere cargar RUT.";
    public static final String ECONOMIC_NO_REQUIRE_RUT_CODE = "00";
    public static final String ECONOMIC_NO_REQUIRE_RUT_NAME = "Exitoso. NO requiere cargar RUT.";

    /**
     * Duración url pre-firmada
     */
    public static final int TIME_URL = 5;


    public static final String STATE_COMPLETED = "COMPLETADO";

    /*
     * CONSULT INSTRUCTIONS ACCEPT CLAUSES POLICY URLs
     */
    public static final String TITLE_POLICY_URL = "URL POLITICA DE PROTECCION DE DATOS:";
    public static final String INFORMATION_PROCCESING_URL =
            "https://www.grupobancolombia.com/wps/wcm/connect/ac040907-38ee-4eb5-a099-9b50a44c89c6/"
                    + "Autorizaci%C3%B3n+para+Administraci%C3%B3n+de+Datos+Personales+21032018.pdf"
                    + "?MOD=AJPERES&CVID=m9faK7P&_ga=2.57126574.540864147.1603891750-1099659819.1590521254";
    public static final String DATA_ADMINISTRATION_POLICY_URL = "https://www.grupobancolombia.com/personas/"
            + "documentos-legales/proteccion-datos/bancolombia-sa"
            + "?_ga=2.98536219.540864147.1603891750-1099659819.1590521254";

    public static final String ANSWER_NAME_SUCCESS = "Cliente creado exitosamente en el maestro de Clientes";

    public static final String[] ERROR_CODE_PERSISTENCE_DOCUMENT = {
            "1402", "1403", "1404", "1405", "1406", "1407", "1408", "1410", "1411", "1412", "1413",
            "1414", "1415", "1416", "1417", "1418", "1419", "1420", "1421", "1422", "1429", "1430", "1431",
            "1435", "1423", "1432", "1434", "1436", "1437", "1438", "1439", "1441", "1442", "1443"};

    /*
     * Persistence Documents
     */
    public static final String URL_BUCKET_DOCUMENTS = "nu0034001/documents/";
    public static final String URL_BUCKET_DOC_PROCESSED = "nu0034001/processed/";
    public static final String ERROR_STATE = "ERROR";
    public static final String OK_STATE = "OK";
    public static final String DOC_SEND_CONTINGENCY = "1433";
    public static final String ST_PENDING = "1";
    public static final String ST_COMPLETED = "2";
    public static final String TYPE_CC = "C\u00e9dula de ciudadan\u00EDa";
    public static final String DOCUMENTARY_TYPE = "01";
    public static final String DELETE_FILE_SUCCESS = "DELETE FILE SUCCESS: ";
    public static final String DELETE_FILE_ERROR = "DELETE FILE ERROR: ";
    public static final String DOCUMENT_CODE_ALL_DOCUMENTS = "000";
    public static final String OUT_COME_CODE_ZERO = "0";
    public static final String OUT_COME_NAME_ZERO = "Documentos guardados exitosamente";
    public static final String COPY_FILE_SUCCESS = "COPY FILE SUCCESS TO: ";
    public static final String ERROR_MOVING_FILE_IN_COPY = "ERROR MOVING FILE WHEN COPY FROM: ";
    public static final String ERROR_MOVING_FILE_IN_REMOVE = "ERROR MOVING FILE WHEN REMOVE FROM: ";
    public static final String DOCUMENTAL_TYPE_CODE_FIRST = "documentalTypeCodeFirst";
    public static final String DOCUMENTAL_SUB_TYPE_CODE_FIRST = "subTypeCodeFirst";
    public static final String DOCUMENT_ID_FIRST = "documentIdFirst";
    public static final String PUBLISHING_RESULT_FIRST = "publishingResultFirst";
    public static final String CODE_FIRST = "codeFirst";
    public static final String TITTLE_FIRST = "tittleFirst";
    public static final String DETAIL_FIRST = "detailFirst";
    public static final String DOCUMENTAL_TYPE_CODE_SECOND = "documentalTypeCodeSecond";
    public static final String DOCUMENTAL_SUB_TYPE_SECOND = "subTypeCodeSecond";
    public static final String DOCUMENT_ID_SECOND = "documentIdSecond";
    public static final String PUBLISHING_RESULT_SECOND = "publishingResultSecond";
    public static final String CODE_SECOND = "codeSecond";
    public static final String TITTLE_SECOND = "tittleSecond";
    public static final String DETAIL_SECOND = "detailSecond";
    public static final String SUCCESFUL_RESULT = "EXITOSO";
    public static final String FAIL_RESULT = "FALLIDO";
    public static final String ERROR_MESSAGE_PUBLISHING_DOCUMENTS = "Falla proceso publicaci\u00F3n de documentos";
    public static final String NO_APPLY_SIMULATED = "NA_SIMULATED";

    /* Flags upload document validate retries Emission date or personal info*/
    public static final String VALIDATE_RUT_RETRIES_FLAG_EMISSION = "EMISSION_DATE";
    public static final String VALIDATE_RUT_RETRIES_FLAG_PERSONAL_INFO = "PERSONAL_INFO";
    public static final String VALIDATE_RUT_RETRIES_FLAG_EMPTY = "";

    /**
     * MONITORING INDEXES
     */
    public static final String MNT_INDEX_OPERATION = "operationIndex";
    public static final String MNT_INDEX_OPERATION_EXPR = "operation = :oper and documentNumber = :docnum";
    public static final String MNT_INDEX_EXPRESSION_OPERATION = ":oper";
    public static final String MNT_INDEX_EXPRESSION_DOC_NUM = ":docnum";
    public static final String MNT_INDEX_ACQUISITION = "acquisitionIndex";
    public static final String MNT_INDEX_ACQUISITION_EXPR = "acquisitionId = :acqui and operation = :oper";
    public static final String MNT_INDEX_EXPRESSION_ACQUISITION = ":acqui";

    /**
     * OPERATION RETRIEVE-SCENARIO
     */
    public static final String GENERATE_TOKEN_MAX_RETRIES_NAME = "GenerateTokenMaxRetries";
    public static final String GENERATE_TOKEN_MAX_RETRIES_PARENT = "generateTokenMaxRetries";
    public static final String VALIDATE_TOKEN_MAX_RETRIES_NAME = "ValidateTokenMaxRetries";
    public static final String VALIDATE_TOKEN_MAX_RETRIES_PARENT = "validateTokenMaxRetries";

    private Constants() {
    }
}
