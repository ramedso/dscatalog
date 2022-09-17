package com.atlas.dscatalog.dto;

import com.atlas.dscatalog.servicies.validation.UserInsertValid;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@UserInsertValid
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
