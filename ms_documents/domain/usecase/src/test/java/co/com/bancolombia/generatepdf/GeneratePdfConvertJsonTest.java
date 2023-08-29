package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.model.generatepdf.CompanyInformationPdf;
import co.com.bancolombia.model.generatepdf.ContactInformationPdf;
import co.com.bancolombia.model.generatepdf.CountryTaxPdf;
import co.com.bancolombia.model.generatepdf.EconomicInformationPdf;
import co.com.bancolombia.model.generatepdf.FinancialInformationPdf;
import co.com.bancolombia.model.generatepdf.ForeignCurrencyPdf;
import co.com.bancolombia.model.generatepdf.InternationalOperationPdf;
import co.com.bancolombia.model.generatepdf.TributaryInformationPdf;
import co.com.bancolombia.util.TransformFields;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PDF_PATTERN_DATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GeneratePdfConvertJsonTest {

	@InjectMocks
	@Spy
	private GeneratePdfConvertJsonImpl generatePdfConvertJson;

	@Mock
	private TransformFields transformFields;

	private Acquisition acquisition;
	
	private AcquisitionPdf acquisitionPdfFull;

	private SimpleDateFormat simpleDateFormat;

	private Date date;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		date = new Date();

		acquisition = Acquisition.builder()
				.id(UUID.randomUUID())
				.documentNumber("10000000")
				.firstName("JUAN")
				.secondName("JOSE")
				.firstSurname("NEGRO")
				.secondSurname("BLANCO")
				.documentType(DocumentType.builder().code("TIPDOC_FS001").build())
				.build();
		
		List<CountryTaxPdf> countryTax = Arrays.asList(
				CountryTaxPdf.builder().country("COLOMBIA").idTax("id").build());
		
	    List<ForeignCurrencyPdf> foreignCurrencyList = Arrays.asList(
	    		ForeignCurrencyPdf.builder()
						.entityName("").productType("TIPOPR_01").productNumber("")
						.averageMonthlyAmount("").currency("").city("").country("")
						.foreignCurrencyTransactionType("TIPOPE_002").build());

		acquisitionPdfFull = AcquisitionPdf.builder()
				.acquisitionId(acquisition.getId())
				.upgrade(true)
				.completionDate(date)
				.firstName("JUAN")
				.secondName("JOSE")
				.firstSurname("NEGRO")
				.secondSurname("BLANCO")
				.documentType("FS0001")
				.documentNumber("999999")
				.expeditionPlace("MEDELLIN")
				.expeditionDate(date)
				.birthPlace("MEDELLIN")
				.birthDate(date)
				.gender("GENERO_F")
				.matiralStatus("N")
				.nationality("CO_COLOMBIANO")
				.typeAcquisition("TVN001")
				.contactInformationPdf(ContactInformationPdf.builder()
						.residenceAddress("")
						.neighborhood("")
						.city("")
						.department("")
						.country("")
						.phone("")
						.cellPhone("")
						.email("")
						.build())
				.economicInformationPdf(EconomicInformationPdf.builder()
						.profession("")
						.job("OCUPAC_01")
						.economicActivity("")
						.codeCiiu("")
						.numberEmployees("")
						.build())
				.companyInformationPdf(CompanyInformationPdf.builder().build())
				.financialInformationPdf(FinancialInformationPdf.builder().build())
				.tributaryInformationPdf(TributaryInformationPdf.builder()
						.incomeDeclarant("SI")
						.withholdingAgent("SI")
						.regimeIva("REGIME_01")
						.declareTaxInAnotherCountry("SI")
						.countryTax(countryTax)
						.originAssetComeFrom("")
						.originAssetComeFromCountry("")
						.originAssetComeFromCity("")
						.build())
				.internationalOperationPdf(InternationalOperationPdf.builder()
						.performsForeignCurrencyOperations("SI")
						.foreignCurrencyList(foreignCurrencyList)
						.build())
				.build();

		simpleDateFormat = new SimpleDateFormat(PDF_PATTERN_DATE);
	}
	

	@Test
	public void getInfoPart1CCTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setDocumentType("TIPDOC_FS001");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart1(info, acquisitionPdfFull, simpleDateFormat);
		assertEquals("X", infoResponse.get("TIPDOC_FS001").getAsString());
	}
	

	
	@Test
	public void getInfoPart1TITest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setDocumentType("TIPDOC_FS004");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart1(info, acquisitionPdfFull, simpleDateFormat);
		assertEquals("X", infoResponse.get("TIPDOC_FS004").getAsString());
	}
	
	@Test
	public void getInfoPart1RCTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setDocumentType("TIPDOC_FS009");
		acquisitionPdfFull.setCompletionDate(null);
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart1(info, acquisitionPdfFull, simpleDateFormat);
		assertNull(infoResponse.get("TIPDOC_FS009"));
	}
	
	@Test
	public void getInfoPart1CETest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setDocumentType("TIPDOC_FS002");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart1(info, acquisitionPdfFull, simpleDateFormat);
		assertEquals("X", infoResponse.get("TIPDOC_FS002").getAsString());
	}
	
	@Test
	public void getInfoPart1PATest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setDocumentType("TIPDOC_FS005");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart1(info, acquisitionPdfFull, simpleDateFormat);
		assertEquals("X", infoResponse.get("TIPDOC_FS005").getAsString());
	}
	
	@Test
	public void getInfoPart1CDTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setDocumentType("TIPDOC_FS000");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart1(info, acquisitionPdfFull, simpleDateFormat);
		assertEquals("X", infoResponse.get("TIPDOC_FS000").getAsString());
	}

	@Test
	public void getInfoPart2GenFTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setGender("GENERO_F");
		acquisitionPdfFull.setNationality("CO_COLOMBIANO");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart2(info, acquisitionPdfFull, simpleDateFormat);
		assertEquals("X", infoResponse.get("GENERO_F").getAsString());
	}
	
	@Test
	public void getInfoPart2GenMTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setGender("GENERO_M");
		acquisitionPdfFull.setNationality("US_ESTADOUNIDENSE");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart2(info, acquisitionPdfFull, simpleDateFormat);
		assertEquals("X", infoResponse.get("GENERO_M").getAsString());
	}
	
	@Test
	public void getInfoPart2NatTest() {
		doReturn("Nationality").when(transformFields).transform(anyString(), any());

		JsonObject info = new JsonObject();
		acquisitionPdfFull.setNationality("US_DEFAULT");
		acquisitionPdfFull.setExpeditionDate(null);
		acquisitionPdfFull.setBirthDate(null);
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart2(info, acquisitionPdfFull, simpleDateFormat);
		assertEquals("Nationality", infoResponse.get("NATIONALITY").getAsString());
	}

	@Test
	public void getInfoMaritalStatusSolteroTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setMatiralStatus("ESTCIV_1");
		JsonObject infoResponse = generatePdfConvertJson.getInfoMaritalStatus(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("ESTCIV_1").getAsString());
	}

	@Test
	public void getInfoMaritalStatusCasadoTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setMatiralStatus("ESTCIV_2");
		JsonObject infoResponse = generatePdfConvertJson.getInfoMaritalStatus(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("ESTCIV_2").getAsString());
	}

	@Test
	public void getInfoMaritalStatusUnionTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setMatiralStatus("ESTCIV_5");
		JsonObject infoResponse = generatePdfConvertJson.getInfoMaritalStatus(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("ESTCIV_5").getAsString());
	}

	@Test
	public void getInfoMaritalStatusDefaultTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.setMatiralStatus("");
		JsonObject infoResponse = generatePdfConvertJson.getInfoMaritalStatus(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart3NullPhoneTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getContactInformationPdf().setPhone(null);
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart3(info, acquisitionPdfFull);
		assertEquals("", infoResponse.get("PHONE").getAsString());
	}
	
	@Test
	public void getInfoPart33Occupation01Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_01");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart33(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_01").getAsString());
	}

	@Test
	public void getInfoPart33Occupation02Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_02");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart33(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_02").getAsString());
	}	
	
	@Test
	public void getInfoPart33Occupation03Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_03");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart33(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_03").getAsString());
	}
	
	@Test
	public void getInfoPart33Occupation04Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_04");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart33(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_04").getAsString());
	}	
	
	@Test
	public void getInfoPart33Occupation05Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_05");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart33(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_05").getAsString());
	}

	@Test
	public void getInfoPart33DefaultTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart33(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}
	
	@Test
	public void getInfoPart333Occupation06Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_06");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_06").getAsString());
	}	
	
	@Test
	public void getInfoPart333Occupation07Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_07");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_07").getAsString());
	}
	
	@Test
	public void getInfoPart333Occupation08Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_08");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_08").getAsString());
	}	
	
	@Test
	public void getInfoPart333Occupation09Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_09");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_09").getAsString());
	}	

	@Test
	public void getInfoPart3333Occupation10Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_10");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart3333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_10").getAsString());
	}	

	@Test
	public void getInfoPart3333Occupation11Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_11");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart3333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_11").getAsString());
	}	

	@Test
	public void getInfoPart3333Occupation12Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_12");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart3333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_12").getAsString());
	}	

	@Test
	public void getInfoPart3333Occupation13Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_13");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart3333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_13").getAsString());
	}	

	@Test
	public void getInfoPart3333Occupation14Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getEconomicInformationPdf().setJob("OCUPAC_14");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart3333(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("OCUPAC_14").getAsString());
	}	
	
	@Test
	public void getInfoPart4NullClosingSalesDateTest() {
		JsonObject info = new JsonObject();
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart4(info, acquisitionPdfFull, simpleDateFormat);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart4ValueClosingSalesDateTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getFinancialInformationPdf().setClosingSalesDate(date);
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart4(info, acquisitionPdfFull, simpleDateFormat);
		assertNotNull(infoResponse);
	}
	
	@Test
	public void getInfoPart5SITest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getTributaryInformationPdf().setIncomeDeclarant("SI");
		acquisitionPdfFull.getTributaryInformationPdf().setWithholdingAgent("SI");
		acquisitionPdfFull.getTributaryInformationPdf().setDeclareTaxInAnotherCountry("SI");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart5(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}
	
	@Test
	public void getInfoPart5NOTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getTributaryInformationPdf().setIncomeDeclarant("NO");
		acquisitionPdfFull.getTributaryInformationPdf().setWithholdingAgent("NO");
		acquisitionPdfFull.getTributaryInformationPdf().setDeclareTaxInAnotherCountry("NO");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart5(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}
	
	@Test
	public void getInfoPart55Regimen01Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getTributaryInformationPdf().setRegimeIva("REGIME_01");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart55(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("REGIME_01").getAsString());
	}

	@Test
	public void getInfoPart55Regimen02Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getTributaryInformationPdf().setRegimeIva("REGIME_02");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart55(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("REGIME_02").getAsString());
	}

	@Test
	public void getInfoPart55Regimen03Test() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getTributaryInformationPdf().setRegimeIva("REGIME_03");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart55(info, acquisitionPdfFull);
		assertEquals("X", infoResponse.get("REGIME_03").getAsString());
	}

	@Test
	public void getInfoPart55DefaultTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getTributaryInformationPdf().setRegimeIva("");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart55(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart6SITest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getInternationalOperationPdf().setPerformsForeignCurrencyOperations("SI");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart6(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}
	
	@Test
	public void getInfoPart6NOTest() {
		JsonObject info = new JsonObject();
		acquisitionPdfFull.getInternationalOperationPdf().setPerformsForeignCurrencyOperations("NO");
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart6(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart6NullCountryTaxTest() {
		JsonObject info = new JsonObject();
		CountryTaxPdf countryTaxPdf = null;
		acquisitionPdfFull.getTributaryInformationPdf().setCountryTax(Arrays.asList(countryTaxPdf));
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart6(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart6SeveralCountryTaxTest() {
		JsonObject info = new JsonObject();
		List<CountryTaxPdf> countryTax = Arrays.asList(
				CountryTaxPdf.builder().country("COLOMBIA").idTax("id").build(),
				CountryTaxPdf.builder().country("ESTADOS UNIDOS").idTax("id").build(),
				CountryTaxPdf.builder().country("CANADA").idTax("id").build(),
				CountryTaxPdf.builder().country("ECUADOR").idTax("id").build(),
				CountryTaxPdf.builder().country("PERU").idTax("id").build());
		acquisitionPdfFull.getTributaryInformationPdf().setCountryTax(countryTax);

		JsonObject infoResponse = generatePdfConvertJson.getInfoPart6(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart7Test() {
		JsonObject info = new JsonObject();
		List<ForeignCurrencyPdf> foreignCurrencyList = Arrays.asList(
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("TIPOPE_002").build(),
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("TIPOPE_003").build(),
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("TIPOPE_004").build(),
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("TIPOPE_005").build(),
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("").build());
		acquisitionPdfFull.getInternationalOperationPdf().setForeignCurrencyList(foreignCurrencyList);

		JsonObject infoResponse = generatePdfConvertJson.getInfoPart7(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart77Test() {
		JsonObject info = new JsonObject();
		List<ForeignCurrencyPdf> foreignCurrencyList = Arrays.asList(
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("TIPOPE_006").build(),
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("TIPOPE_007").build(),
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("TIPOPE_008").build(),
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("TIPOPE_009").build(),
				ForeignCurrencyPdf.builder().foreignCurrencyTransactionType("").build());
		acquisitionPdfFull.getInternationalOperationPdf().setForeignCurrencyList(foreignCurrencyList);

		JsonObject infoResponse = generatePdfConvertJson.getInfoPart77(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart8NullForeignCurrencyTest() {
		JsonObject info = new JsonObject();
		ForeignCurrencyPdf foreignCurrencyPdf = null;
		acquisitionPdfFull.getInternationalOperationPdf().setForeignCurrencyList(Arrays.asList(foreignCurrencyPdf));
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart8(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart8SeveralForeignCurrencyTest() {
		JsonObject info = new JsonObject();
		List<ForeignCurrencyPdf> foreignCurrencyList = Arrays.asList(
				ForeignCurrencyPdf.builder()
						.entityName("").productType("TIPOPR_02").productNumber("")
						.averageMonthlyAmount("").currency("").city("").country("")
						.foreignCurrencyTransactionType("TIPOPE_002").build(),
				ForeignCurrencyPdf.builder()
						.entityName("").productType("TIPOPR_03").productNumber("")
						.averageMonthlyAmount("").currency("").city("").country("")
						.foreignCurrencyTransactionType("TIPOPE_003").build(),
				ForeignCurrencyPdf.builder()
						.entityName("").productType("TIPOPR_04").productNumber("")
						.averageMonthlyAmount("").currency("").city("").country("")
						.foreignCurrencyTransactionType("TIPOPE_004").build());
		acquisitionPdfFull.getInternationalOperationPdf().setForeignCurrencyList(foreignCurrencyList);

		JsonObject infoResponse = generatePdfConvertJson.getInfoPart8(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart8DefaultTest() {
		JsonObject info = new JsonObject();
		List<ForeignCurrencyPdf> foreignCurrencyList = Arrays.asList(
				ForeignCurrencyPdf.builder()
						.entityName("").productType("").productNumber("")
						.averageMonthlyAmount("").currency("").city("").country("")
						.foreignCurrencyTransactionType("TIPOPE_002").build());
		acquisitionPdfFull.getInternationalOperationPdf().setForeignCurrencyList(foreignCurrencyList);

		JsonObject infoResponse = generatePdfConvertJson.getInfoPart8(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart88NullForeignCurrencyTest() {
		JsonObject info = new JsonObject();
		ForeignCurrencyPdf foreignCurrencyPdf = null;
		acquisitionPdfFull.getInternationalOperationPdf().setForeignCurrencyList(Arrays.asList(foreignCurrencyPdf));
		JsonObject infoResponse = generatePdfConvertJson.getInfoPart88(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}

	@Test
	public void getInfoPart88SeveralForeignCurrencyTest() {
		JsonObject info = new JsonObject();
		List<ForeignCurrencyPdf> foreignCurrencyList = Arrays.asList(
				ForeignCurrencyPdf.builder()
						.entityName("").productType("TIPOPR_02").productNumber("")
						.averageMonthlyAmount("").currency("").city("").country("")
						.foreignCurrencyTransactionType("TIPOPE_002").build(),
				ForeignCurrencyPdf.builder()
						.entityName("").productType("TIPOPR_03").productNumber("")
						.averageMonthlyAmount("").currency("").city("").country("")
						.foreignCurrencyTransactionType("TIPOPE_003").build(),
				ForeignCurrencyPdf.builder()
						.entityName("").productType("TIPOPR_04").productNumber("")
						.averageMonthlyAmount("").currency("").city("").country("")
						.foreignCurrencyTransactionType("TIPOPE_004").build());
		acquisitionPdfFull.getInternationalOperationPdf().setForeignCurrencyList(foreignCurrencyList);

		JsonObject infoResponse = generatePdfConvertJson.getInfoPart88(info, acquisitionPdfFull);
		assertNotNull(infoResponse);
	}
	
	@Test
	public void getInfoTest() {
		JsonObject infoResponse = generatePdfConvertJson.getInfo(acquisitionPdfFull);
		assertNotNull(infoResponse);
	}
}
