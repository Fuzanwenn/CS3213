package sg.edu.nus.se.its.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Variable;

/**
 * Custom JSON De-/serializer to incorporate class inheritance.
 */
public class JsonSerializerWithInheritance<T>
    implements JsonSerializer<sg.edu.nus.se.its.util.JsonSerializable>,
    JsonDeserializer<sg.edu.nus.se.its.util.JsonSerializable> {

  static final String TYPE_PROPERTY_NAME = "tokentype";
  static final String DATAMODEL_PACKAGE_NAME = "sg.edu.nus.se.its.model";

  /**
   * Add the tokentype for Operation arguments recursively.
   *
   * @param src A Json Serializable object
   * @param argObject The argument object of the Operation
   */
  public void addPropertyName(sg.edu.nus.se.its.util.JsonSerializable src, JsonObject argObject) {
    if (src instanceof Operation) {
      JsonArray args = argObject.getAsJsonArray("args");
      List<Expression> argsExpression = ((Operation) src).getArgs();
      for (int i = 0; i < argsExpression.size(); i++) {
        JsonObject arg = args.get(i).getAsJsonObject();
        addPropertyName(argsExpression.get(i), arg);
      }
      argObject.addProperty(TYPE_PROPERTY_NAME, src.getType());
    } else if (src instanceof Variable || src instanceof Constant) {
      argObject.addProperty(TYPE_PROPERTY_NAME, src.getType());
    }
  }

  @Override
  public JsonElement serialize(sg.edu.nus.se.its.util.JsonSerializable src, Type typeOfSrc,
      JsonSerializationContext context) {

    JsonObject object;
    if (src.toString().isBlank()) {
      object = (JsonObject) new Gson().toJsonTree(src);
    } else {
      object = context.serialize(src).getAsJsonObject();
    }

    object.addProperty(TYPE_PROPERTY_NAME, src.getType());

    addPropertyName(src, object);
    return object;
  }

  @Override
  public sg.edu.nus.se.its.util.JsonSerializable deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get(TYPE_PROPERTY_NAME);

    String className = DATAMODEL_PACKAGE_NAME + "." + classNamePrimitive.getAsString();

    Class<?> clazz;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new JsonParseException(e.getMessage());
    }
    return context.deserialize(jsonObject, clazz);
  }
}
