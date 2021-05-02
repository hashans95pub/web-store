package com.my.webstore.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    public static final short STATUS_FAIL = 0;
    public static final short STATUS_SUCCESS = 1;

    private short status;
    private T result;
    private String errorMsg;

    public Response(short status, T result) {
        this.status = status;
        this.result = result;
    }

    public void setErrorMsgWithFailStatus(String errorMsg) {
        this.status = STATUS_FAIL;
        this.errorMsg = errorMsg;
    }

    public void setResultWithSuccessStatus(T result) {
        this.status = STATUS_SUCCESS;
        this.result = result;
    }
}
