package com.my.webstore.service.impl;

import com.my.webstore.exception.ServiceException;
import com.my.webstore.util.MessageSourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


/**
 * The service class implements all re-usable common functionalites that can be uses by inherited service classes
 */
@Service
public class BaseService {
    @Autowired
    private MessageSourceUtil messageSourceUtil;

    /**
     * The method provides abstract view of the message source get message method
     *
     * @param key    code of the message
     * @param params parameters for the message
     * @return constructed message, if found otherwise the given key
     */
    protected String getMessage(String key, Object... params) {
        return messageSourceUtil.getMessage(key, params);
    }


    /**
     * The method translate the data access exception to the business exception to provide detail error for consequence
     * layers
     *
     * @param e Exception instance of DataAccessException
     * @return ServiceException that wrap the data access exception
     */
    protected ServiceException translateException(DataAccessException e) {
        return new ServiceException(ServiceException.PROCESSING_FAILED,
                getMessage("label.web.store.proc.error.fetching.data"), e);
    }
}
