package com.emotion_apiserver.config.util;

public class CustomJWTException extends RuntimeException{

    public CustomJWTException(String msg){
        super(msg);
    }
}
