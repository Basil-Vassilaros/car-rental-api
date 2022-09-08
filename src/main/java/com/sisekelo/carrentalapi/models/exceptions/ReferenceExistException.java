package com.sisekelo.carrentalapi.models.exceptions;

import java.text.MessageFormat;

public class ReferenceExistException extends RuntimeException{
    public ReferenceExistException(final Long id, String toDelete, String fromList){
        super(MessageFormat.format("Error: {1} exists in {2} id:{0}", id, toDelete,fromList));
    }
}
