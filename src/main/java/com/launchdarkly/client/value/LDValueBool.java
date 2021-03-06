package com.launchdarkly.client.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

@JsonAdapter(LDValueTypeAdapter.class)
final class LDValueBool extends LDValue {
  private static final LDValueBool TRUE = new LDValueBool(true);
  private static final LDValueBool FALSE = new LDValueBool(false);
  private static final JsonElement JSON_TRUE = new JsonPrimitive(true);
  private static final JsonElement JSON_FALSE = new JsonPrimitive(false);
  
  private final boolean value;
  
  static LDValueBool fromBoolean(boolean value) {
    return value ? TRUE : FALSE;
  }
  
  private LDValueBool(boolean value) {
    this.value = value;
  }
  
  public LDValueType getType() {
    return LDValueType.BOOLEAN;
  }

  @Override
  public boolean booleanValue() {
    return value;
  }

  @Override
  public String toJsonString() {
    return value ? "true" : "false";
  }
  
  @Override
  void write(JsonWriter writer) throws IOException {
    writer.value(value);
  }
  
  @Override
  JsonElement computeJsonElement() {
    return value ? JSON_TRUE : JSON_FALSE;
  }
}
