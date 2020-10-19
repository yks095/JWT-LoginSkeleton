package me.kiseok.jwtskeleton.domain.account.dto;

import lombok.*;
import me.kiseok.jwtskeleton.domain.account.Account;
import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class AccountResponse {

    private Long id;
    private String email;
    private String name;
    private String picture;
    private List<String> roles;

    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .name(account.getName())
                .picture(account.getPicture())
                .roles(account.getRoles())
                .build();
    }
}
