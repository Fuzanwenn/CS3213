package sg.edu.nus.se.its.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.StringEscapeUtils;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.util.JsonSerializerWithInheritance;
import sg.edu.nus.se.its.util.ServiceUtils;

/**
 * Helper class to access the current Parser implementation via the ITS services.
 */
public class ParserServiceImpl implements Parser {

  public static final boolean DEBUG = false;

  public static final String URL = "https://its.comp.nus.edu.sg/cs3213/parser";
  
  private static final String ENDPOINT_NOT_FOUND = "{\"detail\":\"Not Found\"}";

  @Override
  public Program parse(File filePath) throws IOException {
    String jsonPayload = constructJsonRequest(filePath);

    if (DEBUG) {
      System.out.println("jsonPayload:");
      System.out.println(jsonPayload);
    }

    String response = ServiceUtils.post(URL, jsonPayload);

    if (DEBUG) {
      System.out.println("response:");
      System.out.println(response);
    }
    
    if (response.equals(ENDPOINT_NOT_FOUND)) {
      throw new RuntimeException("Endpoint not found! URL=" + URL);
    }

    Program program = fromJson(response);

    if (DEBUG) {
      System.out.println("program:");
      System.out.println(program);
    }

    return program;
  }

  private String constructJsonRequest(File filePath) throws IOException {
    String fileExtension = FilenameUtils.getExtension(filePath.getName());
    
    String languageIdentifier;
    if (fileExtension.equals("c")) {
      languageIdentifier = "c";
    } else if (fileExtension.equals("py")) {
      languageIdentifier = "py";
    } else {
      throw new RuntimeException("Unsupported source file language: " + filePath.getAbsolutePath());
    }
    
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    
    sb.append("\"language\": \"");
    sb.append(languageIdentifier);
    sb.append("\",");
    
    sb.append("\"source_code\": \"");
    sb.append(StringEscapeUtils.escapeJava(Files.readString(filePath.toPath())));
    sb.append("\"");

    sb.append("}");
    return sb.toString();
  }
  
  private Program fromJson(String json) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    Gson gson = builder.create();
    return gson.fromJson(json, Program.class);
  }

}
