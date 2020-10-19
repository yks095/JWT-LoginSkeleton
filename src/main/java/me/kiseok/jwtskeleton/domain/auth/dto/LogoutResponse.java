package me.kiseok.jwtskeleton.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LogoutResponse {

    private Long id;
    private LocalDateTime localDateTime;
}
