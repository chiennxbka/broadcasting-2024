package com.mintpot.broadcasting.common.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mintpot.broadcasting.common.constants.IConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ChienNX
 * @CreatedDate Oct 4, 2017 2:44:45 PM
 */
@Component
public class JsonDateDeserialize extends JsonDeserializer<Date> {

  /*
   * (non-Javadoc)
   *
   * @see
   * com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml
   * .jackson.core.JsonParser,
   * com.fasterxml.jackson.databind.DeserializationContext)
   */

  @Override
  public Date deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
    SimpleDateFormat format = new SimpleDateFormat(IConstants.DATE_PATTERN);
    String date = jsonParser.getText();
    try {
      return format.parse(date);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
