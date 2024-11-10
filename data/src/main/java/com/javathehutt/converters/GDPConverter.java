package com.javathehutt.converters;

import com.google.common.base.Converter;
import com.javathehutt.GDP;
import com.javathehutt.dto.GDPDto;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

public class GDPConverter extends Converter<JSONObject, GDP> {

  @Override
  public GDP doForward(JSONObject obj) {

    final ArrayList<GDPDto> container = new ArrayList<GDPDto>();
    final Iterator<String> keys = obj.keys();

    while (keys.hasNext()) {
      String yearKey = keys.next();
      int year = Integer.parseInt(yearKey);
      double value = obj.getDouble(yearKey);

      GDPDto gdoDto = GDPDto.builder().year(year).value(value).build();
      container.add(gdoDto);
    }

    // Sort by year (if needed later)
    // container.sort(Comparator.comparing(GDPDto::getYear));

    return GDP.builder().values(container).build();
  }

  @Override
  protected JSONObject doBackward(GDP gdp) {
    // TODO will be implemented if needed
    throw new UnsupportedOperationException("Unimplemented method 'doBackward'");
  }
}
