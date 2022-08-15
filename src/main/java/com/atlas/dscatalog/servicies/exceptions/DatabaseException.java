package com.atlas.dscatalog.servicies.exceptions;

import java.io.Serial;

public class DatabaseException extends Exception{
    @Serial
    private static final long serialVersionUID = 1L;

    public DatabaseException(String msg) {
        super(msg);
    }
}