package me.kiseok.jwtskeleton.domain.account.dto;

import lombok.*;
import me.kiseok.jwtskeleton.domain.account.Account;
import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class AccountResponseDto {

    private Long id;
    private String email;
    private String name;
    private String picture;
    private List<String> roles;

    public AccountResponseDto toResponse(Account account) {
        return AccountResponseDto.builder()
                .id(account.getId())
                .email(account.getEmail())
                .name(account.getName())
                .picture(account.getPicture())
                .roles(account.getRoles())
                .build();
    }
}
