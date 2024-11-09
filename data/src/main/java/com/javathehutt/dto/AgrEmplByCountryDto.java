package com.javathehutt.dto;

import com.javathehutt.dto.helpers.Country;
import com.javathehutt.dto.helpers.Indicator;
import javax.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

/**
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
 */
@Builder
@Data
public class AgrEmplByCountryDto {
  private Indicator indicator;
  private Country country;
  private String countryIso3Code;
  private int year;
  @Nullable private Double value;
  private String unit;
  private String obsStatus;
  private int decimal;
}
