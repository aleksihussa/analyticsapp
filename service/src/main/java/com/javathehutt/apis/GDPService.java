package com.javathehutt.apis;

import com.javathehutt.Service;
import com.javathehutt.helpers.ApiData;
import org.json.JSONObject;

public class GDPService implements ApiService {
  private static final String BASE_URL =
      "https://www.imf.org/external/datamapper/api/v1/indicator/NGDPD/country";

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
   *
   * Start and end year are not needed for now, maybe use them in data
   * converter at some point?
   */
  @Override
  public ApiData fetchData(String countryIso3Code, int startYear, int endYear) {
    countryIso3Code = countryIso3Code.toUpperCase();

    String url = String.format("%s/%s", BASE_URL, countryIso3Code);
    String responseBody = Service.fetchUrl(url);

    if (responseBody.isEmpty()) {
      System.err.println("Encountered an error while fetching gdp api");
      return new ApiData(new JSONObject());
    }

    JSONObject bodyJson = new JSONObject(responseBody);
    JSONObject valueObj = bodyJson.getJSONObject("values");
    JSONObject gdpObj = valueObj.getJSONObject("NGDPD");
    JSONObject gdpData = gdpObj.getJSONObject(countryIso3Code);

    // Do something with data

    // System.out.println(new GDPConverter().doForward(gdpData));
    return new ApiData(gdpData);
  }
}
