package com.javathehutt.converters;

import org.json.JSONArray;

import com.google.common.base.Converter;

public class TractorConverter extends Converter<JSONArray, TractorEntity> {

  @Override
  protected TractorEntity doForward(JSONArray a) {
    throw new UnsupportedOperationException("Unimplemented method 'doForward'");
  }

  @Override
  protected JSONArray doBackward(TractorEntity b) {
    throw new UnsupportedOperationException("Unimplemented method 'doBackward'");
  }

}
