package com.javathehutt;

import com.javathehutt.dto.GDPDto;
import java.util.ArrayList;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class GDP {

  @Builder.Default ArrayList<GDPDto> values = new ArrayList<GDPDto>();

  // if ui requires here we can implement methods to manipulate the data
  // or other fields like normalization, etc. can be implemented here

  // Getter for values
  public ArrayList<GDPDto> getValues() {
    return values;
  }
}
