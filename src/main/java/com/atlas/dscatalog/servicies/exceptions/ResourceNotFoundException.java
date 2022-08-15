package com.atlas.dscatalog.servicies.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends Exception{
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}