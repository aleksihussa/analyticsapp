package com.javathehutt.dto.helpers;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Country {
  private String id;
  private String value;
}
