package com.mintpot.broadcasting.common.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mintpot.broadcasting.common.constants.IConstants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateSerializer extends JsonSerializer<Date> {

  /*
   * (non-Javadoc)
   *
   * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider)
   */
  public void serialize(Date value, JsonGenerator gen, SerializerProvider arg) throws IOException {
    gen.writeString(JsonDateSerializer.serialize(value));
  }

  /**
   * Serialize a date in ISO 8601 format.
   *
   * @param value
   * @return
   */
  public static String serialize(Date value) {
    SimpleDateFormat formatter = new SimpleDateFormat(IConstants.DATE_PATTERN);
    return formatter.format(value);
  }
}
