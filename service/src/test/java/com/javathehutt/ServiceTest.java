package com.javathehutt;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.javathehutt.dto.helpers.Country;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ServiceTest {
  // fetchurl is indirectly tested by the agriculture and gdp service tests.

  static String filepathCorrect;
  static String filepathIncorrect;

  @BeforeAll
  static void init() {
    filepathCorrect = "countries.json";
    filepathIncorrect = "wrongfilepath.xyz";
  }

  @Test
  void testGetCountriesShouldNotReturnNull() {
    List<Country> countries = Service.getCountriesFromJsonFile(filepathCorrect);
    assertNotNull(countries);
  }

  @Test
  void testGetCountriesShouldHaveCountries() {
    // if it has finland it probably has loaded correctly
    List<Country> countries = Service.getCountriesFromJsonFile(filepathCorrect);
    Country finland = Country.builder().id("fin").value("Finland").build();
    assertTrue(countries.contains(finland));
    assertTrue(190 < countries.size());
  }

  @Test
  void testGetCountriesShouldReturnEmptyIfNoFileFound() {
    List<Country> countries = Service.getCountriesFromJsonFile(filepathIncorrect);
    assertTrue(countries.isEmpty());
  }
}
