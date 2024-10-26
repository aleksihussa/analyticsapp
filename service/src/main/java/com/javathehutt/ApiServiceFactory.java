package com.javathehutt;

import com.javathehutt.apis.AgricultureService;
import com.javathehutt.apis.ApiService;
import com.javathehutt.apis.GDPService;

public class ApiServiceFactory {
  public ApiService createService(String apiType) {
    if (apiType == null || apiType.isEmpty()) {
      return null;
    }

    switch (apiType.toLowerCase()) {
      case "gdp" -> {
        return new GDPService();
      }
      case "agriculture" -> {
        return new AgricultureService();
      }
      default -> throw new IllegalArgumentException("Unknown api type: " + apiType);
    }
  }
}
