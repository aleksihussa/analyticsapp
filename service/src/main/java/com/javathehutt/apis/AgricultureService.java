/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.javathehutt.apis;

import com.javathehutt.Service;
import com.javathehutt.helpers.ApiData;
import org.json.JSONArray;

public class AgricultureService implements ApiService {

  private static final String BASE_URL = "https://api.worldbank.org/v2/country";

  /*
   * Return array format: [
   * {
   * indicator: {id: AG.AGR.TRAC.NO, value: <id explanation>},
   * country: {id: <code> , value: <country name>},
   * countryiso3code: <country iso3 code>,
   * year: <year (int)>,
   * value: <agriculture % of total employment (float)> || null,
   * unit: <empty string>,
   * obs_status: <empty string>,
   * decimal: 0
   * }
   * ]
   *
   * Relevant fields are date and value
   */
  @Override
  public ApiData fetchData(String countryIso3Code, int startYear, int endYear) {
    String url =
        String.format(
            "%s/%s/indicator/SL.AGR.EMPL.ZS?date=%d:%d&format=json",
            BASE_URL, countryIso3Code, startYear, endYear);

    String responseBody = Service.fetchUrl(url);

    if (responseBody.isEmpty()) {
      System.err.println("Encountered an error while fetching agriculture api");
      return new ApiData(new JSONArray());
    }

    JSONArray bodyJson = new JSONArray(responseBody);
    JSONArray agricultureData = bodyJson.getJSONArray(1);

    // Do something with data

    // System.out.println(new AgrEmplByCountryConverter().doForward(agricultureData));
    return new ApiData(agricultureData);
  }
}
