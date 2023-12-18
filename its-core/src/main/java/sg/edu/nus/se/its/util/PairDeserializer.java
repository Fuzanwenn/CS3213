package sg.edu.nus.se.its.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import org.javatuples.Pair;

/**
 * Custom deserializer for pairs.
 */
public class PairDeserializer<K, V> implements JsonDeserializer<Pair<K, V>> {
  private Class<K> typeOfK;
  private Class<V> typeOfV;

  public PairDeserializer(Class<K> typeOfK, Class<V> typeOfV) {
    this.typeOfK = typeOfK;
    this.typeOfV = typeOfV;
  }

  @Override
  public Pair<K, V> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
    JsonObject tupleValues = json.getAsJsonObject();
    K key = context.deserialize(tupleValues.get("val0"), typeOfK);
    V val = context.deserialize(tupleValues.get("val1"), typeOfV);
    return Pair.with(key, val);
  }
}

