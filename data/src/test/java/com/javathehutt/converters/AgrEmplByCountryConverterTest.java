package com.javathehutt.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.javathehutt.AgrEmplByCountry;
import com.javathehutt.dto.AgrEmplByCountryDto;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgrEmplByCountryConverterTest {

  private static String filePathMockData;

  private static AgrEmplByCountryConverter converter;

  private static AgrEmplByCountry resultUSA;
  private static List<AgrEmplByCountryDto> valuesUSA;

  private static AgrEmplByCountry resultFI;
  private static List<AgrEmplByCountryDto> valuesFI;

  private static JSONArray agrJSONIncorrect;

  @BeforeEach
  void init() {
    // initialize converter to use for mock data
    converter = new AgrEmplByCountryConverter();
    filePathMockData =
        "src\\test\\java\\com\\javathehutt\\converters\\AgrEmplByCountryMockData.json";
    try {
      String strMock = new String(Files.readAllBytes(Paths.get(filePathMockData)));

      JSONArray combinedArray = new JSONArray(strMock);

      JSONArray agrJSONFI = combinedArray.getJSONArray(0);
      JSONArray agrJSONUSA = combinedArray.getJSONArray(1);
      agrJSONIncorrect = combinedArray.getJSONArray(2);

      // convert JSON array to AgrEmplByCountry object
      resultUSA = converter.doForward(agrJSONUSA);
      valuesUSA = resultUSA.getValues();

      // convert JSON array to AgrEmplByCountry object
      resultFI = converter.doForward(agrJSONFI);
      valuesFI = resultFI.getValues();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  void delete() {
    resultUSA = null;
    resultFI = null;
    valuesUSA = null;
    valuesFI = null;
  }

  @Test
  public void doForwardTestResultShouldNotBeNull() {
    assertNotNull(resultUSA);
    assertNotNull(resultFI);
  }

  @Test
  public void doForwardTestValueInputAmount() {
    assertEquals(3, valuesUSA.size());
    assertEquals(4, valuesFI.size());
  }

  @Test
  public void doForwardTestDataEntries() {

    AgrEmplByCountryDto dtoUSA = valuesUSA.get(0);
    assertEquals("USA", dtoUSA.getCountryIso3Code());
    assertEquals(2004, dtoUSA.getYear());
    assertEquals(45.5, dtoUSA.getValue());
    assertEquals("", dtoUSA.getUnit());
    assertEquals("", dtoUSA.getObsStatus());
    assertEquals(2, dtoUSA.getDecimal());

    assertEquals("SL.AGR.EMPL.ZS", dtoUSA.getIndicator().getId());
    assertEquals(
        "Employment in agriculture (% of total employment) (modeled ILO estimate)",
        dtoUSA.getIndicator().getValue());

    assertEquals("US", dtoUSA.getCountry().getId());
    assertEquals("United States", dtoUSA.getCountry().getValue());

    AgrEmplByCountryDto dtoFI = valuesFI.get(0);

    assertEquals("FIN", dtoFI.getCountryIso3Code());
    assertEquals(2020, dtoFI.getYear());
    assertEquals(5.52699004089471, dtoFI.getValue());
    assertEquals("test unit", dtoFI.getUnit());
    assertEquals("test status", dtoFI.getObsStatus());
    assertEquals(5, dtoFI.getDecimal());

    assertEquals("silly test text", dtoFI.getIndicator().getId());
    assertEquals("another silly test text", dtoFI.getIndicator().getValue());

    assertEquals("FI", dtoFI.getCountry().getId());
    assertEquals("Finland", dtoFI.getCountry().getValue());
  }

  @Test
  public void doForwardTestEmptyJSONArray() {
    JSONArray jsonArray = new JSONArray();

    AgrEmplByCountry resultEmpty = converter.doForward(jsonArray);

    assertNotNull(resultEmpty);
    assertEquals(0, resultEmpty.getValues().size());
  }

  @Test
  public void doForwardTestInvalidDataFormat() {
    // usa mock data, incorrect formats
    assertThrows(NumberFormatException.class,
      () -> converter.doForward(agrJSONIncorrect));
  }
}
