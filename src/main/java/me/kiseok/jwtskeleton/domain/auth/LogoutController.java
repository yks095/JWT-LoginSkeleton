package me.kiseok.jwtskeleton.domain.auth;

import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.domain.auth.dto.LogoutResponse;
import me.kiseok.jwtskeleton.domain.token.Token;
import me.kiseok.jwtskeleton.domain.token.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping(value = "/api/logout")
@RestController
public class LogoutController {

    private final TokenRepository tokenRepository;

    @PostMapping
    ResponseEntity<?> logout(HttpServletRequest request)    {
        String header = Optional
                .of(request.getHeader("Authorization").substring(7))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UnAuthorized"));

        Token token = createTokenEntity(header);
        token = tokenRepository.save(token);
        SecurityContextHolder.getContext().setAuthentication(null);

        return new ResponseEntity<>(new LogoutResponse(token.getId(), token.getLocalDateTime()), HttpStatus.OK);
    }

    private Token createTokenEntity(String header) {
        return Token.builder()
                .token(header)
                .localDateTime(LocalDateTime.now())
                .build();
    }

}
