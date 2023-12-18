package sg.edu.nus.se.its.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Program;

/**
 * Tests the custom de-/serialization.
 */
public class JsonSerializerWithInheritanceTest {

  @Test
  void test_EmptyProgram() {

    Program sourceProgram = new Program();

    Gson gsonSerializer = new GsonBuilder()
        .registerTypeAdapter(Program.class, new JsonSerializerWithInheritance<Program>()).create();
    String json = gsonSerializer.toJson(sourceProgram);

    assertEquals("{\"importStatements\":[],\"fncs\":{},\"tokentype\":\"Program\"}", json);

    Gson gsonDeserializer = new GsonBuilder()
        .registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>())
        .create();

    Program targetProgram = gsonDeserializer.fromJson(json, Program.class);

    assertEquals(sourceProgram.getFncs(), targetProgram.getFncs());
  }

}
