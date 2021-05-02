package com.my.webstore.controller;

import com.my.webstore.controller.dto.Response;
import com.my.webstore.exception.ServiceException;
import com.my.webstore.util.MessageSourceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BaseController {
    @Autowired
    private MessageSourceUtil messageSourceUtil;

    protected String getMessage(String key, Object... params) {
        return messageSourceUtil.getMessage(key, params);
    }

    protected void setErrors(ServiceException e, Response response) {
        log.error(e.getMessage(), e);
        if (e.getCode() == ServiceException.VALIDATION_FAILED) {
            response.setErrorMsgWithFailStatus(getMessage(e.getMessage(), (Object[]) e.getMessageArgs()));
        } else {
            response.setErrorMsgWithFailStatus(e.getMessage());
        }
    }
}
