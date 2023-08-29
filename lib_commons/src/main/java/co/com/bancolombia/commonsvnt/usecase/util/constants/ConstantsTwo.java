package co.com.bancolombia.commonsvnt.usecase.util.constants;

public final class ConstantsTwo {
    /**
     * SIGN DOCUMENT - OPERATIONS
     */
    public static final String SD_RESPONSE_CODE = "01";
    public static final String SD_RESPONSE_NAME = "Documento firmado y custodiado exitosamente.";
    public static final String ES_EXCEEDED_RETRIES = "Se supero el numero de intentos permitidos.";

    /**
     * SIGN DOCUMENT - ERRORS
     */
    public static final String[] ERROR_ES_BVNT020_NO_RETRY = {
            "BP1401", "BP1402", "BP1403", "BP1404", "BP1405", "BP1406", "BP1407", "BP1408", "BP1409", "BP1410",
            "BP1411", "BP1415", "BP1416", "BP1417"};
    public static final String[] ERROR_ES_BVNT020_RETRY = {
            "BP1412", "BP1413", "BP1414", "BP1418", "BP1419", "BP1420", "BP1421", "SA401",
            Constants.CODE_INTERNAL_SERVER_ERROR};
    public static final String[] ERROR_ES_TVNT012 = {"400", "404", "500", "500", "504"};

    public static final String[] OCCUPATION_RUT = {
            Constants.PDF_OCUPAC_03, Constants.PDF_OCUPAC_06, Constants.PDF_OCUPAC_07, Constants.PDF_OCUPAC_08,
            Constants.PDF_OCUPAC_09, Constants.PDF_OCUPAC_10};

    /**
     * SIGN DOCUMENT - ITERATIONS
     */
    public static final String TRACE_FILE = "traceability_file.txt";
    public static final String NAME_TXT = "Documentos de trazabilidad.txt";
    public static final String TEST_PLAIN = "test/plain";
    public static final String FIRST_ATTACH = "My first attachment";

    public static final String SESSION_ID = "ID SESION:";
    public static final String SOL_ID = "ID SOLICITUD:";
    public static final String DOC_TYPE_2 = "TIPO DE DOCUMENTO:";
    public static final String DOC_NUMBER = "NUMERO DE DOCUMENTO";
    public static final String SIGN_IP = "IP:";
    public static final String DATE = "FECHA:";


    public static final String ITERATION = "INTERACCION";
    public static final String SEPARATOR = "---------------------";

    public static final String CODE_CLAUSE = "CODIGO CLAUSULA:";
    public static final String DESC_CLAUSE = "DESCRIPCION CLAUSULA:";
    public static final String REST_OK = "RESULTADO INTERACCION: EXITOSO, PUEDE CONTINUAR.";
    public static final String CLAUSE1 = "CLAUSE001";
    public static final String CLAUSE2 = "CLAUSE002";

    public static final String F_NAME = "PRIMER NOMBRE";
    public static final String S_NAME = "SEGUNDO NOMBRE";
    public static final String P_SURNAME = "PRIMER APELLIDO";
    public static final String S_SURNAME = "SEGUNDO APELLIDO";
    public static final String F_EXP = "FECHA DE EXPEDICION:";
    public static final String BIRTH_DATE = "FECHA DE NACIMIENTO:";
    public static final String S_GENDER = "GENERO";

    public static final String S_EMAIL = "CORREO ELECTRONICO:";
    public static final String S_CELLPHONE = "CELULAR:";
    public static final String P_EXP = "PAIS DE EXPEDICION:";
    public static final String D_EXP = "DEPARTAMENTO DE EXPEDICION:";
    public static final String C_EXP = "CUIDAD DE EXPEDICION:";
    public static final String INFO_ITER_THREE_1 = "EN CASO DE DILIGENCIAMIENTO MANUAL DE LOS DATOS DE " +
            "IDENTIDAD ENTREGO:";
    public static final String INFO_ITER_THREE_2 = "SE REALIZA COMPARACION DE DATOS DE IDENTIDAD EXTRAIDOS DEL " +
            "DOCUMENTO Y/O DILIGENCIADOS CONTRA LO EXPUESTO POR EXPERIAN.";

    public static final String P_BIRTH = "PAIS DE NACIMIENTO:";
    public static final String D_BIRTH = "DEPARTAMENTO DE NACIMIENTO:";
    public static final String C_BIRTH = "CIUDAD DE NACIMIENTO:";
    public static final String S_CIVIL_STATUS = "ESTADO CIVIL:";
    public static final String S_NATIONALITY = "NACIONALIDAD:";
    public static final String DEPENDENTS = "NUMERO DE PERSONA A CARGO:";
    public static final String EDU_LEVEL = "NIVEL ACADEMICO:";
    public static final String STRATUM = "ESTRATO:";
    public static final String HOUSE_TYPE = "TIPO DE VIVIENDA:";
    public static final String S_CONTRACT_TYPE = "TIPO DE CONTRATO:";
    public static final String PEP = "PERSONA EXPUESTA PUBLICAMENTE:";
    public static final String INFO_ITER_FIVE = "EN CASO DE DILIGENCIAMIENTO MANUAL DE LOS DATOS DE IDENTIDAD ENTREGO:";

    public static final String T_ADDRESS = "TIPO DE DIRECCION:";
    public static final String S_BRAND = "MARCA DE CORRESPONDENCIA:";
    public static final String ADRRESS = "DIRECCION:";
    public static final String NEIGHTBORHOOD = "BARRIO:";
    public static final String S_COUNTRY = "PAIS:";
    public static final String S_DEPARTMENT = "DEPARTAMENTO:";
    public static final String S_CITY = "CUIDAD:";
    public static final String PHONE = "TELEFONO:";
    public static final String EXT = "EXTENSION:";
    public static final String C_NAME = "NOMBRE DE LA EMPRESA:";

    public static final String S_PROFESSION = "PROFESION:";
    public static final String P_TRADE = "CARGO U OFICIO:";
    public static final String S_OCCUPATION = "OCUPACION:";
    public static final String ACT_ECONOMIC = "ACTIVIDAD ECONOMICA:";
    public static final String CIUU = "CODIGO CIIU:";
    public static final String M_INCOME = "INGRESOS MENSUALES:";
    public static final String OT_INCOME = "OTROS INGRESOS MENSUALES:";
    public static final String T_ASSETS = "TOTAL ACTIVOS:";
    public static final String T_LIABILITIES = "TOTAL PASIVOS:";
    public static final String S_CURRENCY = "MONEDA:";
    public static final String D_O_INCOME = "DETALLE OTROS INGRESOS MENSUALES DIFERENTES A SU ACTIVIDAD ECONOMICA:";
    public static final String TE_MONTHLY = "TOTAL EGRESOS MENSUALES:";
    public static final String S_ANNUAL = "VENTAS ANUALES:";
    public static final String C_D_ANNUAL = "FECHA DE CIERRE VENTA ANUALES:";
    public static final String T_PATRIMONY = "TOTAL PATRIMONIO:";
    public static final String EMP_NUMBER = "NUMERO EMPLEADOS:";
    public static final String S_RUT = "RUT:";

    public static final String DEC_INCOME = "\u00bfES DECLARANTE DE RENTA\u003f:";
    public static final String W_AGENT = "AGENTE RETENEDOR:";
    public static final String R_VAT = "REGIMEN IVA:";
    public static final String O_ASSET_COME = "EL ORIGEN DE MIS BIENES Y/O FONDOS PROVIENEN DE:";
    public static final String C_SOURCE = "EL PAIS DE ORIGEN DE BIENES Y/O FONDOS:";
    public static final String CY_SOURCE = "LA CIUDAD DE ORIGEN DE BIENES Y/O FONDOS:";
    public static final String SOCIAL_SEC = "\u00bfREALIZA PAGOS DE SEGURIDAD SOCIAL\u003f:";
    public static final String D_TAX_A_COUNTRY = "\u00bfESTA OBLIGADO A TRIBUTAR COMO RESIDENTE O CIUDADANO EN LOS " +
            "ESTADOS UNIDOS O EN OTROS PAISES DIFERENTES A COLOMBIA\u003f:";
    public static final String ID_TAX = "IDENTIFICADOR DEL REGISTRO, ID TRIBUTARIO Y PAIS:";

    public static final String F_CURRENCY_T = "\u00bfREALIZA OPERACIONES EN MONEDA EXTRANJERA\u003f:";
    public static final String F_CURRENCY_TT = "\u00bfCUAL(ES) DE LAS SIGUIENTES OPERACIONES REALIZA EN M.E\u003f:";

    public static final String S_PRODUCT_TYPE = "TIPO DE PRODUCTO:";
    public static final String PRODUCT_N = "N DE PRODUCTO:";
    public static final String AVERAGE_AMOUNT = "MONTO MENSUAL PROMEDIO:";

    public static final String INFO_ITER_TEN = "SE VALIDA PERTENENCIA DEL DOCUMENTO Y LA ACTIVIDAD ECONOMICA " +
            "PRINCIPAL ENTREGADA VERSUS ACTIVIDAD ECONOMICA PRINCIPAL REGISTRADA EN EL RUT.";

    /**
     * SEND_FORM
     */
    public static final String ANSWER_NAME = "Formato enviado exitosamente";
    public static final String ORIGIN_VALUE_EMAIL = "originValueEmail";
    public static final String[] ERRORS_CODES_NOTIFICATIONS_RETRY = {"SP500", "SP502", "SP504"};
    public static final String[] ERRORS_CODES_NOTIFICATIONS_NO_RETRY = {Constants.ERROR_SA400, "SA401"};
    public static final String TEMPLATE_TYPE = "masiv-template/html";
    public static final String NAME = "nombre";
    public static final String NAME2 = "nombre2";
    public static final String SURNAME = "apellido";
    public static final String SURNAME2 = "apellido2";
    public static final String FULL_NAME = "nombrecompleto";
    public static final String POLICE_LINK = "linkpolitica";
    public static final String AUTHORIZATION_LINK = "linkautorizacion";
    public static final String DOC_TYPE = "tipodocumento";
    public static final String DOC_NUM = "numdocumento";
    public static final String ACQ_TYPE = "tipovinculacion";
    public static final String CHANNEL = "canal";
    public static final String PRODUCT = "producto";
    public static final String BUSINESS_LINE2 = "lineanegocio";
    public static final String RETRY = "sendFormRetry";
    public static final String SF_PDF_NAME = "F2486_conocimiento_pn_vinculacion_transversal.pdf";
    public static final String SF_FORM_CODE_KNOWLEDGE_FORMAT = "0";
    public static final String SF_FORM_CODE = "formCode";

    private ConstantsTwo() {
    }
}
