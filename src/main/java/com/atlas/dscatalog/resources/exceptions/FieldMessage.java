package com.atlas.dscatalog.resources.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FieldMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 6064913566975491718L;

    private String fieldName;
    private String message;

}
