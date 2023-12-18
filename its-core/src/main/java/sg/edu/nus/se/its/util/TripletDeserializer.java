package sg.edu.nus.se.its.util;

import com.google.gson.*;
import org.javatuples.Triplet;

import java.lang.reflect.Type;

/**
 * Custom deserializer for triplets.
 */
public class TripletDeserializer<A, B, C> implements JsonDeserializer<Triplet<A, B, C>> {
    private Class<A> typeOfA;
    private Class<B> typeOfB;
    private Class<C> typeOfC;

    public TripletDeserializer(Class<A> typeOfA, Class<B> typeOfB, Class<C> typeOfC) {
        this.typeOfA = typeOfA;
        this.typeOfB = typeOfB;
        this.typeOfC = typeOfC;
    }


    @Override
    public Triplet<A, B, C> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject o = json.getAsJsonObject();
        A val0 = context.deserialize(o.get("val0"), typeOfA);
        B val1 = context.deserialize(o.get("val1"), typeOfB);
        C val2 = context.deserialize(o.get("val2"), typeOfC);
        return new Triplet<A,B,C>(val0, val1, val2);
    }
}