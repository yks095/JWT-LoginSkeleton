package me.kiseok.jwtskeleton.domain.login.dto;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LoginRequestDto {

    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;
}
