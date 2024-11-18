package com.javathehutt.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.javathehutt.GDP;
import com.javathehutt.dto.GDPDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GDPConverterTest {

  private static GDPConverter converter;
  private static String filePathMockData;

  private static GDP resultOne;
  private static GDP resultTwo;

  @BeforeEach
  void init() {
    converter = new GDPConverter();
    filePathMockData =
      "src\\test\\java\\com\\javathehutt\\converters\\GDPMockData.json";

    try {
      String strMock = new String(Files.readAllBytes(Paths.get(filePathMockData)));
      JSONArray combinedArray = new JSONArray(strMock);

      JSONObject GDPObjIncorrect = combinedArray.getJSONObject(0);
      JSONObject GDPObjTwo = combinedArray.getJSONObject(1);

      resultOne = converter.doForward(GDPObjIncorrect);
      resultTwo = converter.doForward(GDPObjTwo);

    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  void delete() {
    resultOne = null;
    resultTwo = null;
  }

  @Test
  public void doForwardTestResultShouldNotBeNull() {
    assertNotNull(resultOne);
    assertNotNull(resultTwo);
  }

  @Test
  public void doForwardTestResultAmount() {
    assertEquals(3, resultOne.getValues().size());
    assertEquals(4, resultTwo.getValues().size());
  }

  @Test
  public void doForwardTestDataEntries() {
    // in dtos the years go in reverse order, from highest to lowest.
    // this is the reason for i and j going in different directions

    ArrayList<GDPDto> dtosOne = resultOne.getValues();
    List<Integer> yearsOne = List.of(1980, 1981, 1982);
    List<Double> valuesOne = List.of(10.105, 11.123, 12.456);

    int i = 0;
    for(int j = dtosOne.size() - 1; j >= 0; j--) {
      assertEquals(yearsOne.get(i), dtosOne.get(j).getYear());
      assertEquals(valuesOne.get(i), dtosOne.get(j).getValue());
      i++;
    }

    ArrayList<GDPDto> dtosTwo = resultTwo.getValues();
    List<Integer> yearsTwo = List.of(2000, 2001, 2002, 2003);
    List<Double> valuesTwo = List.of(1.0, 1.2, 2.23, 1.234);
    
    i = 0;
    for(int j = dtosTwo.size() - 1; j >= 0; j--) {
      assertEquals(yearsTwo.get(i), dtosTwo.get(j).getYear());
      assertEquals(valuesTwo.get(i), dtosTwo.get(j).getValue());
      i++;
    }
  }

  @Test
  public void doForwardTestEmptyJSONObject() {
    JSONObject jsonObject = new JSONObject();

    GDP resultEmpty = converter.doForward(jsonObject);

    assertNotNull(resultEmpty);
    assertEquals(0, resultEmpty.getValues().size());
  }

  @Test
  public void doForwardTestInvalidDataFormat() {
    // mock data, incorrect formats
    JSONObject GDPObjIncorrect = new JSONObject();

    GDPObjIncorrect.put("year", 10.105);
    GDPObjIncorrect.put("1981", 11.123);
    GDPObjIncorrect.put("1983", "carlos");

    assertThrows(JSONException.class, () -> converter.doForward(GDPObjIncorrect));
  }
}
