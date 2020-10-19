package me.kiseok.jwtskeleton.domain.auth.dto;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LoginRequest {

    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;
}
