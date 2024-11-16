package com.javathehutt.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.javathehutt.GDP;
import com.javathehutt.dto.GDPDto;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GDPConverterTest {

  private GDPConverter converter;

  private GDP resultUSA;

  private GDP resultFI;

  @BeforeEach
  void init() {
    converter = new GDPConverter();
  }

  void initWorking() {

    // usa mock data
    JSONObject GDPObjUSA = new JSONObject();

    GDPObjUSA.put("1980", 10.105);
    GDPObjUSA.put("1981", 11.123);
    GDPObjUSA.put("1983", 12.456);

    resultUSA = converter.doForward(GDPObjUSA);

    // fin mock data
    JSONObject GDPObjFIN = new JSONObject();

    GDPObjFIN.put("2000", 1);
    GDPObjFIN.put("2001", 1.2);
    GDPObjFIN.put("2002", 1.234);
    GDPObjFIN.put("2003", 1.23);

    resultFI = converter.doForward(GDPObjFIN);
  }

  @Test
  public void doForwardTestResultShouldNotBeNull() {
    initWorking();
    assertNotNull(resultUSA);
    assertNotNull(resultFI);
  }

  @Test
  public void doForwardTestResultAmount() {
    initWorking();
    assertEquals(3, resultUSA.getValues().size());
    assertEquals(4, resultFI.getValues().size());
  }

  @Test
  public void doForwardTestDataEntries() {
    initWorking();

    ArrayList<GDPDto> dtosUSA = resultUSA.getValues();
    assertEquals(1980, dtosUSA.get(2).getYear());
    assertEquals(1981, dtosUSA.get(1).getYear());
    assertEquals(1983, dtosUSA.get(0).getYear());

    assertEquals(10.105, dtosUSA.get(2).getValue());
    assertEquals(11.123, dtosUSA.get(1).getValue());
    assertEquals(12.456, dtosUSA.get(0).getValue());

    ArrayList<GDPDto> dtosFI = resultFI.getValues();
    assertEquals(2000, dtosFI.get(3).getYear());
    assertEquals(2001, dtosFI.get(2).getYear());
    assertEquals(2002, dtosFI.get(1).getYear());
    assertEquals(2003, dtosFI.get(0).getYear());

    assertEquals(1, dtosFI.get(3).getValue());
    assertEquals(1.2, dtosFI.get(2).getValue());
    assertEquals(1.234, dtosFI.get(1).getValue());
    assertEquals(1.23, dtosFI.get(0).getValue());
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
    // usa mock data, incorrect formats
    JSONObject GDPObjUSA = new JSONObject();

    GDPObjUSA.put("year", 10.105);
    GDPObjUSA.put("1981", 11.123);
    GDPObjUSA.put("1983", "carlos");

    assertThrows(JSONException.class, () -> converter.doForward(GDPObjUSA));
  }
}
