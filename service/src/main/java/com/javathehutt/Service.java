package com.javathehutt;

import com.javathehutt.apis.ApiService;
import com.javathehutt.dto.helpers.Country;
import com.javathehutt.helpers.ApiData;
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
import org.json.JSONObject;

public class Service {
  public static void service() {
    System.out.println("Service");
    // call testService here if you want to test it automatically
    testService();
  }

  public static void testService() {
    ApiServiceFactory apiFactory = new ApiServiceFactory();

    // agrEmpl = Agriculture employment
    ApiService agrEmplService = apiFactory.createService("agriculture");
    ApiData agrEmplData = agrEmplService.fetchData("fin", 2000, 2005);
    System.out.println("Agriculture data: " + agrEmplData.toString());

    ApiService gdpService = apiFactory.createService("gdp");
    ApiData gdpData = gdpService.fetchData("fin", 0, 0);
    System.out.println("GDP data: " + gdpData.toString());
  }

  public static String fetchUrl(String url) {
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
