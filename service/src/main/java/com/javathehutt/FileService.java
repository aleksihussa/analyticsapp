package com.javathehutt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public interface FileService {

  /**
   * @param filePath, full valid path to the file, starting from the level project is run from
   * @return jsonElement
   * @throws Exception
   */
  public static JsonElement read(String filePath) throws Exception {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filePath));

      return JsonParser.parseReader(reader);
    } catch (Exception e) {
      // Let's swallow the exception and return null
      System.out.println("Error with file read: " + e);
      return null;
    }
  }
  ;

  /**
   * @param filePath, full valid path to the file, starting from the level project is run from
   * @param elementToWrite
   * @return
   * @throws Exception
   */
  public static boolean write(String filePath, JsonElement elementToWrite) throws Exception {
    try {
      FileWriter writer = new FileWriter(filePath);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(elementToWrite, writer);
      writer.close();
      return true;
    } catch (Exception e) {
      // Let's swallow the exception and return false
      System.out.println("Error with file write: " + e);
      return false;
    }
  }
  ;
}
