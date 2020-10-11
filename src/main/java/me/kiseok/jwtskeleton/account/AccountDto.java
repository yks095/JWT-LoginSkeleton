package me.kiseok.jwtskeleton.account;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class AccountDto {

    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String name;

    private String picture;

    public Account toEntity() {
        return Account.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();
    }
}
