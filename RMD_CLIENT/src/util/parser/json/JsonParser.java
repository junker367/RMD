package util.parser.json;


public class JsonParser {
  
  public static Json loads(String s) throws JsonParserException{
    Json ret = null;
    try {
        ret= Json.loads(s);
    } catch (Exception e) {
      throw new JsonParserException(e.getMessage(), e);
    }
    return ret;
  }
  
  public static String parser(Json json) throws JsonParserException{
    String ret = null;
    try {
        ret= json.toString();
    } catch (Exception e) {
      throw new JsonParserException(e.getMessage(), e);
    }
    return ret;
  }
  
}
