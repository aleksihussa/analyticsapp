package com.javathehutt.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.javathehutt.AgrEmplByCountry;
import com.javathehutt.dto.AgrEmplByCountryDto;

class AgrEmplByCountryConverterTest {

  private AgrEmplByCountryConverter converter;

  private AgrEmplByCountry resultUSA;
  private List<AgrEmplByCountryDto> valuesUSA;

  private AgrEmplByCountry resultFI;
  private List<AgrEmplByCountryDto> valuesFI;

  @BeforeEach
  void init() {
    // initialize converter to use for mock data
    converter = new AgrEmplByCountryConverter();
  }

  void initWorking() {

    //usa mock data
    JSONArray agrJSONUSA = new JSONArray();
    JSONObject agrObjUSA = new JSONObject();

    agrObjUSA.put("countryiso3code", "USA");
    agrObjUSA.put("date", "2022");
    agrObjUSA.put("value", 45.5);
    agrObjUSA.put("unit", "");
    agrObjUSA.put("obs_status", "");
    agrObjUSA.put("decimal", 2);

    JSONObject indicatorUSA = new JSONObject();
    indicatorUSA.put("id", "SL.AGR.EMPL.ZS");
    indicatorUSA.put("value", "Employment in agriculture (% of total employment) (modeled ILO estimate)");
    agrObjUSA.put("indicator", indicatorUSA);

    JSONObject countryUSA = new JSONObject();
    countryUSA.put("id", "US");
    countryUSA.put("value", "United States");
    agrObjUSA.put("country", countryUSA);

    agrJSONUSA.put(agrObjUSA);

    // convert JSON array to AgrEmplByCountry object
    resultUSA = converter.doForward(agrJSONUSA);
    valuesUSA = resultUSA.getValues();

    // finland mock data

    JSONArray agrJSONFI = new JSONArray();
    JSONObject agrObjFI = new JSONObject();

    agrObjFI.put("countryiso3code", "FI");
    agrObjFI.put("date", "2020");
    agrObjFI.put("value", 5.52699004089471);
    agrObjFI.put("unit", "test unit");
    agrObjFI.put("obs_status", "test status");
    agrObjFI.put("decimal", 5);

    JSONObject indicatorFI = new JSONObject();
    indicatorFI.put("id", "silly test text");
    indicatorFI.put("value", "another silly test text");
    agrObjFI.put("indicator", indicatorFI);

    JSONObject countryFI = new JSONObject();
    countryFI.put("id", "FI");
    countryFI.put("value", "Finland");
    agrObjFI.put("country", countryFI);

    // put object twice to check if size is right
    agrJSONFI.put(agrObjFI);
    agrJSONFI.put(agrObjFI);

    // convert JSON array to AgrEmplByCountry object
    resultFI = converter.doForward(agrJSONFI);
    valuesFI = resultFI.getValues();
  }

  @Test
  public void doForwardTestResultShouldNotBeNull() {
    initWorking();
    assertNotNull(resultUSA);
    assertNotNull(resultFI);
  }

  @Test
  public void doForwardTestValueInputAmount() {
    initWorking();
    assertEquals(1, valuesUSA.size());
    assertEquals(2, valuesFI.size());
  }

  @Test
  public void doForwardTestDataEntries() {
    initWorking();

    AgrEmplByCountryDto dtoUSA = valuesUSA.get(0);
    assertEquals("USA", dtoUSA.getCountryIso3Code());
    assertEquals(2022, dtoUSA.getYear());
    assertEquals(45.5, dtoUSA.getValue());
    assertEquals("", dtoUSA.getUnit());
    assertEquals("", dtoUSA.getObsStatus());
    assertEquals(2, dtoUSA.getDecimal());

    assertEquals("SL.AGR.EMPL.ZS", dtoUSA.getIndicator().getId());
    assertEquals("Employment in agriculture (% of total employment) (modeled ILO estimate)", dtoUSA.getIndicator().getValue());
    
    assertEquals("US", dtoUSA.getCountry().getId());
    assertEquals("United States", dtoUSA.getCountry().getValue());

    AgrEmplByCountryDto dtoFI = valuesFI.get(0);
    assertEquals("FI", dtoFI.getCountryIso3Code());
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
    //usa mock data, incorrect formats
    JSONArray agrJSONUSA = new JSONArray();
    JSONObject agrObjUSA = new JSONObject();

    agrObjUSA.put("countryiso3code", "USA");
    agrObjUSA.put("date", "vuosi");
    agrObjUSA.put("value", 45.5);
    agrObjUSA.put("unit", "");
    agrObjUSA.put("obs_status", "");
    agrObjUSA.put("decimal", 2);

    JSONObject indicatorUSA = new JSONObject();
    indicatorUSA.put("id", "SL.AGR.EMPL.ZS");
    indicatorUSA.put("value", "Employment in agriculture (% of total employment) (modeled ILO estimate)");
    agrObjUSA.put("indicator", indicatorUSA);

    JSONObject countryUSA = new JSONObject();
    countryUSA.put("id", "US");
    countryUSA.put("value", "United States");
    agrObjUSA.put("country", countryUSA);

    agrJSONUSA.put(agrObjUSA);

    assertThrows(NumberFormatException.class, () -> converter.doForward(agrJSONUSA));
  }
}
