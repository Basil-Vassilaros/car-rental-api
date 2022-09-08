package com.sisekelo.carrentalapi.models.exceptions;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException{
    public NotFoundException(final Long id, String reference){
        super(MessageFormat.format("Error: could not find {1} with id {0}", id, reference));
    }
}
