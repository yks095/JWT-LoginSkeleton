package me.kiseok.jwtskeleton.domain.account.dto;

import lombok.*;
import me.kiseok.jwtskeleton.domain.account.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collections;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class AccountRequest {

    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String name;

    private String picture;

    public Account toEntity(PasswordEncoder passwordEncoder) {
        return Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .picture(picture)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}
