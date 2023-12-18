package sg.edu.nus.se.its.interpreter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.util.JsonSerializerWithInheritance;
import sg.edu.nus.se.its.util.ServiceUtils;

/**
 * Helper class to access the current Interpreter implementation via the ITS services.
 */
public class InterpreterServiceImpl implements Interpreter {

  public static final boolean DEBUG = false;

  public static final String URL = "https://its.comp.nus.edu.sg/cs3213/interpreter";

  private static final String ENDPOINT_NOT_FOUND = "{\"detail\":\"Not Found\"}";
  private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";


  String languageIdentifier;
  String entryFunctionName;

  /**
   * Initializes the Service implementation for the interpreter.
   *
   * @param fileExtension - String defining the language ("c" or "py")
   * @param entryFunctionName - String defining the name of the entry function
   */
  public InterpreterServiceImpl(String fileExtension, String entryFunctionName) {
    if (fileExtension.equals("c")) {
      this.languageIdentifier = "c";
    } else if (fileExtension.equals("py")) {
      this.languageIdentifier = "py";
    } else {
      throw new RuntimeException("Unsupported source file language: " + fileExtension);
    }

    this.entryFunctionName = entryFunctionName;
  }

  public Trace executeProgram(Program program) {
    return this.executeProgram(program, null);
  }

  /**
   * Executes a program with the given input and produces an execution trace.
   *
   * @param program -- Program object
   * @param input -- program's input
   * @return execution trace
   */
  public Trace executeProgram(Program program, Input input) {
    String jsonPayload = null;

    try {
      jsonPayload = constructJsonRequest(program, input);
    } catch (IOException e) {
      throw new RuntimeException("Unexpected exception during json payload construction!", e);
    }

    if (DEBUG) {
      System.out.println("jsonPayload:");
      System.out.println(jsonPayload);
    }

    String response;
    try {
      response = ServiceUtils.post(URL, jsonPayload);
    } catch (IOException e) {
      throw new RuntimeException("Unexpected exception during ITS interpreter service call!", e);
    }

    if (DEBUG) {
      System.out.println("response:");
      System.out.println(response);
    }

    if (response.equals(ENDPOINT_NOT_FOUND)) {
      throw new RuntimeException("Endpoint not found! URL=" + URL);
    }

    if (response.equals(INTERNAL_SERVER_ERROR)) {
      throw new RuntimeException("Internal Server Error! URL=" + URL);
    }

    Trace trace = fromJson(response);

    if (DEBUG) {
      System.out.println("trace:");
      System.out.println(trace);
    }

    return trace;
  }

  private String constructJsonRequest(Program program, Input input) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    sb.append("\"language\": \"");
    sb.append(this.languageIdentifier);
    sb.append("\",");

    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.setPrettyPrinting();
    Gson gson = builder.create();
    sb.append("\"program_model\": \"");
    sb.append(
        gson.toJson(program).replace("\\\"", "\\\\\"").replace("\"", "\\\"").replace("\n", "\\n"));
    sb.append("\",");

    sb.append("\"function\": \"");
    sb.append(this.entryFunctionName);
    sb.append("\",");

    if (input != null) {

      sb.append("\"inputs\": \"");
      String[] inputs = input.getInputs();
      if (inputs != null && inputs.length > 0) {
        sb.append("[");
        for (int i = 0; i < inputs.length; i++) {
          sb.append(inputs[i]);
          if (i < inputs.length - 1) {
            sb.append(",");
          }
        }
        sb.append("]");
      }
      sb.append("\",");

      sb.append("\"args\": \"");
      String[] args = input.getArgs();
      if (args != null && args.length > 0) {
        sb.append("[");
        for (int i = 0; i < args.length; i++) {
          sb.append(args[i]);
          if (i < args.length - 1) {
            sb.append(",");
          }
        }
        sb.append("]");
      }
      sb.append("\"");

    } else {
      sb.append("\"inputs\": \"\",");
      sb.append("\"args\": \"\"");
    }

    sb.append("}");
    return sb.toString();
  }

  private Trace fromJson(String json) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    Gson gson = builder.create();
    return gson.fromJson(json, Trace.class);
  }

}
