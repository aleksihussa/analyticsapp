package com.javathehutt.converters;

import com.google.common.base.Converter;
import com.javathehutt.AgrEmplByCountry;
import com.javathehutt.dto.AgrEmplByCountryDto;
import com.javathehutt.dto.helpers.Country;
import com.javathehutt.dto.helpers.Indicator;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class AgrEmplByCountryConverter extends Converter<JSONArray, AgrEmplByCountry> {

  @Override
  public AgrEmplByCountry doForward(JSONArray a) {
    ArrayList<AgrEmplByCountryDto> values = new ArrayList<AgrEmplByCountryDto>();

    a.forEach(
        (Object o) -> {
          JSONObject obj = (JSONObject) o;
          values.add(
              AgrEmplByCountryDto.builder()
                  .indicator(
                      Indicator.builder()
                          .id(obj.getJSONObject("indicator").getString("id"))
                          .value(obj.getJSONObject("indicator").getString("value"))
                          .build())
                  .country(
                      Country.builder()
                          .id(obj.getJSONObject("country").getString("id"))
                          .value(obj.getJSONObject("country").getString("value"))
                          .build())
                  .countryIso3Code(obj.getString("countryiso3code"))
                  .year(Integer.parseInt(obj.getString("date")))
                  .value(obj.isNull("value") ? null : obj.getDouble("value"))
                  .unit(obj.getString("unit"))
                  .obsStatus(obj.getString("obs_status"))
                  .decimal(obj.getInt("decimal"))
                  .build());
        });

    // values.sort(Comparator.comparing(AgrEmplByCountryDto::getDate));

    return AgrEmplByCountry.builder().values(values).build();
  }

  @Override
  protected JSONArray doBackward(AgrEmplByCountry b) {
    throw new UnsupportedOperationException("Unimplemented method 'doBackward'");
  }
}
