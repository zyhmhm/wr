package com.wr.web;

import com.wr.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//==@ControllerAdvice+@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    //@ResponseBody
    public JsonResult doHandleRuntimeException(
            RuntimeException e) {
        e.printStackTrace();
        return JsonResult.build(400, e.getMessage());
    }

}

