package com.javathehutt;

import com.javathehutt.converters.AgrEmplByCountryConverter;
import com.javathehutt.converters.GDPConverter;
import com.javathehutt.dto.helpers.Country;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Service {

  private static final String BASE_AGRICULTURE_API_URL = "https://api.worldbank.org/v2/country";
  private static final String BASE_GDP_API_URL =
      "https://www.imf.org/external/datamapper/api/v1/indicator/NGDPD/country";

  public static void service() {
    System.out.println("Service");
    // call testService here if you want to test it automatically
    testService();
  }

  public static void testService() {
    // agrEmpl = Agriculture employment
    JSONArray agrEmplPercentage = Service.getAgrEmplByCountry("fin", 2000, 2005);
    JSONObject gdpYears = Service.getGdpByCountry("fin");

    // System.out.println("Agriculture % Finland:");
    // for (int i = 0; i < agrEmplPercentage.length(); i++) {
    // JSONObject data = agrEmplPercentage.getJSONObject(i);
    // System.out.println(data.toString());
    // }

    // System.out.println("GPD Finland:");
    // for (String year : gdpYears.keySet()) {
    // double value = gdpYears.getDouble(year);
    // System.out.println("Year: " + year + ", Value: " + value);
    // }
  }

  /*
   * Return array format: [
   * {
   * indicator: {id: AG.AGR.TRAC.NO, value: <id explanation>},
   * country: {id: <code> , value: <country name>},
   * countryiso3code: <country iso3 code>,
   * date: <year (string)>,
   * value: <agriculture % of total employment (float)> || null,
   * unit: <empty string>,
   * obs_status: <empty string>,
   * decimal: 0
   * }
   * ]
   *
   * Relevant fields are date and value
   */
  public static JSONArray getAgrEmplByCountry(String countryIso3Code, int startYear, int endYear) {
    String url =
        String.format(
            "%s/%s/indicator/SL.AGR.EMPL.ZS?date=%d:%d&format=json",
            BASE_AGRICULTURE_API_URL, countryIso3Code, startYear, endYear);

    String responseBody = fetchData(url);

    if (responseBody.isEmpty()) {
      System.err.println("Encountered an error while fetching agriculture api");
      return new JSONArray();
    }

    JSONArray bodyJson = new JSONArray(responseBody);
    JSONArray agricultureData = bodyJson.getJSONArray(1);

    // Do something with data

    System.out.println(new AgrEmplByCountryConverter().doForward(agricultureData));
    return agricultureData;
  }

  /*
   * Return object format (example years 2000-2003):
   * {
   * 2000: 126.075,
   * 2001: 129.534,
   * 2002: 140.305,
   * 2003: 171.609,
   * }
   *
   * value unit is "Billions of U.S. dollars"
   */
  public static JSONObject getGdpByCountry(String countryIso3Code) {
    countryIso3Code = countryIso3Code.toUpperCase();

    String url = String.format("%s/%s", BASE_GDP_API_URL, countryIso3Code);
    String responseBody = fetchData(url);

    if (responseBody.isEmpty()) {
      System.err.println("Encountered an error while fetching gdp api");
      return new JSONObject();
    }

    JSONObject bodyJson = new JSONObject(responseBody);
    JSONObject valueObj = bodyJson.getJSONObject("values");
    JSONObject gpdObj = valueObj.getJSONObject("NGDPD");
    JSONObject gdpData = gpdObj.getJSONObject(countryIso3Code);

    // Do something with data

    System.out.println(new GDPConverter().doForward(gdpData));
    return gdpData;
  }

  private static String fetchData(String url) {
    try {
      HttpClient client = HttpClient.newHttpClient();

      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return response.body();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public List<Country> getCountries() {
    return getCountriesFromJsonFile("countries.json");
  }

  public static List<Country> getCountriesFromJsonFile(String filePath) {
    List<Country> countries = new ArrayList<>();
    try (InputStream inputStream = Service.class.getClassLoader().getResourceAsStream(filePath)) {
      if (inputStream == null) {
        throw new IllegalArgumentException("File not found: " + filePath);
      }
      String jsonText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      JSONObject countriesObject = new JSONObject(jsonText);
      for (String countryName : countriesObject.keySet()) {
        String iso3Code = countriesObject.getString(countryName);
        Country country = Country.builder().id(iso3Code).value(countryName).build();
        countries.add(country);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Failed to load countries.");
    }

    Collections.sort(countries, Comparator.comparing(Country::getValue));

    return countries;
  }
}
