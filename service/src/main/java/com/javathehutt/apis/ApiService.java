package com.javathehutt.apis;

import com.javathehutt.helpers.ApiData;

public interface ApiService {
  ApiData fetchData(String countryIso3Code, int startYear, int endYear);
}
