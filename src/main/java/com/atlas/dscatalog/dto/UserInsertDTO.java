package com.atlas.dscatalog.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class UserInsertDTO extends UserDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    private String password;

    UserInsertDTO() {
        super();
    }
}
