package com.javathehutt;

import com.javathehutt.dto.AgrEmplByCountryDto;
import java.util.ArrayList;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class AgrEmplByCountry {
  @Builder.Default ArrayList<AgrEmplByCountryDto> values = new ArrayList<AgrEmplByCountryDto>();
}
