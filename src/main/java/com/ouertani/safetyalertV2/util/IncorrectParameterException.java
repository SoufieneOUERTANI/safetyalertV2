package com.ouertani.safetyalertV2.util;
public class IncorrectParameterException extends Exception { 
    public IncorrectParameterException(String errorMessage) {
        super(errorMessage);
    }
}