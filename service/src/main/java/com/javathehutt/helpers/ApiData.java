package com.javathehutt.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiData {
  private JSONObject jsonObject;
  private JSONArray jsonArray;

  public ApiData(JSONObject jsonObject) {
    this.jsonObject = jsonObject;
  }

  public ApiData(JSONArray jsonArray) {
    this.jsonArray = jsonArray;
  }

  public JSONObject getJsonObject() {
    return jsonObject;
  }

  public JSONArray getJsonArray() {
    return jsonArray;
  }

  @Override
  public String toString() {
    return jsonObject != null ? jsonObject.toString() : jsonArray.toString();
  }
}
