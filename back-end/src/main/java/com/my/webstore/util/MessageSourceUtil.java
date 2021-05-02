package com.my.webstore.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * The class will provide abstract view of the spring message source methods
 */
@Log4j2
@Component
public class MessageSourceUtil {
    @Autowired
    private MessageSource messageSource;

    /** The method get messages from the message for given key with parameters
     *
     * @param key code of the message
     * @param params parameters for the message
     * @return if found, constructed message otherwise the given key
     */
    public String getMessage(String key, Object... params) {
        try {
            return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            log.error(e.getMessage(), e);
        }
        return String.format("%s", key);
    }
}
