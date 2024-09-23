package com.javathehutt;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class Service {

  private static final String BASE_TRACTOR_API_URL = "https://api.worldbank.org/v2/country";
  private static final String BASE_GDP_API_URL =
      "https://www.imf.org/external/datamapper/api/v1/indicator/NGDPD/country";

  public static void service() {
    System.out.println("Service");
  }

  public static void testService() {
    JSONArray tractors = Service.getTractorsByCountry("fin", 2000, 2005);
    JSONObject gdpYears = Service.getGdpByCountry("fin");

    System.out.println("Tractors Finland:");
    for (int i = 0; i < tractors.length(); i++) {
      JSONObject data = tractors.getJSONObject(i);
      System.out.println(data.toString());
    }

    System.out.println("GPD Finland:");
    for (String year : gdpYears.keySet()) {
      double value = gdpYears.getDouble(year);
      System.out.println("Year: " + year + ", Value: " + value);
    }
  }

  /*
   * Return array format: [
   *  {
   *    indicator:        {id: AG.AGR.TRAC.NO, value: <id explanation>},
   *    country:          {id: <code> , value: <country name>},
   *    countryiso3code:  <country iso3 code>,
   *    date:             <year>,
   *    value:            <number of tractors> || null,
   *    unit:             <empty string>,
   *    obs_status:       <empty string>,
   *    decimal:          0
   *  }
   * ]
   *
   * Relevant fields are date and value
   *
   */
  public static JSONArray getTractorsByCountry(String countryIso3Code, int startYear, int endYear) {
    String url =
        String.format(
            "%s/%s/indicator/AG.AGR.TRAC.NO?date=%d:%d&format=json",
            BASE_TRACTOR_API_URL, countryIso3Code, startYear, endYear);

    String responseBody = fetchData(url);

    if (responseBody.isEmpty()) {
      System.err.println("Encountered an error while fetching tractor api");
      return new JSONArray();
    }

    JSONArray bodyJson = new JSONArray(responseBody);
    JSONArray tractorData = bodyJson.getJSONArray(1);

    // Do something with data

    return tractorData;
  }

  /*
   * Return object format (example years 2000-2003):
   * {
   *    2000: 126.075,
   *    2001: 129.534,
   *    2002: 140.305,
   *    2003: 171.609,
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
}
