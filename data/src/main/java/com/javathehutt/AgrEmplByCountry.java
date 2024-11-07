package com.javathehutt;

import com.javathehutt.dto.AgrEmplByCountryDto;
import java.util.ArrayList;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class AgrEmplByCountry {
  @Builder.Default ArrayList<AgrEmplByCountryDto> values = new ArrayList<AgrEmplByCountryDto>();
}
