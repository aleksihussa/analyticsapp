package com.javathehutt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GDPDto {
  private int year;
  private double value;
}
