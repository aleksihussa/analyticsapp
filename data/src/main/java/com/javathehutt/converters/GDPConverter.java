package com.javathehutt.converters;

import com.google.common.base.Converter;
import com.javathehutt.entities.GDPEntity;
import org.json.JSONArray;

public class GDPConverter extends Converter<JSONArray, GDPEntity> {

  @Override
  protected GDPEntity doForward(JSONArray a) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'doForward'");
  }

  @Override
  protected JSONArray doBackward(GDPEntity b) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'doBackward'");
  }
}
