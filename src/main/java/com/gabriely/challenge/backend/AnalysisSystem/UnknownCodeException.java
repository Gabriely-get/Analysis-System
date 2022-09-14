package com.gabriely.challenge.backend.AnalysisSystem;

public class UnknownCodeException extends RuntimeException {

    public UnknownCodeException() {
        super();
    }

    public UnknownCodeException(String errorMessage) {
        super(errorMessage);
    }

    public UnknownCodeException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }

}
