package com.junker.boilerplate.parser;

import com.junker.boilerplate.parser.json.Json;

public class ParserManager {

  public static String JsonToString(Json j) throws ParserException {
    String ret = null;
    try {
      ret = j.toString();
    } catch (Exception e) {
      throw new ParserException(e.getMessage(), e);
    }
    return ret;
  }

  public static Json StringToJson(String s) throws ParserException {
    Json ret = null;
    try {
      ret = Json.loads(s);
    } catch (Exception e) {
      throw new ParserException(e.getMessage(), e);
    }
    return ret;
  }

  /* FUTURE IMPLEMENTATIONS
   * public static String XmlToString(Xml x){}
   * 
   * public static Xml StringToXml(String s){}
   */
}