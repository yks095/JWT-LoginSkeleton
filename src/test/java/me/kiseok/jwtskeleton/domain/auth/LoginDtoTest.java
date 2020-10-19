package me.kiseok.jwtskeleton.domain.auth;

import me.kiseok.jwtskeleton.domain.auth.dto.LoginRequest;
import me.kiseok.jwtskeleton.domain.auth.dto.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class LoginDtoTest {

    @DisplayName("LoginRequestDto @Getter @Builder 테스트")
    @Test
    void builder_getter_login_requestDto_test() {
        String email = "test@email.com";
        String password = "testPassword";

        LoginRequest requestDto = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        assertEquals(requestDto.getEmail(), email);
        assertEquals(requestDto.getPassword(), password);
    }

    @DisplayName("LoginRequestDto @Setter 테스트")
    @Test
    void setter_login_requestDto_test() {
        LoginRequest requestDto = LoginRequest.builder()
                .email("email")
                .password("password")
                .build();

        String email = "test@email.com";
        String password = "testPassword";

        requestDto.setEmail(email);
        requestDto.setPassword(password);

        assertEquals(requestDto.getEmail(), email);
        assertEquals(requestDto.getPassword(), password);
    }

    @DisplayName("LoginResponseDto @Getter @Setter 테스트")
    @Test
    void getter_setter_login_responseDto_test() {
        String accessToken = "testAccessToken";
        LoginResponse responseDto = new LoginResponse();

        responseDto.setAccessToken(accessToken);

        assertEquals(responseDto.getAccessToken(), accessToken);
    }

}
