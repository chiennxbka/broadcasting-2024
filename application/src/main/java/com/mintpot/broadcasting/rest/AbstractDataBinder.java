package com.mintpot.broadcasting.rest;

import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractDataBinder {

    @Value("${inbound.date.format}")
    private String clientDateFormat;

    private DateFormat dateFormat;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(clientDateFormat);
        }
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    setValue(dateFormat.parse(text));
                } catch (ParseException e) {
                    throw new BusinessException(ErrorCode.VALIDATION_FAILED);
                }
            }
        });
    }
}
