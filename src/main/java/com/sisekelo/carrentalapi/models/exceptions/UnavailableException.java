package com.sisekelo.carrentalapi.models.exceptions;

import java.text.MessageFormat;

public class UnavailableException extends RuntimeException{
    public UnavailableException(final Long id, String object, String reference){
        super(MessageFormat.format("Error: {1} exists in {2} id:{0}", id, object));
    }
}
