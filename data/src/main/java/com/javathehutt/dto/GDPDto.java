package com.javathehutt.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class GDPDto {

  private int year;
  private double value;
}
